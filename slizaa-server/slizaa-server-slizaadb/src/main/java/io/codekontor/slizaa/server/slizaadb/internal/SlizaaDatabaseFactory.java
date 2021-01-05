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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.google.common.base.Preconditions.checkState;

@Component
public class SlizaaDatabaseFactory implements ISlizaaDatabaseFactory, InitializingBean {

    @Autowired
    private StateMachineFactory<SlizaaDatabaseState, SlizaaDatabaseTrigger> _stateMachineFactory;

    @Autowired
    private ISlizaaDatabaseEnvironment _graphDatabaseEnvironment;

    private Map<StateMachine<SlizaaDatabaseState, SlizaaDatabaseTrigger>, SlizaaDatabaseStateMachineContext> _stateMachine2StructureDatabaseContext = new HashMap<>();

    private NamedLock _namedLock;

    private ExecutorService _executorService;

    @Override
    public void afterPropertiesSet() throws Exception {
        _namedLock = new NamedLock();
        // TODO: Config
        _executorService = Executors.newFixedThreadPool(5);
    }

    @Override
    public IInternalSlizaaDatabase newInstance(String id, int port, File databaseRootDirectory) {

        // create
        SlizaaDatabaseImpl result = createDatabaseImpl(id, port, databaseRootDirectory);

        // start
        result.stateMachine().start();

        // finally return the result
        return createProxy(result);
    }

    @Override
    public IInternalSlizaaDatabase newInstance(ISlizaaDatabaseConfiguration databaseConfiguration, File databaseRootDirectory) {

        // method internal helper structure
        class ModifiedState {
            SlizaaDatabaseState modifiedState;
            boolean triggerStart;
        }

        SlizaaDatabaseImpl result = createDatabaseImpl(databaseConfiguration.getIdentifier(), databaseConfiguration.getPort(), databaseRootDirectory);

        //
        if (databaseConfiguration.hasContentDefinition()) {

            IContentDefinitionProvider<?> contentDefinitionProvider = _graphDatabaseEnvironment.createContentDefinitionProvider(
                    databaseConfiguration.getContentDefinitionFactoryId(), databaseConfiguration.getContentDefinitionExternalRepresentation());

            result.stateMachineContext().setContentDefinition(contentDefinitionProvider);
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
        result.stateMachine().getStateMachineAccessor().doWithAllRegions(access -> access
                .resetStateMachine(new DefaultStateMachineContext<>(modifiedState.modifiedState, null, null, null)));

        // start
        result.stateMachine().start();

        //
        if (modifiedState.triggerStart) {
            result.start();
        }

        //
        return createProxy(result);
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

    private IInternalSlizaaDatabase createProxy(SlizaaDatabaseImpl graphDatabase) {
        return (IInternalSlizaaDatabase) Proxy.newProxyInstance(
                SlizaaDatabaseFactory.class.getClassLoader(),
                new Class[] { IInternalSlizaaDatabase.class },
                new SynchronizedSlizaaDatabaseInvocationHandler(graphDatabase, _namedLock));
    }
}
