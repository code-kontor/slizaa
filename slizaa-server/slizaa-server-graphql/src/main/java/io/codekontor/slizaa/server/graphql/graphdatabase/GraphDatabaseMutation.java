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

import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

import graphql.schema.DataFetchingEnvironment;

@Component
public class GraphDatabaseMutation extends AbstractDatabaseAwareComponent implements GraphQLMutationResolver {

  public GraphDatabase createGraphDatabase(String identifier, DataFetchingEnvironment environment) {
    
    return GraphDatabase.convert(slizaaService().newGraphDatabase(identifier));
  }

  public GraphDatabase startGraphDatabase(String databaseId, DataFetchingEnvironment environment) {

    return executeOnDatabase(environment, databaseId, database -> {
      database.start();
    });
  }

  public GraphDatabase stopGraphDatabase(String databaseId, DataFetchingEnvironment environment) {

    return executeOnDatabase(environment, databaseId, database -> {
      database.stop();
    });
  }

  public GraphDatabase deleteGraphDatabase(String databaseId, DataFetchingEnvironment environment) {

    return executeOnDatabase(environment, databaseId, database -> {
      database.terminate();
    });
  }
  
  public GraphDatabase parseGraphDatabase(String databaseId, DataFetchingEnvironment environment) {

    return executeOnDatabase(environment, databaseId, database -> {
      database.parse(true);
    });
  }

  public GraphDatabase setGraphDatabaseContentDefinition(String databaseId, String contentDefinitionFactoryId,
      String contentDefinition, DataFetchingEnvironment environment) {

    return executeOnDatabase(environment, databaseId, database -> {
      database.setContentDefinitionProvider(contentDefinitionFactoryId, contentDefinition);
    });
  }

  public GraphDatabase createHierarchicalGraph(String databaseId, String hierarchicalGraphId, DataFetchingEnvironment environment) {

    return executeOnDatabase(environment, databaseId, database -> {
      database.newHierarchicalGraph(hierarchicalGraphId);
    });
  }
}
