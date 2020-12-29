/**
 * slizaa-server-service-slizaa - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.slizaa.internal.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
public class GraphDatabaseContentDefinitionConfiguration {

  @JsonProperty("factoryId")
  private String _factoryId;

  @JsonProperty("contentDefinition")
  private String _contentDefinition;

  public GraphDatabaseContentDefinitionConfiguration() {
  }

  public GraphDatabaseContentDefinitionConfiguration(String factoryId, String contentDefinition) {
    _factoryId = checkNotNull(factoryId);
    _contentDefinition = checkNotNull(contentDefinition);
  }

  public String getFactoryId() {
    return _factoryId;
  }

  public String getContentDefinition() {
    return _contentDefinition;
  }
}
