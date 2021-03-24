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

import io.codekontor.slizaa.server.service.provisioning.model.IGraphDatabaseDTO;
import io.codekontor.slizaa.server.slizaadb.SlizaaDatabaseState;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class GraphDatabaseRequest implements IGraphDatabaseDTO {

  private String id;
  private String state = SlizaaDatabaseState.RUNNING.name();
  private ContentDefinitionRequest contentDefinition;
  private List<HierarchicalGraphRequest> hierarchicalGraphs;

  public GraphDatabaseRequest() {
    // default constructor
  }

  public GraphDatabaseRequest(String id, ContentDefinitionRequest contentDefinition, List<HierarchicalGraphRequest> hierarchicalGraphs) {
    this.id = id;
    this.contentDefinition = contentDefinition;
    this.hierarchicalGraphs = hierarchicalGraphs != null ? hierarchicalGraphs : Collections.emptyList();
  }

  public void setId(String id) {
    this.id = checkNotNull(id);
  }

  public void setContentDefinition(ContentDefinitionRequest contentDefinition) {
    this.contentDefinition = checkNotNull(contentDefinition);
  }

  public void setHierarchicalGraphs(List<HierarchicalGraphRequest> hierarchicalGraphs) {
    this.hierarchicalGraphs = hierarchicalGraphs != null ? hierarchicalGraphs : Collections.emptyList();
  }

  @Override
  public String getState() {
    return this.state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getId() {
    return id;
  }

  public ContentDefinitionRequest getContentDefinition() {
    return contentDefinition;
  }

  public List<HierarchicalGraphRequest> getHierarchicalGraphs() {
    return hierarchicalGraphs != null ? hierarchicalGraphs : Collections.emptyList();
  }

  @Override
  public int hashCode() {
    return IGraphDatabaseDTO.hashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
   return IGraphDatabaseDTO.equals(this, obj);
  }

  public void validate() {
    checkNotNull(id);

    checkNotNull(state);
    checkState(state.equals(SlizaaDatabaseState.RUNNING.name()) || state.equals(SlizaaDatabaseState.NOT_RUNNING.name()), "State must be either '%s' or '%s'.", SlizaaDatabaseState.RUNNING.name(), SlizaaDatabaseState.NOT_RUNNING.name());

    checkNotNull(contentDefinition);
    contentDefinition.validate();
    hierarchicalGraphs.forEach(hg -> hg.validate());

    checkNotNull(hierarchicalGraphs);
    Set<String> items = new HashSet<>();
    Set<HierarchicalGraphRequest> duplicateIds = hierarchicalGraphs.stream()
            .filter(hg -> !items.add(hg.getId()))
            .collect(Collectors.toSet());
    checkState(duplicateIds.isEmpty(), "Duplicate IDs:  %s.", duplicateIds);
  }
}
