/**
 * slizaa-server-service-slizaa - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.slizaa.internal.graphdatabase;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;

import io.codekontor.slizaa.server.service.slizaa.GraphDatabaseState;
import io.codekontor.slizaa.server.service.slizaa.IGraphDatabase;

@Component
public class GraphDatabaseFactory {

  @Autowired
  private StateMachineFactory<GraphDatabaseState, GraphDatabaseTrigger>                                  _stateMachineFactory;

  @Autowired
  private IGraphDatabaseStateMachineContextFactory                                                       _graphDatabaseStateMachineContextFactory;

  private Map<StateMachine<GraphDatabaseState, GraphDatabaseTrigger>, IGraphDatabaseStateMachineContext> _stateMachine2StructureDatabaseContext = new HashMap<>();

  /**
   * 
   * @param configuration
   * @return
   */
  public IGraphDatabase newInstance(IGraphDatabaseConfiguration configuration) {

    checkNotNull(configuration);

    // create the database directory
    if (!configuration.getDirectory().exists()) {
      configuration.getDirectory().mkdirs();
    }

    // create the new state machine
    StateMachine<GraphDatabaseState, GraphDatabaseTrigger> statemachine = _stateMachineFactory.getStateMachine();

    // create the state machine context
    IGraphDatabaseStateMachineContext stateMachineContext = _graphDatabaseStateMachineContextFactory
        .createGraphDatabaseStateMachineContext(configuration.getIdentifier(), configuration.getDirectory(), configuration.getPort());

    // HERE!!

    // create the structure database
    GraphDatabaseImpl structureDatabase = new GraphDatabaseImpl(statemachine, stateMachineContext);

    // store the association
    _stateMachine2StructureDatabaseContext.put(statemachine, stateMachineContext);

    // now start...
    statemachine.start();

    // finally return the result
    return structureDatabase;
  }

  IGraphDatabaseStateMachineContext context(StateMachine<GraphDatabaseState, GraphDatabaseTrigger> stateMachine) {
    return _stateMachine2StructureDatabaseContext.get(stateMachine);
  }
}
