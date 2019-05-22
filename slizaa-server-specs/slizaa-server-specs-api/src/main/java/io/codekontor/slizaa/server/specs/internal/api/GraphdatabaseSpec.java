/**
 * slizaa-server-specs-api - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.specs.internal.api;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import io.codekontor.slizaa.server.specs.api.IContentDefinitionSpecProvider;
import io.codekontor.slizaa.server.specs.api.IGraphdatabaseSpec;
import io.codekontor.slizaa.server.specs.api.IHierarchicalGraphSpec;

public class GraphdatabaseSpec implements IGraphdatabaseSpec {

  private String                      _identifier;

  private List<ContentDefinitionSpec> _contentDefinitionSpecs;

  private List<HierarchicalGraphSpec> _hierarchicalGraphSpecs;

  private boolean                     _runnning;

  private boolean                     _forceRebuild;

  public GraphdatabaseSpec(String identifier) {
    _identifier = identifier;
    _runnning = true;
    _contentDefinitionSpecs = new ArrayList<>();
    _hierarchicalGraphSpecs = new ArrayList<>();
  }

  public IGraphdatabaseSpec contentDefinition(String type, String definition) {
    _contentDefinitionSpecs.add(new ContentDefinitionSpec(type, definition));
    return this;
  }

  @Override
  public IGraphdatabaseSpec contentDefinition(IContentDefinitionSpecProvider contentDefinitionSpecProvider) {
    _contentDefinitionSpecs.add(new ContentDefinitionSpec(checkNotNull(contentDefinitionSpecProvider).getType(),
        checkNotNull(contentDefinitionSpecProvider).getContentDefinition()));
    return this;
  }

  public IGraphdatabaseSpec running(boolean runnning) {
    _runnning = runnning;
    return this;
  }

  public IGraphdatabaseSpec forceRebuild(boolean forceRebuild) {
    _forceRebuild = forceRebuild;
    return this;
  }

  /**
   * 
   */
  public IGraphdatabaseSpec hierarchicalGraphs(IHierarchicalGraphSpec... hierarchicalGraphConfiguration) {

    //
    for (IHierarchicalGraphSpec spec : hierarchicalGraphConfiguration) {
      _hierarchicalGraphSpecs.add((HierarchicalGraphSpec) spec);
    }

    return this;
  }

  /**
   * 
   * @return
   */
  public String getIdentifier() {
    return _identifier;
  }

  /**
   * 
   * @return
   */
  public List<ContentDefinitionSpec> getContentDefinitionSpecs() {
    return _contentDefinitionSpecs;
  }

  public List<HierarchicalGraphSpec> getHierarchicalGraphSpecs() {
    return _hierarchicalGraphSpecs;
  }

  public boolean isRunnning() {
    return _runnning;
  }

  public boolean isForceRebuild() {
    return _forceRebuild;
  }

  @Override
  public String toString() {
    return "GraphdatabaseSpec [_identifier=" + _identifier + ", _runnning=" + _runnning + ", _contentDefinitionSpecs="
        + _contentDefinitionSpecs + ", _hierarchicalGraphSpecs=" + _hierarchicalGraphSpecs + "]";
  }
}
