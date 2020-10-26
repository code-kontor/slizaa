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
package io.codekontor.slizaa.server.graphql.hierarchicalgraph;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.SourceOrTarget;
import io.codekontor.slizaa.hierarchicalgraph.core.selection.IFilteredDependencies;
import io.codekontor.slizaa.hierarchicalgraph.core.selection.INodeSelection;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.model.GraphUtil;
import io.codekontor.slizaa.server.graphql.hierarchicalgraph.internal.NodeUtils;
import io.codekontor.slizaa.server.graphql.hierarchicalgraph.internal.Utils;
import io.codekontor.slizaa.server.service.selection.IAggregatedDependencySelectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class AggregatedDependencyDependencySet extends AbstractDependencySet {

    private static Logger logger = LoggerFactory.getLogger(AggregatedDependencyDependencySet.class);

    private IAggregatedDependencySelectionService _selectionService;

    private HGAggregatedDependency _aggregatedDependency;

    private transient ILabelDefinitionProvider labelDefinitionProvider;

    public AggregatedDependencyDependencySet(IAggregatedDependencySelectionService selectionService, HGAggregatedDependency aggregatedDependency) {
        _selectionService = checkNotNull(selectionService);
        _aggregatedDependency = aggregatedDependency;

        //
        labelDefinitionProvider = Utils.getLabelDefinitionProvider(aggregatedDependency.getFrom());
    }

    @Override
    protected List<Node> onFilteredChildren(String parentNode, NodeType parentNodeType) {
        return NodeUtils.toNodes(filteredHgNodesChildren(parentNode, parentNodeType), true);
    }

    @Override
    protected List<String> onFilteredChildrenIds(String parentNode, NodeType parentNodeType) {
        return NodeUtils.toNodeIds(filteredHgNodesChildren(parentNode, parentNodeType), true);
    }

    @Override
    protected List<Dependency> createDependencies() {
        if (_aggregatedDependency == null) {
            return Collections.emptyList();
        }

        List<HGCoreDependency> coreDependencies = GraphUtil.sortCoreDependencies(_aggregatedDependency.getCoreDependencies());
        return coreDependencies.stream().map(dep -> new Dependency(dep)).collect(Collectors.toList());
    }

    @Override
    protected FilteredDependencies onFilteredDependencies(List<NodeSelection> nodeSelection) {

        List<INodeSelection> nodeSelections = nodeSelection.stream().map(ns -> {
            List<HGNode> nodes = ns.getSelectedNodeIds().stream().map(id -> _aggregatedDependency.getRootNode().lookupNode(Long.parseLong(checkNotNull(id)))).collect(Collectors.toList());
            return INodeSelection.create(nodes, NodeType.SOURCE.equals(ns.getSelectedNodesType()) ? SourceOrTarget.SOURCE : SourceOrTarget.TARGET);
        }).collect(Collectors.toList());

        // TODO: RESOLVE PROXY
        IFilteredDependencies filteredDependencies = _selectionService.getAggregatedDependencyDependencySet(_aggregatedDependency).getFilteredDependencies(nodeSelections);
        return new FilteredDependencies(filteredDependencies);
    }

    private Collection<HGNode> filteredHgNodesChildren(String parentNode, NodeType parentNodeType) {

        if (_aggregatedDependency == null) {
            return Collections.emptySet();
        }

        HGNode hgNode = _aggregatedDependency.getRootNode().lookupNode(Long.parseLong(checkNotNull(parentNode)));
        // TODO: NULL CHECK
        return checkNotNull(parentNodeType).equals(NodeType.SOURCE) ?
                // TODO:PROXY RESOLVE!
                _selectionService.getAggregatedDependencyDependencySet(_aggregatedDependency).getFilteredNodeChildren(hgNode, SourceOrTarget.SOURCE, false) :
                _selectionService.getAggregatedDependencyDependencySet(_aggregatedDependency).getFilteredNodeChildren(hgNode, SourceOrTarget.TARGET, false);
    }
}
