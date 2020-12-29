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
    // @formatter:off
        .withStates().initial(SlizaaDatabaseState.INITIAL).choice(SlizaaDatabaseState.PARSING)
        .states(EnumSet.allOf(SlizaaDatabaseState.class));
    // @formatter:on
  }

  @Override
  public void configure(StateMachineTransitionConfigurer<SlizaaDatabaseState, SlizaaDatabaseTrigger> transitions)
      throws Exception {

    transitions
    // @formatter:off
      // INITIAL
      .withExternal()
        .source(SlizaaDatabaseState.INITIAL)
        .target(SlizaaDatabaseState.NOT_RUNNING)
        .guard(guardWithCtx(ctx -> ctx.hasContentDefinitionProvider() && ctx.hasPopulatedDatabaseDirectory()))
        .and()
      .withExternal()
          .source(SlizaaDatabaseState.INITIAL)
          .target(SlizaaDatabaseState.CONFIGURED)
          .guard(guardWithCtx(ctx -> ctx.hasContentDefinitionProvider() && !ctx.hasPopulatedDatabaseDirectory()))
          .and()
      .withExternal()
        .source(SlizaaDatabaseState.INITIAL)
        .target(SlizaaDatabaseState.CONFIGURED)
        .event(SlizaaDatabaseTrigger.SET_CONTENT_DEFINITION)
        .action(actionWithCtx((stateCtx, ctx) -> {
          setContentDefinition(stateCtx, ctx);
        }))
        .and()
      // CONFIGURED  
      .withExternal()
        .source(SlizaaDatabaseState.CONFIGURED)
        .target(SlizaaDatabaseState.PARSING)
        .event(SlizaaDatabaseTrigger.PARSE)
        .action(actionWithCtx((stateCtx, ctx) -> ctx.parse(stateCtx.getMessageHeaders().get(SlizaaDatabaseImpl.START_DATABASE_AFTER_PARSING, Boolean.class))))
        .and()
      .withExternal()
        .source(SlizaaDatabaseState.CONFIGURED)
        .target(SlizaaDatabaseState.CONFIGURED)
        .event(SlizaaDatabaseTrigger.SET_CONTENT_DEFINITION)
        .action(actionWithCtx((stateCtx, ctx) -> {
          setContentDefinition(stateCtx, ctx);
        }))
        .and()
      // PARSING
      .withChoice()
        .source(SlizaaDatabaseState.PARSING)
        .first(SlizaaDatabaseState.RUNNING, guardWithCtx(ctx -> ctx.isRunning()))
        .last(SlizaaDatabaseState.NOT_RUNNING)
        .and()
      // NOT_RUNNING
      .withExternal()
        .source(SlizaaDatabaseState.NOT_RUNNING)
        .target(SlizaaDatabaseState.CONFIGURED)
        .event(SlizaaDatabaseTrigger.SET_CONTENT_DEFINITION)
        .action(actionWithCtx((stateCtx, ctx) -> {
          setContentDefinition(stateCtx, ctx);
        }))
        .and()
      .withExternal()
        .source(SlizaaDatabaseState.NOT_RUNNING)
        .target(SlizaaDatabaseState.PARSING)
        .event(SlizaaDatabaseTrigger.PARSE)
        .action(actionWithCtx((stateCtx, ctx) -> ctx.parse(stateCtx.getMessageHeaders().get(SlizaaDatabaseImpl.START_DATABASE_AFTER_PARSING, Boolean.class))))
        .and()
      .withExternal()
        .source(SlizaaDatabaseState.NOT_RUNNING)
        .target(SlizaaDatabaseState.RUNNING)
        .event(SlizaaDatabaseTrigger.START)
        .action(actionWithCtx(ctx -> ctx.start()))
        .and()
      // RUNNING  
      .withExternal()
        .source(SlizaaDatabaseState.RUNNING)
        .target(SlizaaDatabaseState.NOT_RUNNING)
        .event(SlizaaDatabaseTrigger.STOP)
        .action(actionWithCtx(ctx -> ctx.stop()))
        .and()
      // TERMINATE
      .withExternal()
        .source(SlizaaDatabaseState.INITIAL)
        .target(SlizaaDatabaseState.TERMINATED)
        .event(SlizaaDatabaseTrigger.TERMINATE)
        .action(actionWithCtx(ctx -> ctx.terminate()))
        .and()
      .withExternal()
        .source(SlizaaDatabaseState.CONFIGURED)
        .target(SlizaaDatabaseState.TERMINATED)
        .event(SlizaaDatabaseTrigger.TERMINATE)
        .action(actionWithCtx(ctx -> ctx.terminate()))
        .and()
      .withExternal()
        .source(SlizaaDatabaseState.RUNNING)
        .target(SlizaaDatabaseState.TERMINATED)
        .event(SlizaaDatabaseTrigger.TERMINATE)
        .action(actionWithCtx(ctx -> ctx.terminate()))
        .and()
      .withExternal()
        .source(SlizaaDatabaseState.NOT_RUNNING)
        .target(SlizaaDatabaseState.TERMINATED)
        .event(SlizaaDatabaseTrigger.TERMINATE)
        .action(actionWithCtx(ctx -> ctx.terminate()));
      // @formatter:on
  }

  private void setContentDefinition(StateContext<SlizaaDatabaseState, SlizaaDatabaseTrigger> stateCtx,
                                    SlizaaDatabaseStateMachineContext ctx) {

    try {
      ctx.setContentDefinition(stateCtx.getMessageHeaders().get(SlizaaDatabaseImpl.CONTENT_DEFINITION_PROVIDER, IContentDefinitionProvider.class));
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  private Action<SlizaaDatabaseState, SlizaaDatabaseTrigger> actionWithCtx(
      Consumer<SlizaaDatabaseStateMachineContext> consumer) {

    return new Action<SlizaaDatabaseState, SlizaaDatabaseTrigger>() {
      @Override
      public void execute(StateContext<SlizaaDatabaseState, SlizaaDatabaseTrigger> context) {
        try {
          consumer.accept(_slizaaDatabaseFactory.context(context.getStateMachine()));
        } catch (Exception exception) {
          exception.printStackTrace();
        }
      }
    };
  }

  private Action<SlizaaDatabaseState, SlizaaDatabaseTrigger> actionWithCtx(
      BiConsumer<StateContext<SlizaaDatabaseState, SlizaaDatabaseTrigger>, SlizaaDatabaseStateMachineContext> consumer) {

    return new Action<SlizaaDatabaseState, SlizaaDatabaseTrigger>() {
      @Override
      public void execute(StateContext<SlizaaDatabaseState, SlizaaDatabaseTrigger> context) {
        try {
          consumer.accept(context, _slizaaDatabaseFactory.context(context.getStateMachine()));
        } catch (Exception exception) {
          exception.printStackTrace();
        }
      }
    };
  }

  private Guard<SlizaaDatabaseState, SlizaaDatabaseTrigger> guardWithCtx(
      Function<SlizaaDatabaseStateMachineContext, Boolean> guard) {

    return new Guard<SlizaaDatabaseState, SlizaaDatabaseTrigger>() {
      @Override
      public boolean evaluate(StateContext<SlizaaDatabaseState, SlizaaDatabaseTrigger> context) {
        return guard.apply(_slizaaDatabaseFactory.context(context.getStateMachine()));
      }
    };

  }
}
