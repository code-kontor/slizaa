/**
 * slizaa-server-service-provisioning - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.provisioning.model.request;

import io.codekontor.slizaa.server.service.provisioning.model.descr.GraphDatabaseDescr;
import io.codekontor.slizaa.server.service.provisioning.model.descr.ServerExtensionDescr;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class SlizaaServerRequest {

  /** - */
  private List<GraphDatabaseDescr> graphDatabases;

  public void setGraphDatabases(List<GraphDatabaseDescr> graphDatabases) {
    this.graphDatabases = checkNotNull(graphDatabases);
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public List<GraphDatabaseDescr> getGraphDatabases() {
    return graphDatabases;
  }
}
