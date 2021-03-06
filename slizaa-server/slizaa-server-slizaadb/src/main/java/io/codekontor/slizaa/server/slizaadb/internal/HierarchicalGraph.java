/**
 * slizaa-server-slizaadb - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.slizaadb.internal;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.server.slizaadb.IHierarchicalGraph;
import io.codekontor.slizaa.server.slizaadb.IHierarchicalGraphContainer;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
public class HierarchicalGraph implements IHierarchicalGraph {

    private HierarchicalGraphDefinition _hierarchicalGraphDefinition;

    private IHierarchicalGraphContainer _container;

    private HGRootNode _rootNode;

    private Function<HierarchicalGraphDefinition, HGRootNode> _creatorFunction;

    /**
     * @param hierarchicalGraphDefinition
     * @param container
     */
    public HierarchicalGraph(HierarchicalGraphDefinition hierarchicalGraphDefinition,
                             IHierarchicalGraphContainer container, Function<HierarchicalGraphDefinition, HGRootNode> creatorFunction) {

        _hierarchicalGraphDefinition = checkNotNull(hierarchicalGraphDefinition);
        _container = checkNotNull(container);
        _creatorFunction = checkNotNull(creatorFunction);
    }

    public HGRootNode getRootNode() {
        return this._rootNode;
    }

    @Override
    public IHierarchicalGraphContainer getContainer() {
        return _container;
    }

    @Override
    public String getIdentifier() {
        return _hierarchicalGraphDefinition.getIdentifier();
    }

    public void initialize(boolean initializeCaches) {
        _rootNode = _creatorFunction.apply(_hierarchicalGraphDefinition);
        if (initializeCaches) {
            _rootNode.initializeCaches(_rootNode.getChildren());
        }
    }

    public void dispose() {
        _rootNode = null;
    }
}
