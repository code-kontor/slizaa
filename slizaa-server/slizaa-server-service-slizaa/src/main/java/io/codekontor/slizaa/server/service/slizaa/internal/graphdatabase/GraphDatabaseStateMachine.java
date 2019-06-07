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

import java.util.EnumSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

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

import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import io.codekontor.slizaa.server.service.slizaa.GraphDatabaseState;

@EnableStateMachineFactory
public class GraphDatabaseStateMachine
    extends EnumStateMachineConfigurerAdapter<GraphDatabaseState, GraphDatabaseTrigger> {

  @Autowired
  private GraphDatabaseFactory _graphDatabaseFactory;

  @Override
  public void configure(StateMachineConfigurationConfigurer<GraphDatabaseState, GraphDatabaseTrigger> config)
      throws Exception {

    config.withConfiguration().taskExecutor(new SyncTaskExecutor()).autoStartup(false);
  }

  @Override
  public void configure(StateMachineStateConfigurer<GraphDatabaseState, GraphDatabaseTrigger> states) throws Exception {

    states
    // @formatter:off
        .withStates().initial(GraphDatabaseState.INITIAL).choice(GraphDatabaseState.PARSING)
        .states(EnumSet.allOf(GraphDatabaseState.class));
    // @formatter:on
  }

  @Override
  public void configure(StateMachineTransitionConfigurer<GraphDatabaseState, GraphDatabaseTrigger> transitions)
      throws Exception {

    transitions
    // @formatter:off
      // INITIAL
      .withExternal()
        .source(GraphDatabaseState.INITIAL)
        .target(GraphDatabaseState.NOT_RUNNING)
        // TODO: CONTENT DEFINITION SET?
        .guard(guardWithCtx(ctx -> ctx.hasPopulatedDatabaseDirectory()))
        .and()
      .withExternal()
        .source(GraphDatabaseState.INITIAL)
        .target(GraphDatabaseState.CONFIGURED)
        .event(GraphDatabaseTrigger.SET_CONTENT_DEFINITION)
        .action(actionWithCtx((stateCtx, ctx) -> {
          setContentDefinition(stateCtx, ctx);
        }))
        .and()
      // CONFIGURED  
      .withExternal()
        .source(GraphDatabaseState.CONFIGURED)
        .target(GraphDatabaseState.PARSING)
        .event(GraphDatabaseTrigger.PARSE)
        .action(actionWithCtx((stateCtx, ctx) -> ctx.parse(stateCtx.getMessageHeaders().get(GraphDatabaseImpl.START_DATABASE_AFTER_PARSING, Boolean.class))))
        .and()
      .withExternal()
        .source(GraphDatabaseState.CONFIGURED)
        .target(GraphDatabaseState.CONFIGURED)
        .event(GraphDatabaseTrigger.SET_CONTENT_DEFINITION)
        .action(actionWithCtx((stateCtx, ctx) -> {
          setContentDefinition(stateCtx, ctx);
        }))
        .and()
      // PARSING
      .withChoice()
        .source(GraphDatabaseState.PARSING)
        .first(GraphDatabaseState.RUNNING, guardWithCtx(ctx -> ctx.isRunning()))
        .last(GraphDatabaseState.NOT_RUNNING)
        .and()
      // NOT_RUNNING
      .withExternal()
        .source(GraphDatabaseState.NOT_RUNNING)
        .target(GraphDatabaseState.CONFIGURED)
        .event(GraphDatabaseTrigger.SET_CONTENT_DEFINITION)
        .action(actionWithCtx((stateCtx, ctx) -> {
          setContentDefinition(stateCtx, ctx);
        }))
        .and()
      .withExternal()
        .source(GraphDatabaseState.NOT_RUNNING)
        .target(GraphDatabaseState.PARSING)
        .event(GraphDatabaseTrigger.PARSE)
        .action(actionWithCtx((stateCtx, ctx) -> ctx.parse(stateCtx.getMessageHeaders().get(GraphDatabaseImpl.START_DATABASE_AFTER_PARSING, Boolean.class))))
        .and()
      .withExternal()
        .source(GraphDatabaseState.NOT_RUNNING)
        .target(GraphDatabaseState.RUNNING)
        .event(GraphDatabaseTrigger.START)
        .action(actionWithCtx(ctx -> ctx.start()))
        .and()
      // RUNNING  
      .withExternal()
        .source(GraphDatabaseState.RUNNING)
        .target(GraphDatabaseState.NOT_RUNNING)
        .event(GraphDatabaseTrigger.STOP)
        .action(actionWithCtx(ctx -> ctx.stop()))
        .and()
      // TERMINATE
      .withExternal()
        .source(GraphDatabaseState.INITIAL)
        .target(GraphDatabaseState.TERMINATED)
        .event(GraphDatabaseTrigger.TERMINATE)
        .action(actionWithCtx(ctx -> ctx.terminate()))
        .and()
      .withExternal()
        .source(GraphDatabaseState.CONFIGURED)
        .target(GraphDatabaseState.TERMINATED)
        .event(GraphDatabaseTrigger.TERMINATE)
        .action(actionWithCtx(ctx -> ctx.terminate()))
        .and()
      .withExternal()
        .source(GraphDatabaseState.RUNNING)
        .target(GraphDatabaseState.TERMINATED)
        .event(GraphDatabaseTrigger.TERMINATE)
        .action(actionWithCtx(ctx -> ctx.terminate()))
        .and()
      .withExternal()
        .source(GraphDatabaseState.NOT_RUNNING)
        .target(GraphDatabaseState.TERMINATED)
        .event(GraphDatabaseTrigger.TERMINATE)
        .action(actionWithCtx(ctx -> ctx.terminate()));
      // @formatter:on
  }

  private void setContentDefinition(StateContext<GraphDatabaseState, GraphDatabaseTrigger> stateCtx,
      IGraphDatabaseStateMachineContext ctx) {
        
    IContentDefinitionProvider<?> contentDefinitionProvider = stateCtx.getMessageHeaders().get(GraphDatabaseImpl.CONTENT_DEFINITION_PROVIDER, IContentDefinitionProvider.class);
    ctx.setContentDefinition(contentDefinitionProvider);
  }

  private Action<GraphDatabaseState, GraphDatabaseTrigger> actionWithCtx(
      Consumer<IGraphDatabaseStateMachineContext> consumer) {

    return new Action<GraphDatabaseState, GraphDatabaseTrigger>() {
      @Override
      public void execute(StateContext<GraphDatabaseState, GraphDatabaseTrigger> context) {
        consumer.accept(_graphDatabaseFactory.context(context.getStateMachine()));
      }
    };
  }

  private Action<GraphDatabaseState, GraphDatabaseTrigger> actionWithCtx(
      BiConsumer<StateContext<GraphDatabaseState, GraphDatabaseTrigger>, IGraphDatabaseStateMachineContext> consumer) {

    return new Action<GraphDatabaseState, GraphDatabaseTrigger>() {
      @Override
      public void execute(StateContext<GraphDatabaseState, GraphDatabaseTrigger> context) {
        consumer.accept(context, _graphDatabaseFactory.context(context.getStateMachine()));
      }
    };
  }

  private Guard<GraphDatabaseState, GraphDatabaseTrigger> guardWithCtx(
      Function<IGraphDatabaseStateMachineContext, Boolean> guard) {

    return new Guard<GraphDatabaseState, GraphDatabaseTrigger>() {
      @Override
      public boolean evaluate(StateContext<GraphDatabaseState, GraphDatabaseTrigger> context) {
        return guard.apply(_graphDatabaseFactory.context(context.getStateMachine()));
      }
    };

  }
}
