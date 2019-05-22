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

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

public class SlizaaGraphQLError implements GraphQLError {
  
  private static final long serialVersionUID = 1L;

  private String _message;

  private List<SourceLocation> _locations;
  
  private List<Object> _path;
  
  public SlizaaGraphQLError(String message, List<SourceLocation> locations, List<Object> path) {
    _message = message;
    _locations = locations;
    _path = path;
  }

  @Override
  public List<SourceLocation> getLocations() {
    return _locations;
  }

  @Override
  public ErrorType getErrorType() {
    return ErrorType.ValidationError;
  }

  @Override
  public String getMessage() {
    return _message;
  }

  @Override
  public List<Object> getPath() {
    return _path;
  }

  @Override
  public Map<String, Object> toSpecification() {
    return GraphQLError.super.toSpecification();
  }

  @Override
  public Map<String, Object> getExtensions() {
    return GraphQLError.super.getExtensions();
  }  
}
