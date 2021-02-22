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

import io.codekontor.slizaa.server.service.provisioning.model.descr.ContentDefinitionDescr;
import io.codekontor.slizaa.server.service.provisioning.model.descr.HierarchicalGraphDescr;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class GraphDatabaseRequest {

  /** - */
  private String                       identifier;

  /** - */
  private ContentDefinitionRequest contentDefinition;

  /** - */
  private List<HierarchicalGraphDescr> hierarchicalGraphs;

  /** - */
  private String                       state;

  public void setIdentifier(String identifier) {
    this.identifier = checkNotNull(identifier);
  }

  public void setContentDefinition(ContentDefinitionRequest contentDefinition) {
    this.contentDefinition = checkNotNull(contentDefinition);
  }

  public void setHierarchicalGraphs(List<HierarchicalGraphDescr> hierarchicalGraphs) {
    this.hierarchicalGraphs = checkNotNull(hierarchicalGraphs);
  }

  public void setState(String state) {
    this.state = checkNotNull(state);
  }

  public String getIdentifier() {
    return identifier;
  }

  public ContentDefinitionRequest getContentDefinition() {
    return contentDefinition;
  }

  public List<HierarchicalGraphDescr> getHierarchicalGraphs() {
    return hierarchicalGraphs;
  }

  public String getState() {
    return state;
  }
}
