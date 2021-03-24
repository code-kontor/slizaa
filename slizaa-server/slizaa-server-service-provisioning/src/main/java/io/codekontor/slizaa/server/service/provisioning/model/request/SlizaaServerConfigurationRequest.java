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

import io.codekontor.slizaa.server.service.provisioning.model.ISlizaaServerConfigurationDTO;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class SlizaaServerConfigurationRequest implements ISlizaaServerConfigurationDTO {

  private List<GraphDatabaseRequest> graphDatabases;

  private String _configurationRequestId;

  // TODO
  public SlizaaServerConfigurationRequest() {
    _configurationRequestId = "BUMM";
  }

  public SlizaaServerConfigurationRequest(List<GraphDatabaseRequest> graphDatabases) {
    this.graphDatabases = graphDatabases != null ? graphDatabases : Collections.emptyList();
  }

  public void setGraphDatabases(List<GraphDatabaseRequest> graphDatabases) {
    this.graphDatabases = checkNotNull(graphDatabases);
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public List<GraphDatabaseRequest> getGraphDatabases() {
    return graphDatabases;
  }

  @Override
  public int hashCode() {
    return ISlizaaServerConfigurationDTO.hashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return ISlizaaServerConfigurationDTO.equals(this, obj);
  }

  public void validate() {
    checkNotNull(graphDatabases);
    graphDatabases.forEach(db -> db.validate());
  }
}
