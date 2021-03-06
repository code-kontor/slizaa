/**
 * slizaa-server-service-provisioning - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.provisioning.model.descr;

import io.codekontor.slizaa.server.service.provisioning.model.IGraphDatabaseDTO;

import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class GraphDatabaseDescr implements IGraphDatabaseDTO {

    private String id;
    private ContentDefinitionDescr contentDefinition;
    private List<HierarchicalGraphDescr> hierarchicalGraphs;
    private String state;
    private int port;
    private List<String> availableActions;

    /**
     * <p>
     * Creates a new instance of type {@link GraphDatabaseDescr}.
     * </p>
     *
     * @param id
     * @param contentDefinition
     * @param hierarchicalGraphs
     * @param state
     * @param port
     * @param availableActions
     */
    public GraphDatabaseDescr(String id, ContentDefinitionDescr contentDefinition,
                              List<HierarchicalGraphDescr> hierarchicalGraphs, String state, int port, List<String> availableActions) {

        this.id = checkNotNull(id);
        this.contentDefinition = checkNotNull(contentDefinition);
        this.hierarchicalGraphs = checkNotNull(hierarchicalGraphs);
        this.state = checkNotNull(state);
        this.port = port;
        this.availableActions = checkNotNull(availableActions);
    }

    public String getId() {
        return id;
    }

    public ContentDefinitionDescr getContentDefinition() {
        return contentDefinition;
    }

    public List<HierarchicalGraphDescr> getHierarchicalGraphs() {
        return hierarchicalGraphs;
    }

    public String getState() {
        return state;
    }

    public int getPort() {
        return port;
    }

    public List<String> getAvailableActions() {
        return availableActions;
    }

    @Override
    public int hashCode() {
        return IGraphDatabaseDTO.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return IGraphDatabaseDTO.equals(this, obj);
    }
}
