/**
 * slizaa-server-graphql - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.graphql;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import graphql.ErrorClassification;
import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import graphql.servlet.GraphQLErrorHandler;

@Configuration
@ComponentScan(basePackageClasses = { GraphqlConfiguration.class })
public class GraphqlConfiguration {

//  @Bean
//  public ExecutionStrategy executionStrategy() {
//    return new AsyncExecutionStrategy(new DataFetcherExceptionHandler() {
//
//      @Override
//      public void onException(DataFetcherExceptionHandlerParameters handlerParameters) {
//        Throwable exception = handlerParameters.getException();
//        SourceLocation sourceLocation = handlerParameters.getField().getSourceLocation();
//        ExecutionPath path = handlerParameters.getPath();
//
//        ExceptionWhileDataFetching error = new ExceptionWhileDataFetching(path, exception, sourceLocation);
//        handlerParameters.getExecutionContext().addError(error);
//      }
//    });
//  }

  @Bean
  public GraphQLErrorHandler errorHandler() {
    return new GraphQLErrorHandler() {

      @Override
      public List<GraphQLError> processErrors(List<GraphQLError> errors) {
        return errors.stream().map(e -> new GraphQLErrorAdapter(e))
            .collect(Collectors.toList());
      }
    };
  }

  private class GraphQLErrorAdapter implements GraphQLError {

    private static final long serialVersionUID = 1308954323465652533L;

    private GraphQLError      error;

    public GraphQLErrorAdapter(GraphQLError error) {
      this.error = error;
    }

    @Override
    public Map<String, Object> getExtensions() {
      return error.getExtensions();
    }

    @Override
    public List<SourceLocation> getLocations() {
      return error.getLocations();
    }

    @Override
    public ErrorClassification getErrorType() {
      return error.getErrorType();
    }

    @Override
    public List<Object> getPath() {
      return error.getPath();
    }

    @Override
    public Map<String, Object> toSpecification() {
      Map<String, Object> result = error.toSpecification();
      result.remove("exception");
      return result;
    }

    @Override
    public String getMessage() {
      if (error instanceof ExceptionWhileDataFetching) {
        return ((ExceptionWhileDataFetching) error).getException().getMessage();
      }
      return error.getMessage();
    }
  }
}
