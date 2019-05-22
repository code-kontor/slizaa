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

import java.util.List;
import java.util.stream.Collectors;

import io.codekontor.slizaa.server.graphql.hierarchicalgraph.HierarchicalGraph;
import io.codekontor.slizaa.server.service.slizaa.IGraphDatabase;
import io.codekontor.slizaa.server.service.slizaa.IHierarchicalGraph;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

/**
 *
 */
@Component
public class GraphDatabaseQuery extends AbstractDatabaseAwareComponent implements GraphQLQueryResolver {

  /**
   *
   * @return
   */
  public List<ContentDefinitionType> contentDefinitionTypes() {
    return slizaaService().getContentDefinitionProviderFactories().stream().map(factory -> new ContentDefinitionType(factory.getFactoryId(), factory.getName(), factory.getDescription()))
            .collect(Collectors.toList());
  }

  /**
   *
   * @return
   */
  public List<GraphDatabase> graphDatabases() {

    return slizaaService().getGraphDatabases().stream().map(db -> GraphDatabase.convert(db))
        .collect(Collectors.toList());
  }

  public GraphDatabase graphDatabase(String identifier) {

    IGraphDatabase graphDatabase = slizaaService().getGraphDatabase(identifier);
    return graphDatabase != null ? GraphDatabase.convert(graphDatabase) : null;
  }

  public HierarchicalGraph hierarchicalGraph(String databaseIdentifier, String hierarchicalGraphIdentifier) {

    IGraphDatabase graphDatabase = slizaaService().getGraphDatabase(databaseIdentifier);

    if (graphDatabase != null) {

      IHierarchicalGraph hierarchicalGraph = graphDatabase.getHierarchicalGraph(hierarchicalGraphIdentifier);

      if (hierarchicalGraph != null) {
        return new HierarchicalGraph(databaseIdentifier, hierarchicalGraphIdentifier);
      }
    }
    
    return null;
  }
}
