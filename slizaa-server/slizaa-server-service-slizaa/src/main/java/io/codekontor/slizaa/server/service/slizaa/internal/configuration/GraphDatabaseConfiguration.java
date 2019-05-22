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
package io.codekontor.slizaa.server.service.slizaa.internal.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.codekontor.slizaa.server.service.slizaa.IGraphDatabase;
import io.codekontor.slizaa.server.service.slizaa.IHierarchicalGraph;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
@JsonAutoDetect(getterVisibility=Visibility.NONE)
public class GraphDatabaseConfiguration {

  @JsonProperty("identifier")
  private String                                            _identifier;

  @JsonProperty("running")
  private boolean                                           _isRunning;

  @JsonProperty("port")
  private int                                               _port;

  @JsonProperty("hierarchicalGraphs")
  private List<GraphDatabaseHierarchicalGraphConfiguration> _hierarchicalGraphs = new ArrayList<>();

  @JsonProperty("contentDefinition")
  private GraphDatabaseContentDefinitionConfiguration       _contentDefinition;

  protected GraphDatabaseConfiguration() {
  }

  public GraphDatabaseConfiguration(IGraphDatabase graphDatabase) {
    _identifier = graphDatabase.getIdentifier();
    _isRunning = graphDatabase.isRunning();
    _port = graphDatabase.getPort();

    for (IHierarchicalGraph hierarchicalGraph : graphDatabase.getHierarchicalGraphs()) {
      _hierarchicalGraphs.add(new GraphDatabaseHierarchicalGraphConfiguration(hierarchicalGraph.getIdentifier()));
    }

    //
    if (graphDatabase.getContentDefinition() != null) {

      String contentDefinitionFactoryId = graphDatabase.getContentDefinition()
          .getContentDefinitionProviderFactory().getFactoryId();

      String externalRepresenation = graphDatabase.getContentDefinition().toExternalRepresentation();

      _contentDefinition = new GraphDatabaseContentDefinitionConfiguration(contentDefinitionFactoryId,
          externalRepresenation);
    }
  }

  public String getIdentifier() {
    return _identifier;
  }

  public boolean isRunning() {
    return _isRunning;
  }

  public int getPort() {
    return _port;
  }

  public List<GraphDatabaseHierarchicalGraphConfiguration> getHierarchicalGraphs() {
    return _hierarchicalGraphs;
  }

  public GraphDatabaseContentDefinitionConfiguration getContentDefinition() {
    return _contentDefinition;
  }
}
