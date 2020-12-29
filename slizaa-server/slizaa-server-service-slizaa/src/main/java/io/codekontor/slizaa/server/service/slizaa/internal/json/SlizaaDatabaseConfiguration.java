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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.codekontor.slizaa.server.slizaadb.SlizaaDatabaseState;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabaseConfiguration;
import io.codekontor.slizaa.server.slizaadb.IHierarchicalGraph;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
@JsonAutoDetect(getterVisibility = Visibility.NONE)
public class SlizaaDatabaseConfiguration implements ISlizaaDatabaseConfiguration {

    @JsonProperty("identifier")
    private String _identifier;

    @JsonProperty("port")
    private int _port;

    @JsonProperty("state")
    private String _state;

    @JsonProperty("hierarchicalGraphs")
    private List<GraphDatabaseHierarchicalGraphConfiguration> _hierarchicalGraphs = new ArrayList<>();

    @JsonProperty("contentDefinition")
    private GraphDatabaseContentDefinitionConfiguration _contentDefinition;

    public SlizaaDatabaseConfiguration(ISlizaaDatabase graphDatabase) {
        _identifier = graphDatabase.getIdentifier();
        _port = graphDatabase.getPort();
        _state = graphDatabase.getState().name();

        for (IHierarchicalGraph hierarchicalGraph : graphDatabase.getHierarchicalGraphs()) {
            _hierarchicalGraphs.add(new GraphDatabaseHierarchicalGraphConfiguration(hierarchicalGraph.getIdentifier()));
        }

        //
        if (graphDatabase.getContentDefinition() != null) {

            String contentDefinitionFactoryId = graphDatabase.getContentDefinition().getContentDefinitionProviderFactory()
                    .getFactoryId();

            String externalRepresenation = graphDatabase.getContentDefinition().toExternalRepresentation();

            _contentDefinition = new GraphDatabaseContentDefinitionConfiguration(contentDefinitionFactoryId,
                    externalRepresenation);
        }
    }

    protected SlizaaDatabaseConfiguration() {
        // json
    }

    @Override
    public final String getIdentifier() {
        return _identifier;
    }

    @Override
    public final int getPort() {
        return _port;
    }

    @Override
    public SlizaaDatabaseState getState() {
        return SlizaaDatabaseState.valueOf(_state);
    }

    @Override
    public String getContentDefinitionFactoryId() {
        return hasContentDefinition() ? _contentDefinition.getFactoryId() : null;
    }

    @Override
    public String getContentDefinitionExternalRepresentation() {
        return hasContentDefinition() ? _contentDefinition.getContentDefinition() : null;
    }

    @Override
    public boolean hasContentDefinition() {
        return _contentDefinition != null;
    }

    public List<GraphDatabaseHierarchicalGraphConfiguration> getHierarchicalGraphs() {
        return _hierarchicalGraphs;
    }

    public GraphDatabaseContentDefinitionConfiguration getContentDefinition() {
        return _contentDefinition;
    }
}
