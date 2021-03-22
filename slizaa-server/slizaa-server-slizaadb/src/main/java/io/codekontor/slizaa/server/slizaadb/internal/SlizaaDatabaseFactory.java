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

import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import io.codekontor.slizaa.server.slizaadb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import static com.google.common.base.Preconditions.checkState;

@Component
public class SlizaaDatabaseFactory implements ISlizaaDatabaseFactory, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlizaaDatabaseFactory.class);

    @Autowired
    private StateMachineFactory<SlizaaDatabaseState, SlizaaDatabaseTrigger> _stateMachineFactory;

    @Autowired
    private ISlizaaDatabaseSPI _graphDatabaseEnvironment;

    private Map<StateMachine<SlizaaDatabaseState, SlizaaDatabaseTrigger>, SlizaaDatabaseStateMachineContext> _stateMachine2StructureDatabaseContext = new HashMap<>();

    private ExecutorService _executorService;

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO: Config
        _executorService = Executors.newFixedThreadPool(5);
    }

    @Override
    public SlizaaDatabaseImpl newInstanceFromConfiguration(String id, int port, File databaseRootDirectory) {

        // create
        SlizaaDatabaseImpl result = createDatabaseImpl(id, port, databaseRootDirectory);

        // start
        result.stateMachine().start();

        // finally return the result
        return result;
    }

    @Override
    public SlizaaDatabaseImpl newInstanceFromConfiguration(ISlizaaDatabaseConfiguration databaseConfiguration, File databaseRootDirectory) {

        // method internal helper structure
        class ModifiedState {
            SlizaaDatabaseState modifiedState;
            boolean triggerStart;
        }

        LOGGER.info("Creating new slizaa database instance '{}'. ", databaseConfiguration.getIdentifier());

        SlizaaDatabaseImpl slizaaDatabase = createDatabaseImpl(databaseConfiguration.getIdentifier(), databaseConfiguration.getPort(), databaseRootDirectory);

        //
        if (databaseConfiguration.hasContentDefinition()) {

            LOGGER.info("Setting content definition for slizaa database '{}'. ", databaseConfiguration.getIdentifier());

            IContentDefinitionProvider<?> contentDefinitionProvider = _graphDatabaseEnvironment.createContentDefinitionProvider(
                    databaseConfiguration.getContentDefinitionFactoryId(), databaseConfiguration.getContentDefinitionExternalRepresentation());

            slizaaDatabase.stateMachineContext().setContentDefinition(contentDefinitionProvider);
        }

        //
        ModifiedState modifiedState = new ModifiedState();
        switch (databaseConfiguration.getState()) {
            case RUNNING:
            case STARTING:
            case CREATING_HIERARCHICAL_GRAPH:{
                modifiedState.modifiedState = SlizaaDatabaseState.NOT_RUNNING;
                modifiedState.triggerStart = true;
                break;
            }
            case STOPPING: {
                modifiedState.modifiedState = SlizaaDatabaseState.NOT_RUNNING;
                modifiedState.triggerStart = false;
                break;
            }
            case PARSING: {
                modifiedState.modifiedState = SlizaaDatabaseState.CONFIGURED;
                modifiedState.triggerStart = false;
                break;
            }
            default: {
                modifiedState.modifiedState = databaseConfiguration.getState();
                modifiedState.triggerStart = false;
            }
        }

        //
        slizaaDatabase.stateMachine().getStateMachineAccessor().doWithAllRegions(access -> access
                .resetStateMachine(new DefaultStateMachineContext<>(modifiedState.modifiedState, null, null, null)));

        // start
        slizaaDatabase.stateMachine().start();

        //
        if (modifiedState.triggerStart) {
            LOGGER.info("Starting slizaa database {}.", databaseConfiguration.getIdentifier());
            slizaaDatabase.start();
            try {
                slizaaDatabase.awaitState(SlizaaDatabaseState.STARTING, 10000L);
            } catch (TimeoutException e) {
                LOGGER.error("Timeout while starting slizaa database {}.",  e.getMessage());
            }
        }

        LOGGER.info("Successfully created slizaa database {}.", databaseConfiguration.getIdentifier());

        //
        return slizaaDatabase;
    }

    /**
     * @param id
     * @param port
     * @param databaseRootDirectory
     * @return
     */
    private SlizaaDatabaseImpl createDatabaseImpl(String id, int port, File databaseRootDirectory) {

        checkState(id != null && !id.isEmpty());
        checkState(databaseRootDirectory != null);

        // create the database directory
        File databaseDirectory = new File(databaseRootDirectory, id);
        if (!databaseDirectory.exists()) {
            databaseDirectory.mkdirs();
        }

        // create the new state machine
        StateMachine<SlizaaDatabaseState, SlizaaDatabaseTrigger> statemachine = _stateMachineFactory.getStateMachine();

        // create the state machine context
        SlizaaDatabaseStateMachineContext stateMachineContext =  new SlizaaDatabaseStateMachineContext(id, databaseDirectory, port, _graphDatabaseEnvironment, _executorService, statemachine);

        // create the structure database
        SlizaaDatabaseImpl database = new SlizaaDatabaseImpl(statemachine, stateMachineContext, _graphDatabaseEnvironment);

        // store the association
        _stateMachine2StructureDatabaseContext.put(statemachine, stateMachineContext);

        // finally return the result
        return database;
    }

    SlizaaDatabaseStateMachineContext context(StateMachine<SlizaaDatabaseState, SlizaaDatabaseTrigger> stateMachine) {
        return _stateMachine2StructureDatabaseContext.get(stateMachine);
    }
}
