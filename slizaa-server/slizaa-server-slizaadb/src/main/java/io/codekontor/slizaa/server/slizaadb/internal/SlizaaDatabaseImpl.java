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
import io.codekontor.slizaa.scanner.spi.contentdefinition.InvalidContentDefinitionException;
import io.codekontor.slizaa.server.slizaadb.SlizaaDatabaseState;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabaseEnvironment;
import io.codekontor.slizaa.server.slizaadb.IHierarchicalGraph;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class SlizaaDatabaseImpl implements ISlizaaDatabase {

    public static final String START_DATABASE_AFTER_PARSING = "START_DATABASE_AFTER_PARSING";

    public static final String CONTENT_DEFINITION_PROVIDER = "CONTENT_DEFINITION_PROVIDER";

    /* the state machine */
    private final StateMachine<SlizaaDatabaseState, SlizaaDatabaseTrigger> _stateMachine;

    /* the state machine context */
    private final SlizaaDatabaseStateMachineContext _stateMachineContext;

    /* the database environments */
    private final ISlizaaDatabaseEnvironment _graphDatabaseEnvironment;

    /**
     * @param stateMachine
     * @param stateMachineContext
     */
    SlizaaDatabaseImpl(StateMachine<SlizaaDatabaseState, SlizaaDatabaseTrigger> stateMachine,
                       SlizaaDatabaseStateMachineContext stateMachineContext,
                       ISlizaaDatabaseEnvironment graphDatabaseEnvironment) {

        this._stateMachine = checkNotNull(stateMachine);
        this._stateMachineContext = checkNotNull(stateMachineContext);
        this._graphDatabaseEnvironment = checkNotNull(graphDatabaseEnvironment);

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

        // create the content definition provider
        IContentDefinitionProvider<?> contentDefinitionProvider = _graphDatabaseEnvironment.createContentDefinitionProvider(contentDefinitionFactoryId, contentDefinition);
        if (contentDefinitionProvider == null) {
            throw new InvalidContentDefinitionException(String.format("Invalid content definition ('%s', '%s').", contentDefinitionFactoryId, contentDefinition));
        }

        //
        trigger(MessageBuilder
                    .withPayload(SlizaaDatabaseTrigger.SET_CONTENT_DEFINITION)
                    .setHeader(CONTENT_DEFINITION_PROVIDER, contentDefinitionProvider)
                    .build());
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
        IHierarchicalGraph result = _stateMachineContext.createHierarchicalGraph(identifier);
        _stateMachineContext.storeConfiguration();
        return result;
    }

    @Override
    public void removeHierarchicalGraph(String identifier) {
        _stateMachineContext.disposeHierarchicalGraph(identifier);
    }

    @Override
    public IHierarchicalGraph getHierarchicalGraph(String identifier) {
        return _stateMachineContext.getHierarchicalGraph(identifier);
    }

    @Override
    public List<IHierarchicalGraph> getHierarchicalGraphs() {
        return _stateMachineContext.getHierarchicalGraphs();
    }

    @Override
    public void parse(boolean startDatabase) throws IOException {
        trigger(MessageBuilder
                    .withPayload(SlizaaDatabaseTrigger.PARSE)
                    .setHeader(START_DATABASE_AFTER_PARSING, startDatabase).build());
    }

    @Override
    public void start() {
        // ugly work-around: the statemachine may has not triggered all lambda expressions yet
        try {
            trigger(SlizaaDatabaseTrigger.START);
        } catch (IllegalStateException e) {
            trigger(SlizaaDatabaseTrigger.START);
        }
    }

    @Override
    public void stop() {
        trigger(SlizaaDatabaseTrigger.STOP);
    }

    @Override
    public boolean isRunning() {
        return SlizaaDatabaseState.RUNNING.equals(this._stateMachine.getState().getId());
    }

    @Override
    public void terminate() {
        trigger(SlizaaDatabaseTrigger.TERMINATE);
    }

    @Override
    public SlizaaDatabaseState getState() {
        return _stateMachine.getState().getId();
    }

    @Override
    public List<GraphDatabaseAction> getAvailableActions() {
        return _stateMachine.getTransitions().stream()
                .filter(transition -> transition.getSource().equals(_stateMachine.getState()))
                .map(transition -> transition.getTrigger()).filter(trigger -> trigger != null)
                .map(trigger -> trigger.getEvent().getAction()).filter(action -> action != null).collect(Collectors.toList());
    }

    SlizaaDatabaseStateMachineContext stateMachineContext() {
        return _stateMachineContext;
    }

    StateMachine<SlizaaDatabaseState, SlizaaDatabaseTrigger> stateMachine() {
        return _stateMachine;
    }

    private void trigger(Message<SlizaaDatabaseTrigger> triggerMessage) {

        if (!this._stateMachine.sendEvent(triggerMessage)) {
            throw new IllegalStateException(String.format("Trigger '%s' not accepted in state '%s'.",
                    triggerMessage.getPayload(), this._stateMachine.getState().getId().name()));
        }
        _stateMachineContext.storeConfiguration();
    }

    private void trigger(SlizaaDatabaseTrigger trigger) {
        if (!this._stateMachine.sendEvent(trigger)) {
            throw new IllegalStateException(String.format("Trigger '%s' not accepted in state '%s'.", trigger,
                    this._stateMachine.getState().getId().name()));
        }
        _stateMachineContext.storeConfiguration();
    }
}
