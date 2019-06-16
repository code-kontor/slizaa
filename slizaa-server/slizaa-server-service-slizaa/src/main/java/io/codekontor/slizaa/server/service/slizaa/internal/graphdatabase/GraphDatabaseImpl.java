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
import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;

import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import io.codekontor.slizaa.scanner.spi.contentdefinition.InvalidContentDefinitionException;
import io.codekontor.slizaa.server.service.slizaa.GraphDatabaseState;
import io.codekontor.slizaa.server.service.slizaa.IGraphDatabase;
import io.codekontor.slizaa.server.service.slizaa.IHierarchicalGraph;

/**
 *
 */
public class GraphDatabaseImpl implements IGraphDatabase {

  public static final String                                     START_DATABASE_AFTER_PARSING = "START_DATABASE_AFTER_PARSING";

  public static final String                                     CONTENT_DEFINITION_PROVIDER  = "CONTENT_DEFINITION_PROVIDER";

  /** the state machine **/
  private StateMachine<GraphDatabaseState, GraphDatabaseTrigger> _stateMachine;

  /**
   * the state machine context
   **/
  private IGraphDatabaseStateMachineContext                      _stateMachineContext;

  /**
   * @param stateMachine
   * @param stateMachineContext
   */
  GraphDatabaseImpl(StateMachine<GraphDatabaseState, GraphDatabaseTrigger> stateMachine,
      IGraphDatabaseStateMachineContext stateMachineContext) {

    this._stateMachine = checkNotNull(stateMachine);
    this._stateMachineContext = checkNotNull(stateMachineContext);
    this._stateMachineContext.setGraphDatabase(this);
  }

  @Override
  public String getIdentifier() {
    return _stateMachineContext.getIdentifier();
  }

  @Override
  public int getPort() {
    return _stateMachineContext.getPort();
  }

  @Override
  public void setContentDefinitionProvider(String contentDefinitionFactoryId, String contentDefinition) {

    //
    IContentDefinitionProvider<?> contentDefinitionProvider = _stateMachineContext.createContentDefinitionProvider(contentDefinitionFactoryId, contentDefinition);

    //
    if (contentDefinitionProvider == null) {
     throw new InvalidContentDefinitionException(String.format("Invalid content definition ('%s', '%s').", contentDefinitionFactoryId, contentDefinition)); 
    }
    
    //
    Message<GraphDatabaseTrigger> triggerMessage = MessageBuilder
        .withPayload(GraphDatabaseTrigger.SET_CONTENT_DEFINITION)
        .setHeader(CONTENT_DEFINITION_PROVIDER, contentDefinitionProvider)
        .build();

    trigger(triggerMessage);
  }

  /**
   * @return
   */
  @Override
  public boolean hasContentDefinition() {
    return _stateMachineContext.hasContentDefinitionProvider();
  }

  /**
   * 
   */
  @Override
  public IContentDefinitionProvider<?> getContentDefinition() {
    return _stateMachineContext.getContentDefinitionProvider();
  }

  @Override
  public IHierarchicalGraph newHierarchicalGraph(String identifier) {

    checkState(GraphDatabaseState.RUNNING.equals(this._stateMachine.getState().getId()), "Database is not running:  %s",
        this._stateMachine.getState().getId());

    IHierarchicalGraph result = _stateMachineContext.createHierarchicalGraph(identifier);
    _stateMachineContext.storeConfiguration();
    return result;
  }

  @Override
  public void removeHierarchicalGraph(String identifier) {

    checkState(GraphDatabaseState.RUNNING.equals(this._stateMachine.getState().getId()), "Database is not running:  %s",
        this._stateMachine.getState().getId());

    _stateMachineContext.disposeHierarchicalGraph(identifier);
  }

  @Override
  public IHierarchicalGraph getHierarchicalGraph(String identifier) {

    if (!GraphDatabaseState.RUNNING.equals(this._stateMachine.getState().getId())) {
      return null;
    }

    return _stateMachineContext.getHierarchicalGraph(identifier);
  }

  @Override
  public List<IHierarchicalGraph> getHierarchicalGraphs() {

    if (!GraphDatabaseState.RUNNING.equals(this._stateMachine.getState().getId())) {
      return Collections.emptyList();
    }

    return _stateMachineContext.getHierarchicalGraphs();
  }

  @Override
  public void parse(boolean startDatabase) throws IOException {

    //
    Message<GraphDatabaseTrigger> triggerMessage = MessageBuilder.withPayload(GraphDatabaseTrigger.PARSE)
        .setHeader(START_DATABASE_AFTER_PARSING, startDatabase).build();

    trigger(triggerMessage);
  }

  @Override
  public void start() {
    trigger(GraphDatabaseTrigger.START);
  }

  @Override
  public void stop() {
    trigger(GraphDatabaseTrigger.STOP);
  }

  @Override
  public boolean isRunning() {
    return GraphDatabaseState.RUNNING.equals(this._stateMachine.getState().getId());
  }

  @Override
  public void terminate() {
    if (!GraphDatabaseState.TERMINATED.equals(this._stateMachine.getState().getId())) {
      trigger(GraphDatabaseTrigger.TERMINATE);
    }
  }

  @Override
  public GraphDatabaseState getState() {
    return _stateMachine.getState().getId();
  }

  @Override
  public List<GraphDatabaseAction> getAvailableActions() {
    return _stateMachine.getTransitions().stream()
        .filter(transition -> transition.getSource().equals(_stateMachine.getState()))
        .map(transition -> transition.getTrigger()).filter(trigger -> trigger != null)
        .map(trigger -> trigger.getEvent().getAction()).filter(action -> action != null).collect(Collectors.toList());
  }

  public IGraphDatabaseStateMachineContext stateMachineContext() {
    return _stateMachineContext;
  }

  private void trigger(Message<GraphDatabaseTrigger> triggerMessage) {

    if (!this._stateMachine.sendEvent(triggerMessage)) {
      throw new IllegalStateException(String.format("Trigger '%s' not accepted in state '%s'.",
          triggerMessage.getPayload(), this._stateMachine.getState().getId().name()));
    }
    _stateMachineContext.storeConfiguration();
  }

  private void trigger(GraphDatabaseTrigger trigger) {

    if (!this._stateMachine.sendEvent(trigger)) {
      throw new IllegalStateException(String.format("Trigger '%s' not accepted in state '%s'.", trigger,
          this._stateMachine.getState().getId().name()));
    }
    _stateMachineContext.storeConfiguration();
  }
}
