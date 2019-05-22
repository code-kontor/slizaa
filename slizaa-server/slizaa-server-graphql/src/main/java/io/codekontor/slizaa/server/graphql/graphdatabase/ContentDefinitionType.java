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
package io.codekontor.slizaa.server.graphql.graphdatabase;

public class ContentDefinitionType {

  private String _identifier;

  private String _name;

  private String _description;

  public ContentDefinitionType(String identifier, String name, String description) {
    _identifier = identifier;
    _name = name;
    _description = description;
  }

  public String getIdentifier() {
    return _identifier;
  }

  public String getName() {
    return _name;
  }

  public String getDescription() {
    return _description;
  }
}
