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

import com.google.common.math.IntMath;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;
import io.codekontor.slizaa.server.service.selection.ISelectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class DependencySet {

    private static Logger logger = LoggerFactory.getLogger(DependencySet.class);

    private ISelectionService _selectionService;

    private HGAggregatedDependency _aggregatedDependency;

    private transient ILabelDefinitionProvider labelDefinitionProvider;

    public DependencySet(ISelectionService selectionService, HGAggregatedDependency aggregatedDependency) {
        _selectionService = checkNotNull(selectionService);
        _aggregatedDependency = aggregatedDependency;

        //
        labelDefinitionProvider = Utils.getLabelDefinitionProvider(aggregatedDependency.getFrom());
    }

    public List<Dependency> dependencies() {

        if (_aggregatedDependency == null) {
            return Collections.emptyList();
        }

        return _aggregatedDependency.getCoreDependencies().stream()
                .sorted(Comparator.comparing(dep -> labelDefinitionProvider.getLabelDefinition(dep.getFrom()).getText()))
                .map(dep -> new Dependency(dep)).collect(Collectors.toList());
    }

    /*
     * page is starting with 1 !!
     */
    public DependencyPage dependencyPage(int pageNumber, int pageSize) {

        if (pageNumber < 1) {
            throw new IndexOutOfBoundsException("Invalid");
        }

        if (_aggregatedDependency == null) {
            return new DependencyPage(new PageInfo(1,0,0,0), Collections.emptyList());
        }

        List<HGCoreDependency> coreDependencies = new LinkedList<>(_aggregatedDependency.getCoreDependencies());
        coreDependencies.sort(Comparator.comparing(dep -> labelDefinitionProvider.getLabelDefinition(dep.getFrom()).getText()));

        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, coreDependencies.size());

        // TODO : partial list
        List<HGCoreDependency> partialList =
                startIndex > coreDependencies.size() ?
                        Collections.emptyList() :
                        coreDependencies.subList(startIndex, endIndex);

        List<Dependency> partialResultList = convert(partialList);
        int maxPages = IntMath.divide(coreDependencies.size(), pageSize, RoundingMode.CEILING);
        PageInfo pageInfo = new PageInfo(pageNumber, maxPages, pageSize, coreDependencies.size());

        return new DependencyPage(pageInfo, partialResultList);
    }

    public int size() {

        if (_aggregatedDependency == null) {
            return 0;
        }

        return _aggregatedDependency.getCoreDependencies().size();
    }

    public List<Node> referencedNodes(List<String> selectedNodes, NodeType selectedNodesType, boolean includedPredecessors) {

        if (_aggregatedDependency == null) {
            return Collections.emptyList();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("referencedNodes({},{},{})", selectedNodes, selectedNodesType, includedPredecessors);
        }

        List<Node> result = NodeUtils.toNodes(referencedHgNodes(selectedNodes, selectedNodesType, includedPredecessors));

        if (logger.isDebugEnabled()) {
            logger.debug("referencedNodes result:{}", result);
        }

        return result;
    }

    public List<String> referencedNodeIds(List<String> selectedNodes, NodeType selectedNodesType, boolean includedPredecessors) {

        if (_aggregatedDependency == null) {
            return Collections.emptyList();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("referencedNodeIds({},{},{})", selectedNodes, selectedNodesType, includedPredecessors);
        }

        List<String> result = NodeUtils.toNodeIds(referencedHgNodes(selectedNodes, selectedNodesType, includedPredecessors));

        if (logger.isDebugEnabled()) {
            logger.debug("referencedNodeIds result: {}", result);
        }

        return result;
    }

    public List<Node> filteredChildren(String parentNode, NodeType parentNodeType) {

        if (_aggregatedDependency == null) {
            return Collections.emptyList();
        }

        return NodeUtils.toNodes(filteredHgNodesChildren(parentNode, parentNodeType));
    }

    public List<String> filteredChildrenIds(String parentNode, NodeType parentNodeType) {

        if (_aggregatedDependency == null) {
            return Collections.emptyList();
        }

        return NodeUtils.toNodeIds(filteredHgNodesChildren(parentNode, parentNodeType));
    }

    public DependencySet filteredDependencies(List<String> selectedNodes, NodeType selectedNodesType) {
        throw new UnsupportedOperationException("filteredDependencies");
    }

    private Set<HGNode> referencedHgNodes(List<String> selectedNodes, NodeType selectedNodesType, boolean includedPredecessors) {

        if (_aggregatedDependency == null) {
            return Collections.emptySet();
        }

        Set<HGNode> hgNodes = checkNotNull(selectedNodes).
                stream().map(id -> _aggregatedDependency.getRootNode().lookupNode(Long.parseLong(id))).collect(Collectors.toSet());

        return checkNotNull(selectedNodesType).equals(NodeType.SOURCE) ?
                _selectionService.getReferencedTargetNodes(_aggregatedDependency, hgNodes, includedPredecessors) :
                _selectionService.getReferencedSourceNodes(_aggregatedDependency, hgNodes, includedPredecessors);
    }

    private Set<HGNode> filteredHgNodesChildren(String parentNode, NodeType parentNodeType) {

        if (_aggregatedDependency == null) {
            return Collections.emptySet();
        }

        HGNode hgNode = _aggregatedDependency.getRootNode().lookupNode(Long.parseLong(checkNotNull(parentNode)));
        // TODO: NULL CHECK
        return checkNotNull(parentNodeType).equals(NodeType.SOURCE) ?
                _selectionService.getChildrenFilteredByDependencySources(_aggregatedDependency, hgNode) :
                _selectionService.getChildrenFilteredByDependencyTargets(_aggregatedDependency, hgNode);
    }

    private List<Dependency> convert(List<HGCoreDependency> coreDependencies) {

        if (coreDependencies == null) {
            return Collections.emptyList();
        }

        return coreDependencies.stream()
                .map(dep -> new Dependency(dep)).collect(Collectors.toList());
    }
}
