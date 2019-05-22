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

public class ContentDefinitionType {

  private String _identifier;

  private String _name;

  private String _description;

  public String getIdentifier() {
    return _identifier;
  }

  public String getName() {
    return _name;
  }

  public String getDescription() {
    return _description;
  }

  void setIdentifier(String identifier) {
    _identifier = identifier;
  }

  void setName(String name) {
    _name = name;
  }

  void setDescription(String description) {
    _description = description;
  }

  @Override
  public String toString() {
    return "ContentDefinitionType [_identifier=" + _identifier + ", _name=" + _name + ", _description=" + _description
        + "]";
  }
}
