/**
 * slizaa-hierarchicalgraph-core-testfwk - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.core.testfwk;

import static com.google.common.base.Preconditions.checkNotNull;

public enum XmiBasedGraph {

  //
  MAP_STRUCT("mapstruct_1-1-0-Beta2.hggraph"), EUREKA("eureka_1-4-10.hggraph"), EUREKA_AGGREGATED(
      "eureka_1-4-10-aggregated.hggraph");

  private XmiBasedGraph(String xmiFileName) {
    _xmiFileName = checkNotNull(xmiFileName);
  }
  
  public String getXmiFileName() {
    return _xmiFileName;
  }

  private String _xmiFileName;
}
