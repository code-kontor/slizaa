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
import io.codekontor.slizaa.server.slizaadb.SlizaaDatabaseState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

import java.util.EnumSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@EnableStateMachineFactory
public class SlizaaDatabaseStateMachine
        extends EnumStateMachineConfigurerAdapter<SlizaaDatabaseState, SlizaaDatabaseTrigger> {

    public static final String TRIGGER_PARAM = "TRIGGER_PARAM";

    @Autowired
    private SlizaaDatabaseFactory _slizaaDatabaseFactory;

    @Override
    public void configure(StateMachineConfigurationConfigurer<SlizaaDatabaseState, SlizaaDatabaseTrigger> config)
            throws Exception {

        config.withConfiguration().taskExecutor(new SyncTaskExecutor()).autoStartup(false);
    }

    @Override
    public void configure(StateMachineStateConfigurer<SlizaaDatabaseState, SlizaaDatabaseTrigger> states) throws Exception {

        states
                .withStates()
                .initial(SlizaaDatabaseState.INITIAL)
                .states(EnumSet.allOf(SlizaaDatabaseState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<SlizaaDatabaseState, SlizaaDatabaseTrigger> transitions)
            throws Exception {

        transitions

                // INITIAL --> NOT_RUNNING
                .withExternal()
                .source(SlizaaDatabaseState.INITIAL)
                .target(SlizaaDatabaseState.NOT_RUNNING)
                .guard(guardWithCtx(ctx -> ctx.hasContentDefinitionProvider() && ctx.hasPopulatedDatabaseDirectory()))
                .and()
                // INITIAL --> CONFIGURED
                .withExternal()
                .source(SlizaaDatabaseState.INITIAL)
                .target(SlizaaDatabaseState.CONFIGURED)
                .guard(guardWithCtx(ctx -> ctx.hasContentDefinitionProvider() && !ctx.hasPopulatedDatabaseDirectory()))
                .and()
                // INITIAL --> CONFIGURED
                .withExternal()
                .source(SlizaaDatabaseState.INITIAL)
                .target(SlizaaDatabaseState.CONFIGURED)
                .event(SlizaaDatabaseTrigger.SET_CONTENT_DEFINITION)
                .action(setContentDefinition())
                .and()

                // CONFIGURED --> PARSING
                .withExternal()
                .source(SlizaaDatabaseState.CONFIGURED)
                .target(SlizaaDatabaseState.PARSING)
                .event(SlizaaDatabaseTrigger.PARSE)
                .action(parse())
                .and()
                // CONFIGURED --> CONFIGURED
                .withExternal()
                .source(SlizaaDatabaseState.CONFIGURED)
                .target(SlizaaDatabaseState.CONFIGURED)
                .event(SlizaaDatabaseTrigger.SET_CONTENT_DEFINITION)
                .action(setContentDefinition())
                .and()

                // PARSING --> RUNNING
                .withExternal()
                .source(SlizaaDatabaseState.PARSING)
                .target(SlizaaDatabaseState.RUNNING)
                .event(SlizaaDatabaseTrigger.PARSE_WITH_START_SUCCEEDED)
                .and()
                // PARSING --> NOT_RUNNING
                .withExternal()
                .source(SlizaaDatabaseState.PARSING)
                .target(SlizaaDatabaseState.NOT_RUNNING)
                .event(SlizaaDatabaseTrigger.PARSE_WITHOUT_START_SUCCEEDED)
                .and()
                // PARSING --> CONFIGURED
                .withExternal()
                .source(SlizaaDatabaseState.PARSING)
                .target(SlizaaDatabaseState.CONFIGURED)
                .event(SlizaaDatabaseTrigger.PARSE_FAILED)
                .and()

                // NOT_RUNNING --> CONFIGURED
                .withExternal()
                .source(SlizaaDatabaseState.NOT_RUNNING)
                .target(SlizaaDatabaseState.CONFIGURED)
                .event(SlizaaDatabaseTrigger.SET_CONTENT_DEFINITION)
                .action(setContentDefinition())
                .and()
                // NOT_RUNNING --> PARSING
                .withExternal()
                .source(SlizaaDatabaseState.NOT_RUNNING)
                .target(SlizaaDatabaseState.PARSING)
                .event(SlizaaDatabaseTrigger.PARSE)
                .action(parse())
                .and()
                // NOT_RUNNING --> STARTING
                .withExternal()
                .source(SlizaaDatabaseState.NOT_RUNNING)
                .target(SlizaaDatabaseState.STARTING)
                .event(SlizaaDatabaseTrigger.START)
                .action(actionWithCtx(ctx -> ctx.start()))
                .and()

                // STARTING --> RUNNING
                .withExternal()
                .source(SlizaaDatabaseState.STARTING)
                .target(SlizaaDatabaseState.RUNNING)
                .event(SlizaaDatabaseTrigger.START_SUCCEEDED)
                .and()
                // STARTING --> NOT_RUNNING
                .withExternal()
                .source(SlizaaDatabaseState.STARTING)
                .target(SlizaaDatabaseState.NOT_RUNNING)
                .event(SlizaaDatabaseTrigger.START_FAILED)
                .and()

                // RUNNING --> STOPPING
                .withExternal()
                .source(SlizaaDatabaseState.RUNNING)
                .target(SlizaaDatabaseState.STOPPING)
                .event(SlizaaDatabaseTrigger.STOP)
                .action(actionWithCtx(ctx -> ctx.stop()))
                .and()
                // RUNNING --> CREATING_HIERARCHICAL_GRAPH
                .withExternal()
                .source(SlizaaDatabaseState.RUNNING)
                .target(SlizaaDatabaseState.CREATING_HIERARCHICAL_GRAPH)
                .event(SlizaaDatabaseTrigger.CREATE_HIERARCHICAL_GRAPH)
                .action(createHierarchicalGraph())
                .and()

                // CREATING_HIERARCHICAL_GRAPH --> RUNNING
                .withExternal()
                .source(SlizaaDatabaseState.CREATING_HIERARCHICAL_GRAPH)
                .target(SlizaaDatabaseState.RUNNING)
                .event(SlizaaDatabaseTrigger.CREATE_HIERARCHICAL_GRAPH_SUCCEEDED)
                .and()
                // CREATING_HIERARCHICAL_GRAPH --> RUNNING
                .withExternal()
                .source(SlizaaDatabaseState.CREATING_HIERARCHICAL_GRAPH)
                .target(SlizaaDatabaseState.RUNNING)
                .event(SlizaaDatabaseTrigger.CREATE_HIERARCHICAL_GRAPH_FAILED)
                .and()

                // STOPPING --> NOT_RUNNING
                .withExternal()
                .source(SlizaaDatabaseState.STOPPING)
                .target(SlizaaDatabaseState.NOT_RUNNING)
                .event(SlizaaDatabaseTrigger.STOP_SUCCEEDED)
                .and()
                // STOPPING --> RUNNING
                .withExternal()
                .source(SlizaaDatabaseState.STOPPING)
                .target(SlizaaDatabaseState.RUNNING)
                .event(SlizaaDatabaseTrigger.STOP_FAILED)
                .and()

                // INITIAL --> TERMINATED
                .withExternal()
                .source(SlizaaDatabaseState.INITIAL)
                .target(SlizaaDatabaseState.TERMINATED)
                .event(SlizaaDatabaseTrigger.TERMINATE)
                .action(actionWithCtx(ctx -> ctx.terminate()))
                .and()

                // CONFIGURED --> TERMINATED
                .withExternal()
                .source(SlizaaDatabaseState.CONFIGURED)
                .target(SlizaaDatabaseState.TERMINATED)
                .event(SlizaaDatabaseTrigger.TERMINATE)
                .action(actionWithCtx(ctx -> ctx.terminate()))
                .and()

                // RUNNING --> TERMINATED
                .withExternal()
                .source(SlizaaDatabaseState.RUNNING)
                .target(SlizaaDatabaseState.TERMINATED)
                .event(SlizaaDatabaseTrigger.TERMINATE)
                .action(actionWithCtx(ctx -> ctx.terminate()))
                .and()

                // NOT_RUNNING --> TERMINATED
                .withExternal()
                .source(SlizaaDatabaseState.NOT_RUNNING)
                .target(SlizaaDatabaseState.TERMINATED)
                .event(SlizaaDatabaseTrigger.TERMINATE)
                .action(actionWithCtx(ctx -> ctx.terminate()));
    }

    private Action<SlizaaDatabaseState, SlizaaDatabaseTrigger> createHierarchicalGraph() {
        return actionWithCtx(
                (stateCtx, ctx) -> {
                    TriggerParameter_CreateHierarchicalGraph parameter = stateCtx.getMessageHeaders().get(TRIGGER_PARAM, TriggerParameter_CreateHierarchicalGraph.class);
                    ctx.createHierarchicalGraph(parameter.getIdentifier());
                }
        );
    }

    private Action<SlizaaDatabaseState, SlizaaDatabaseTrigger> parse() {
        return actionWithCtx(
                (stateCtx, ctx) -> {
                    TriggerParameter_Parse parameter = stateCtx.getMessageHeaders().get(TRIGGER_PARAM, TriggerParameter_Parse.class);
                    ctx.parse(parameter.startDatabaseAfterParsing());
                }
        );
    }

    private Action<SlizaaDatabaseState, SlizaaDatabaseTrigger> setContentDefinition() {
        return actionWithCtx(
                (stateCtx, ctx) -> {
                    try {
                        TriggerParameter_SetContentDefinition parameter =
                                stateCtx.getMessageHeaders().get(SlizaaDatabaseStateMachine.TRIGGER_PARAM, TriggerParameter_SetContentDefinition.class);
                        ctx.setContentDefinition(parameter.getContentDefinitionProvider());
                    } catch (Exception exception) {
                        // TODO
                        exception.printStackTrace();
                    }
                }
        );
    }

    private Action<SlizaaDatabaseState, SlizaaDatabaseTrigger> actionWithCtx(
            Consumer<SlizaaDatabaseStateMachineContext> consumer) {
        return context -> {
            try {
                consumer.accept(_slizaaDatabaseFactory.context(context.getStateMachine()));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        };
    }

    private Action<SlizaaDatabaseState, SlizaaDatabaseTrigger> actionWithCtx(
            BiConsumer<StateContext<SlizaaDatabaseState, SlizaaDatabaseTrigger>, SlizaaDatabaseStateMachineContext> consumer) {
        return context -> {
            try {
                consumer.accept(context, _slizaaDatabaseFactory.context(context.getStateMachine()));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        };
    }

    private Guard<SlizaaDatabaseState, SlizaaDatabaseTrigger> guardWithCtx(
            Function<SlizaaDatabaseStateMachineContext, Boolean> guard) {
        return context -> guard.apply(_slizaaDatabaseFactory.context(context.getStateMachine()));
    }

    public static class TriggerParameter_Parse {

        private boolean startDatabaseAfterParsing;

        public TriggerParameter_Parse(boolean startDatabaseAfterParsing) {
            this.startDatabaseAfterParsing = startDatabaseAfterParsing;
        }

        public boolean startDatabaseAfterParsing() {
            return startDatabaseAfterParsing;
        }
    }

    public static class TriggerParameter_SetContentDefinition {

        private IContentDefinitionProvider<?> contentDefinitionProvider;

        public TriggerParameter_SetContentDefinition(IContentDefinitionProvider<?> contentDefinitionProvider) {
            this.contentDefinitionProvider = contentDefinitionProvider;
        }

        public IContentDefinitionProvider<?> getContentDefinitionProvider() {
            return contentDefinitionProvider;
        }
    }

    public static class TriggerParameter_CreateHierarchicalGraph {

        private String identifier;

        public TriggerParameter_CreateHierarchicalGraph(String identifier) {
            this.identifier = identifier;
        }

        public String getIdentifier() {
            return identifier;
        }
    }
}
