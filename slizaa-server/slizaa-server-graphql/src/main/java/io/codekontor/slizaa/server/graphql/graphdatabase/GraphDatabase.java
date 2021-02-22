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
package io.codekontor.slizaa.server.gql.graphdatabase;

import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;

public class GraphDatabase {

  private String                            _identifier;

  private String                            _state;

  private String[]                          _availableActions;

  private int                               _port;

  private ContentDefinition                 _contentDefinition;

  private GraphDatabase(String identifier, String state, int port, String[] availableActions, ContentDefinition contentDefinition) {
    this._identifier = identifier;
    this._state = state;
    this._port = port;
    this._contentDefinition = contentDefinition;
    this._availableActions = availableActions;
  }

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

  /**
   * @param database
   * @return
   */
  public static GraphDatabase convert(ISlizaaDatabase database) {

    // get the content definition provider
    IContentDefinitionProvider<?> contentDefinitionProvider = database.getContentDefinition();

    ContentDefinition contentDefinition = null;

    // the content definition
    if (contentDefinitionProvider != null) {

      contentDefinition = new ContentDefinition(
          new ContentDefinitionType(contentDefinitionProvider.getContentDefinitionProviderFactory().getFactoryId(),
              contentDefinitionProvider.getContentDefinitionProviderFactory().getName(),
              contentDefinitionProvider.getContentDefinitionProviderFactory().getDescription()),
          contentDefinitionProvider.toExternalRepresentation());
    }

    // return the database
    return new GraphDatabase(database.getIdentifier(), database.getState().name(), database.getPort(),
        database.getAvailableActions().stream().map(action -> action.getName()).toArray(String[]::new),
        contentDefinition);
  }
}
