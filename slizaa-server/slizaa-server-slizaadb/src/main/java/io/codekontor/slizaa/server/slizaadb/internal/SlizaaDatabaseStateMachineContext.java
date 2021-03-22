/**
 * slizaa-server-slizaadb - Slizaa Static Software Analysis Tools
 * Copyright © 2019 Code-Kontor GmbH and others (slizaa@codekontor.io)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.codekontor.slizaa.server.slizaadb.internal;

import io.codekontor.slizaa.core.boltclient.IBoltClient;
import io.codekontor.slizaa.core.progressmonitor.DefaultProgressMonitor;
import io.codekontor.slizaa.scanner.api.graphdb.IGraphDb;
import io.codekontor.slizaa.scanner.api.importer.IModelImporter;
import io.codekontor.slizaa.scanner.spi.contentdefinition.AbstractContentDefinitionProvider;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import io.codekontor.slizaa.server.slizaadb.IHierarchicalGraph;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabaseSPI;
import io.codekontor.slizaa.server.slizaadb.SlizaaDatabaseState;
import io.codekontor.slizaa.server.slizaadb.SlizaaSocketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateMachine;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class SlizaaDatabaseStateMachineContext {

    private static Logger LOGGER = LoggerFactory
            .getLogger(SlizaaDatabaseStateMachineContext.class);

    private final ISlizaaDatabaseSPI _graphDatabaseEnvironment;

    /* the state machine */
    private final StateMachine<SlizaaDatabaseState, SlizaaDatabaseTrigger> _stateMachine;

    private final ExecutorService _executorService;

    private SlizaaDatabaseImpl _graphDatabase;

    private String _identifier;

    private File _databaseDirectory;

    private int _port = -1;

    private IContentDefinitionProvider<?> _contentDefinitionProvider;

    private Map<String, HierarchicalGraph> _hierarchicalGraphs;

    private IGraphDb _graphDb;

    private IBoltClient _boltClient;

    public SlizaaDatabaseStateMachineContext(
            String identifier,
            File databaseDirectory,
            int port,
            ISlizaaDatabaseSPI graphDatabaseEnvironment,
            ExecutorService executorService,
            StateMachine<SlizaaDatabaseState, SlizaaDatabaseTrigger> stateMachine) {

        this._identifier = checkNotNull(identifier);
        this._databaseDirectory = checkNotNull(databaseDirectory);
        this._port = SlizaaSocketUtils.available(port) ? port : SlizaaSocketUtils.findAvailableTcpPort();
        this._graphDatabaseEnvironment = checkNotNull(graphDatabaseEnvironment);
        this._stateMachine = checkNotNull(stateMachine);
        this._executorService = checkNotNull(executorService);

        _hierarchicalGraphs = new HashMap<>();
    }

    public String getIdentifier() {
        return _identifier;
    }

    public int getPort() {
        return _port;
    }

    public File getDatabaseDirectory() {
        return _databaseDirectory;
    }

    public boolean hasGraphDb() {
        return _graphDb != null;
    }

    public boolean hasBoltClient() {
        return _boltClient != null;
    }

    public boolean isRunning() {
        return _graphDb != null;
    }

    public boolean hasPopulatedDatabaseDirectory() {
        return _databaseDirectory.isDirectory() && _databaseDirectory.list().length > 0;
    }

    public void setContentDefinition(IContentDefinitionProvider<?> contentDefinitionProvider) {
        LOGGER.debug("setContentDefinitionProvider({})", contentDefinitionProvider);
        this._contentDefinitionProvider = contentDefinitionProvider;
    }

    public boolean hasContentDefinitionProvider() {
        return _contentDefinitionProvider != null;
    }

    public IContentDefinitionProvider<?> getContentDefinitionProvider() {
        return _contentDefinitionProvider;
    }

    public void disposeHierarchicalGraph(String identifier) {
        checkState(SlizaaDatabaseState.RUNNING.equals(this._stateMachine.getState().getId()), "Database must be RUNNING to remove a hierarchical graph. Current state: %s.",
                this._stateMachine.getState().getId());

        HierarchicalGraph hierarchicalGraph = _hierarchicalGraphs.remove(identifier);
        this._graphDatabaseEnvironment.handleHierarchicalGraphRemoved(hierarchicalGraph);
    }

    public List<IHierarchicalGraph> getHierarchicalGraphs() {
        if (!SlizaaDatabaseState.RUNNING.equals(this._stateMachine.getState().getId())) {
            return Collections.emptyList();
        }
        return new ArrayList<>(_hierarchicalGraphs.values());
    }

    public IHierarchicalGraph getHierarchicalGraph(String identifier) {
        if (!SlizaaDatabaseState.RUNNING.equals(this._stateMachine.getState().getId())) {
            return null;
        }
        return _hierarchicalGraphs.get(identifier);
    }

    protected void recomputePort() {
        if (_port == -1 || !SlizaaSocketUtils.available(_port)) {
            _port = SlizaaSocketUtils.findAvailableTcpPort();
        }
    }

    protected void clearDatabaseDirectory() {
        if (_databaseDirectory.exists()) {
            try {
                Files.walk(_databaseDirectory.toPath(), FileVisitOption.FOLLOW_LINKS).sorted(Comparator.reverseOrder())
                        .map(Path::toFile).forEach(File::delete);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {

        // execute async
        _executorService.submit(() -> {
            try {

                // start the database
                recomputePort();
                _graphDb = executeWithCtxClassLoader(() -> {
                    return _graphDatabaseEnvironment.createGraphDb(getPort(), getDatabaseDirectory());
                });

                // connect the internal client
                connectBoltClient();

                // initialize the hierarchical graphs
                _hierarchicalGraphs.values().forEach(hierarchicalGraph -> hierarchicalGraph.initialize(true));

                _stateMachine.sendEvent(SlizaaDatabaseTrigger.START_SUCCEEDED);

            } catch (Exception exception) {
                // TODO LOG
                exception.printStackTrace();
                _stateMachine.sendEvent(SlizaaDatabaseTrigger.START_FAILED);
            }

            return null;
        });
    }

    public void stop() {

        // execute async
        _executorService.submit(() -> {
            try {

                _hierarchicalGraphs.values().forEach(hierarchicalGraph -> hierarchicalGraph.dispose());
                if (this._boltClient != null) {
                    this._boltClient.disconnect();
                    this._boltClient = null;
                }
                if (this._graphDb != null) {
                    this._graphDb.shutdown();
                    this._graphDb = null;
                }

                _stateMachine.sendEvent(SlizaaDatabaseTrigger.STOP_SUCCEEDED);

            } catch (Exception exception) {
                // TODO LOG
                exception.printStackTrace();
                _stateMachine.sendEvent(SlizaaDatabaseTrigger.STOP_FAILED);
            }

            return null;
        });
    }

    public void parse(boolean startDatabase) {

        _executorService.submit(() -> {

            try {

                boolean isRunning = _parse(startDatabase);

                if (isRunning) {
                    _stateMachine.sendEvent(SlizaaDatabaseTrigger.PARSE_WITH_START_SUCCEEDED);
                } else {
                    _stateMachine.sendEvent(SlizaaDatabaseTrigger.PARSE_WITHOUT_START_SUCCEEDED);
                }

            } catch (Exception exception) {
                // TODO LOG
                exception.printStackTrace();
                _stateMachine.sendEvent(SlizaaDatabaseTrigger.PARSE_FAILED);
            }

            return null;
        });
    }

    /**
     * @throws Exception
     */
    private boolean _parse(boolean startDatabase) {

        // delete all contained files
        clearDatabaseDirectory();

        // re-initialize the content definition provider
        if (getContentDefinitionProvider() instanceof AbstractContentDefinitionProvider) {
            ((AbstractContentDefinitionProvider<?>) getContentDefinitionProvider()).dispose();
            ((AbstractContentDefinitionProvider<?>) getContentDefinitionProvider()).initialize();
        }

        // create the graph database
        _graphDb = executeWithCtxClassLoader(() -> {

            IModelImporter modelImporter = _graphDatabaseEnvironment.createModelImporter(getContentDefinitionProvider(), getDatabaseDirectory());

            // parse the model
            if (startDatabase) {

                //
                modelImporter.parse(new DefaultProgressMonitor("Parse", 100, DefaultProgressMonitor.consoleLogger()), () -> {

                    recomputePort();

                    return _graphDatabaseEnvironment.createGraphDb(getPort(), getDatabaseDirectory());
                });

                connectBoltClient();

                _hierarchicalGraphs.values().forEach(hierarchicalGraph -> hierarchicalGraph.initialize(true));

                return modelImporter.getGraphDb();
            } else {
                modelImporter.parse(new DefaultProgressMonitor("Parse", 100, DefaultProgressMonitor.consoleLogger()));
                return null;
            }
        });

        return isRunning();
    }

    public void terminate() {
        stop();

        _graphDatabaseEnvironment.handleGraphDatabaseRemoved(getIdentifier());
        clearDatabaseDirectory();
    }


    /**
     * @param identifier
     * @return
     */
    public void createHierarchicalGraph(String identifier) {
        _executorService.submit(() -> {

            try {

                // TODO: move to start/stop
                connectBoltClient();

                //
                HierarchicalGraph hierarchicalGraph = new HierarchicalGraph(new HierarchicalGraphDefinition(identifier),
                        getGraphDatabase(), (def) -> _graphDatabaseEnvironment.mapHierarchicalGraph(def, this._boltClient));

                hierarchicalGraph.initialize(true);

                _hierarchicalGraphs.put(identifier, hierarchicalGraph);

                _stateMachine.sendEvent(SlizaaDatabaseTrigger.CREATE_HIERARCHICAL_GRAPH_SUCCEEDED);

            } catch (Exception exception) {
                // TODO LOG
                exception.printStackTrace();
                _stateMachine.sendEvent(SlizaaDatabaseTrigger.CREATE_HIERARCHICAL_GRAPH_FAILED);
            }

            return null;
        });
    }


    void setGraphDatabase(SlizaaDatabaseImpl structureDatabase) {
        _graphDatabase = structureDatabase;
    }

    SlizaaDatabaseImpl getGraphDatabase() {
        return _graphDatabase;
    }

    /**
     * @param action
     * @return
     * @throws Exception
     */
    private <T> T executeWithCtxClassLoader(Callable<T> action) {

        //
        checkNotNull(action);

        // store the current ctx class loader
        ClassLoader currentContextClassLoader = Thread.currentThread().getContextClassLoader();

        try {

            // set the extension class loader as the context class loader
            Thread.currentThread()
                    .setContextClassLoader(_graphDatabaseEnvironment.getCurrentExtensionClassLoader());

            // execute the action
            try {
                T result = action.call();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

        } finally {

            // reset the current ctx class loader
            Thread.currentThread().setContextClassLoader(currentContextClassLoader);
        }
    }

    private void connectBoltClient() {
        if (_boltClient == null) {
            // TODO!
            this._boltClient = _graphDatabaseEnvironment.createBoltClient("bolt://localhost:" + getPort());

            this._boltClient.connect();
        }
    }
}
