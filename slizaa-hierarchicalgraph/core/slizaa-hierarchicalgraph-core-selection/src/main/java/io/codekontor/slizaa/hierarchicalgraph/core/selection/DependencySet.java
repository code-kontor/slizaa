/**
 * slizaa-hierarchicalgraph-core-selection - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.core.selection;

import static com.google.common.base.Preconditions.*;

import io.codekontor.slizaa.hierarchicalgraph.core.model.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a set of {@link HGCoreDependency HGCoreDependencies}.
 */
public class DependencySet {

    /**
     * The result of computeReferencedNodes
     */
    public static class ReferencedNodes {

        private Collection<HGNode> _selectedNodesWithSuccessorsAndPredecessors;

        private SourceOrTarget _selectedNodesType;

        private Set<HGCoreDependency> _filteredCoreDependencies;

        private Set<HGNode> _filteredNodes;

        public ReferencedNodes(Collection<HGNode> selectedNodesWithSuccessorsAndPredecessors, SourceOrTarget selectedNodesType, Set<HGCoreDependency> filteredCoreDependencies, Set<HGNode> filteredNodes) {
            this._selectedNodesWithSuccessorsAndPredecessors = checkNotNull(selectedNodesWithSuccessorsAndPredecessors);
            this._selectedNodesType = checkNotNull(selectedNodesType);
            this._filteredCoreDependencies = checkNotNull(filteredCoreDependencies);
            this._filteredNodes = checkNotNull(filteredNodes);
        }

        public Collection<HGNode> getSelectedNodesWithSuccessorsAndPredecessors() {
            return Collections.unmodifiableCollection(_selectedNodesWithSuccessorsAndPredecessors);
        }

        public SourceOrTarget getSelectedNodesType() {
            return _selectedNodesType;
        }

        public Set<HGCoreDependency> getFilteredCoreDependencies() {
            return _filteredCoreDependencies;
        }

        public Set<HGNode> getFilteredNodes(boolean includePredecessors) {
            return includePredecessors ?
                    _filteredNodes.stream().flatMap(node -> Stream.concat(node.getPredecessors().stream(), Stream.of(node))).collect(Collectors.toSet()) :
                    _filteredNodes;
        }
    }

    private final Collection<HGCoreDependency> _unfilteredCoreDependencies;

    private final Map<HGNode, Set<HGCoreDependency>> _sourceNode2CoreDependenciesMap;

    private final Map<HGNode, Set<HGCoreDependency>> _targetNode2CoreDependenciesMap;

    private Set<HGNode> _unfilteredSourceNodes;

    private Set<HGNode> _unfilteredSourceNodePredecessors;

    private Set<HGNode> _unfilteredTargetNodes;

    private Set<HGNode> _unfilteredTargetNodePredecessors;

    public DependencySet(Collection<HGCoreDependency> dependencies) {

        //
        _unfilteredCoreDependencies = Collections.unmodifiableCollection(checkNotNull(dependencies));

        //
        _sourceNode2CoreDependenciesMap = new HashMap<>();
        _targetNode2CoreDependenciesMap = new HashMap<>();
        _unfilteredSourceNodes = new HashSet<HGNode>();
        _unfilteredSourceNodePredecessors = new HashSet<HGNode>();
        _unfilteredTargetNodes = new HashSet<HGNode>();
        _unfilteredTargetNodePredecessors = new HashSet<HGNode>();

        //
        getResolvedCoreDependenciesOrProxyDependencyOtherwise(_unfilteredCoreDependencies).forEach(dep -> {
            _sourceNode2CoreDependenciesMap.computeIfAbsent(dep.getFrom(), key -> new HashSet<>()).add(dep);
            _targetNode2CoreDependenciesMap.computeIfAbsent(dep.getTo(), key -> new HashSet<>()).add(dep);
            _unfilteredSourceNodes.add(dep.getFrom());
            _unfilteredSourceNodePredecessors.addAll(dep.getFrom().getPredecessors());
            _unfilteredTargetNodes.add(dep.getTo());
            _unfilteredTargetNodePredecessors.addAll(dep.getTo().getPredecessors());
        });
    }

    public ReferencedNodes computeReferencedNodes(HGNode selectedNode, SourceOrTarget selectedNodesType) {
        return computeReferencedNodes(Collections.singleton(selectedNode), selectedNodesType);
    }

    public ReferencedNodes computeReferencedNodes(Collection<HGNode> selectedNodes, SourceOrTarget selectedNodesType) {

        // fetch the appropriate dependencies map
        Map<HGNode, Set<HGCoreDependency>> dependenciesMap = checkNotNull(selectedNodesType) == SourceOrTarget.SOURCE ? _sourceNode2CoreDependenciesMap : _targetNode2CoreDependenciesMap;

        // fetch the selected nodes with successors and predecessors
        Set<HGNode> selectedNodesWithSuccessorsAndPredecessors = checkNotNull(selectedNodes).stream()
                .flatMap(node -> Stream.concat(NodeSelections.getSuccessors(node, true).stream(), node.getPredecessors().stream()))
                .collect(Collectors.toSet());

        // get the selected nodes that are keys in the dependencies map
        Set<HGNode> keys = dependenciesMap.keySet().stream().filter(selectedNodesWithSuccessorsAndPredecessors::contains).collect(Collectors.toSet());

        // resolve the filtered core dependencies
        Set<HGCoreDependency> filteredCoreDependencies = keys.stream().flatMap(key -> dependenciesMap.get(key).stream()).collect(Collectors.toSet());

        // resolve the filtered nodes
        Set<HGNode> filterNodes = keys.stream().flatMap(key -> {
            return dependenciesMap.get(key).stream()
                    .map(dep -> selectedNodesType == SourceOrTarget.SOURCE ? dep.getTo() : dep.getFrom());
        }).collect(Collectors.toSet());

        // return the result
        return new ReferencedNodes(selectedNodesWithSuccessorsAndPredecessors, selectedNodesType, filteredCoreDependencies, filterNodes);
    }

    public Collection<HGCoreDependency> getUnfilteredCoreDependencies() {
        return _unfilteredCoreDependencies;
    }

    public Set<HGNode> getUnfilteredSourceNodes(boolean includePredecessors) {
        return  includePredecessors ?
                Stream.of(_unfilteredSourceNodes, _unfilteredSourceNodePredecessors)
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet()) :
                _unfilteredSourceNodes;
    }

    public Set<HGNode> getUnfilteredTargetNodes(boolean includePredecessors) {
        return  includePredecessors ?
                Stream.of(_unfilteredTargetNodes, _unfilteredTargetNodePredecessors)
                        .flatMap(Set::stream)
                        .collect(Collectors.toSet()) :
                _unfilteredTargetNodes;
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    private static Set<HGCoreDependency> getResolvedCoreDependenciesOrProxyDependencyOtherwise(Collection<HGCoreDependency> dependencies) {

        //
        Set<HGCoreDependency> coreDependencies = new HashSet<>();

        checkNotNull(dependencies).forEach((c) -> {
            if (c instanceof HGProxyDependency && ((HGProxyDependency) c).isResolved()
                    && ((HGProxyDependency) c).getAccumulatedCoreDependencies().size() > 0) {
                coreDependencies.addAll(((HGProxyDependency) c).getAccumulatedCoreDependencies());
            } else {
                coreDependencies.add(c);
            }
        });

        //
        return coreDependencies;
    }
}
