/**
 * slizaa-server-service-selection - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.selection.impl;

import static com.google.common.base.Preconditions.*;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.SourceOrTarget;
import io.codekontor.slizaa.hierarchicalgraph.core.selection.DependencySet;
import io.codekontor.slizaa.server.service.selection.IModifiableSelectionService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SelectionServiceImpl extends AbstractSelectionService implements IModifiableSelectionService {

    @Override
    public void dropSelections(HGRootNode rootNode) {
        dropSelectionsForRootNode(checkNotNull(rootNode));
    }

    @Override
    public Set<HGNode> getChildrenFilteredByDependencySources(HGAggregatedDependency aggregatedDependency, HGNode node) {
        return getFilteredChildren(aggregatedDependency, node, SourceOrTarget.SOURCE);
    }

    @Override
    public Set<HGNode> getChildrenFilteredByDependencyTargets(HGAggregatedDependency aggregatedDependency, HGNode node) {
        return getFilteredChildren(aggregatedDependency, node, SourceOrTarget.TARGET);
    }

    @Override
    public Set<HGNode> getReferencedSourceNodes(HGAggregatedDependency aggregatedDependency, Collection<HGNode> selectedTargetNodes, boolean includePredecessors) {
        return getReferencedNodes(aggregatedDependency, selectedTargetNodes, SourceOrTarget.TARGET, includePredecessors);
    }

    @Override
    public Set<HGNode> getReferencedTargetNodes(HGAggregatedDependency aggregatedDependency, Collection<HGNode> selectedSourceNodes, boolean includePredecessors) {
        return getReferencedNodes(aggregatedDependency, selectedSourceNodes, SourceOrTarget.SOURCE, includePredecessors);
    }

    @Override
    public Set<HGNode> getSourceNodes(HGAggregatedDependency aggregatedDependency, boolean includedPredecessors) {
        return getNodes(aggregatedDependency, SourceOrTarget.SOURCE, includedPredecessors);
    }

    @Override
    public Set<HGNode> getTargetNodes(HGAggregatedDependency aggregatedDependency, boolean includedPredecessors) {
        return getNodes(aggregatedDependency, SourceOrTarget.TARGET, includedPredecessors);
    }

    private Set<HGNode> getNodes(HGAggregatedDependency aggregatedDependency, SourceOrTarget nodeType, boolean includePredecessors) {
        checkNotNull(aggregatedDependency);
        checkNotNull(nodeType);

        DependencySet dependencySet = getDependenciesSelection(aggregatedDependency);
        return nodeType.equals(SourceOrTarget.SOURCE) ? dependencySet.getUnfilteredSourceNodes(includePredecessors) :
                dependencySet.getUnfilteredTargetNodes(includePredecessors);
    }

    private Set<HGNode> getFilteredChildren(HGAggregatedDependency aggregatedDependency, HGNode node, SourceOrTarget nodeType) {
        checkNotNull(node);
        checkNotNull(aggregatedDependency);
        checkNotNull(nodeType);

        DependencySet dependencySet = getDependenciesSelection(aggregatedDependency);
        Set<HGNode> nodeSet = nodeType.equals(SourceOrTarget.SOURCE) ? dependencySet.getUnfilteredSourceNodes(true) :
                dependencySet.getUnfilteredTargetNodes(true);
        return node.getChildren().stream().filter(n -> nodeSet.contains(n)).collect(Collectors.toSet());
    }

    private Set<HGNode> getReferencedNodes(HGAggregatedDependency aggregatedDependency, Collection<HGNode> selectedNodes, SourceOrTarget nodeType, boolean includePredecessors) {
        checkNotNull(aggregatedDependency);
        checkNotNull(selectedNodes);
        checkNotNull(nodeType);

        DependencySet dependencySet = getDependenciesSelection(aggregatedDependency);
        DependencySet.ReferencedNodes referencedNodes = dependencySet.computeReferencedNodes(selectedNodes, nodeType);
        return referencedNodes.getFilteredNodes(includePredecessors);
    }
}