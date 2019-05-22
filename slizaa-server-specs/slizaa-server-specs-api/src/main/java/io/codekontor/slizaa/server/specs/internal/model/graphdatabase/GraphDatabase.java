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
package io.codekontor.slizaa.server.specs.internal.model.graphdatabase;

import java.util.Arrays;
import java.util.List;

public class GraphDatabase {

  private String                  _identifier;

  private String                  _state;

  private String[]                _availableActions;

  private int                     _port;

  private ContentDefinition       _contentDefinition;

  private List<HierarchicalGraph> _hierarchicalGraphs;

  public String getIdentifier() {
    return _identifier;
  }

  public String getState() {
    return _state;
  }

  public int getPort() {
    return _port;
  }

  public ContentDefinition getContentDefinition() {
    return _contentDefinition;
  }

  public String[] availableActions() {
    return _availableActions;
  }

  void setIdentifier(String identifier) {
    _identifier = identifier;
  }

  void setState(String state) {
    _state = state;
  }

  void setAvailableActions(String[] availableActions) {
    _availableActions = availableActions;
  }

  void setPort(int port) {
    _port = port;
  }

  void setContentDefinition(ContentDefinition contentDefinition) {
    _contentDefinition = contentDefinition;
  }

 public List<HierarchicalGraph> getHierarchicalGraphs() {
    return _hierarchicalGraphs;
  }

  void setHierarchicalGraphs(List<HierarchicalGraph> hierarchicalGraphs) {
    _hierarchicalGraphs = hierarchicalGraphs;
  }

  public String[] getAvailableActions() {
    return _availableActions;
  }

  @Override
  public String toString() {
    return "GraphDatabase [_identifier=" + _identifier + ", _port=" + _port + ", _state=" + _state
        + ", _availableActions=" + Arrays.toString(_availableActions) + ", _contentDefinition=" + _contentDefinition
        + ", _hierarchicalGraphs=" + _hierarchicalGraphs + "]";
  }
}
