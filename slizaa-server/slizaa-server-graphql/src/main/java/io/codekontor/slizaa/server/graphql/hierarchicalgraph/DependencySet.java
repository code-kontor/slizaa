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
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.server.service.selection.ISelectionService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class DependencySet {

    private ISelectionService _selectionService;

    private HGAggregatedDependency _aggregatedDependency;

    public DependencySet(ISelectionService selectionService, HGAggregatedDependency aggregatedDependency) {
        _selectionService = checkNotNull(selectionService);
        _aggregatedDependency = checkNotNull(aggregatedDependency);
    }

    public List<Dependency> dependencies() {
        return _aggregatedDependency.getCoreDependencies().stream()
                .map(dep -> new Dependency(new Node(dep.getFrom()), new Node(dep.getTo()), dep.getWeight())).collect(Collectors.toList());
    }

    public int size() {
        return _aggregatedDependency.getCoreDependencies().size();
    }

    public List<Node> nodes(NodeType nodeType, boolean includedPredecessors) {

        Set<HGNode> nodes = checkNotNull(nodeType).equals(NodeType.SOURCE) ?
                _selectionService.getSourceNodes(_aggregatedDependency, includedPredecessors) :
                _selectionService.getTargetNodes(_aggregatedDependency, includedPredecessors);

        return AbstractNodeSet.toNodes(nodes);
    }

    public List<String> nodeIds(NodeType nodeType, boolean includedPredecessors) {

        Set<HGNode> nodes = checkNotNull(nodeType).equals(NodeType.SOURCE) ?
                _selectionService.getSourceNodes(_aggregatedDependency, includedPredecessors) :
                _selectionService.getTargetNodes(_aggregatedDependency, includedPredecessors);

        return AbstractNodeSet.toNodeIds(nodes);
    }

    public List<Node> referencedNodes(List<String> selectedNodes, NodeType selectedNodesType, boolean includedPredecessors) {
        return AbstractNodeSet.toNodes(referencedHgNodes(selectedNodes, selectedNodesType, includedPredecessors));
    }

    public List<String> referencedNodeIds(List<String> selectedNodes, NodeType selectedNodesType, boolean includedPredecessors) {
        return AbstractNodeSet.toNodeIds(referencedHgNodes(selectedNodes, selectedNodesType, includedPredecessors));
    }

    public NodeSet filteredChildren(String selectedNode, NodeType selectedNodeType) {
        HGNode hgNode = _aggregatedDependency.getRootNode().lookupNode(Long.parseLong(checkNotNull(selectedNode)));
        // TODO: NULL CHECK
        Set<HGNode> nodes = checkNotNull(selectedNodeType).equals(NodeType.SOURCE) ?
                _selectionService.getChildrenFilteredByDependencySources(_aggregatedDependency, hgNode) :
                _selectionService.getChildrenFilteredByDependencyTargets(_aggregatedDependency, hgNode);
        return new NodeSet(nodes);
    }

    public DependencySet filteredDependencies(List<String> selectedNodes, NodeType selectedNodesType) {
        throw new UnsupportedOperationException("filteredDependencies");
    }

    private Set<HGNode> referencedHgNodes(List<String> selectedNodes, NodeType selectedNodesType, boolean includedPredecessors) {

        Set<HGNode> hgNodes = checkNotNull(selectedNodes).
                stream().map(id -> _aggregatedDependency.getRootNode().lookupNode(Long.parseLong(id))).collect(Collectors.toSet());

        return checkNotNull(selectedNodesType).equals(NodeType.SOURCE) ?
                _selectionService.getReferencedTargetNodes(_aggregatedDependency, hgNodes, includedPredecessors) :
                _selectionService.getReferencedSourceNodes(_aggregatedDependency, hgNodes, includedPredecessors);
    }
}