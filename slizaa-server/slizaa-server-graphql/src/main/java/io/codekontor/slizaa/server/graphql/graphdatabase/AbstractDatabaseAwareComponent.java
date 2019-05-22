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

import io.codekontor.slizaa.server.graphql.SlizaaGraphQLError;
import io.codekontor.slizaa.server.service.slizaa.IGraphDatabase;
import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import org.springframework.beans.factory.annotation.Autowired;

import graphql.schema.DataFetchingEnvironment;

public abstract class AbstractDatabaseAwareComponent {

  //
  @Autowired
  private ISlizaaService _slizaaService;

  /**
   * 
   * @return
   */
  protected ISlizaaService slizaaService() {
    return _slizaaService;
  }
  
  /**
   * 
   * @param databaseId
   * @param consumer
   * @return
   */
  protected GraphDatabase executeOnDatabase(DataFetchingEnvironment environment, String databaseId, DatabaseConsumer consumer) {
  
    // get the database
    IGraphDatabase database = _slizaaService.getGraphDatabase(databaseId);

    // check exists
    if (database == null) {
      // TODO:
    }

    //
    try {
      consumer.accept(database);
    } catch (Exception e) {
      e.printStackTrace();
      // TODO: ERROR HANDLING
      // environment.addError(new SlizaaGraphQLError(e.getMessage(), null, null));
    }

    // return the result
    return GraphDatabase.convert(database);
  }

  @FunctionalInterface
  public interface DatabaseConsumer {

    void accept(IGraphDatabase database) throws Exception;
  }
}
