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

import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase.GraphDatabaseAction;

public enum SlizaaDatabaseTrigger {

  SET_CONTENT_DEFINITION(GraphDatabaseAction.SET_CONTENT_DEFINITION),
  PARSE(GraphDatabaseAction.PARSE),
  PARSE_FAILED(null),
  PARSE_WITHOUT_START_SUCCEEDED(null),
  PARSE_WITH_START_SUCCEEDED(null),
  START(GraphDatabaseAction.START),
  START_SUCCEEDED(null),
  START_FAILED(null),
  STOP(GraphDatabaseAction.STOP),
  STOP_SUCCEEDED(null),
  STOP_FAILED(null),
  CREATE_HIERARCHICAL_GRAPH(GraphDatabaseAction.CREATE_HIERARCHICAL_GRAPH),
  CREATE_HIERARCHICAL_GRAPH_SUCCEEDED(null),
  CREATE_HIERARCHICAL_GRAPH_FAILED(null),
  TERMINATE(GraphDatabaseAction.DELETE);

  public GraphDatabaseAction getAction() {
    return _action;
  }

  SlizaaDatabaseTrigger(GraphDatabaseAction action) {
    _action = action;
  }

  private GraphDatabaseAction _action;
}
