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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Sets;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGProxyDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.SourceOrTarget;
import io.codekontor.slizaa.hierarchicalgraph.core.model.impl.Utilities;

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

    private Map<HGNode, Set<HGCoreDependency>> _sourceNode2CoreDependenciesMap;

    private Map<HGNode, Set<HGCoreDependency>> _targetNode2CoreDependenciesMap;

    private Map<HGNode, Set<HGProxyDependency>> _sourceNode2UnresolvedProxyDependenciesMap;

    private Map<HGNode, Set<HGProxyDependency>> _targetNode2UnresolvedProxyDependenciesMap;

    private Set<HGNode> _unfilteredSourceNodes;

    private Set<HGNode> _unfilteredSourceNodePredecessors;

    private Set<HGNode> _unfilteredTargetNodes;

    private Set<HGNode> _unfilteredTargetNodePredecessors;

    public DependencySet(Collection<HGCoreDependency> dependencies) {

        //
        _unfilteredCoreDependencies = Collections.unmodifiableCollection(checkNotNull(dependencies));

        initialize();
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

    public Set<HGNode> getFilteredSourceNodeChildren(HGNode node) {

        // auto-resolve proxy dependencies
        if (!this._sourceNode2UnresolvedProxyDependenciesMap.isEmpty()) {

            // find unresolved dependencies
            Set<HGNode> unresolvedNodes = this._sourceNode2UnresolvedProxyDependenciesMap.keySet().stream()
                    .filter(n -> n.equals(node) || node.getPredecessors().contains(n))
                    .collect(Collectors.toSet());

            // resolve it
            unresolvedNodes.forEach(n -> {
                _sourceNode2UnresolvedProxyDependenciesMap.remove(n).forEach(proxyDep -> {
                    proxyDep.resolveProxyDependencies();
                    // TODO cache dependency
                });
            });
        }

        Set<HGNode> nodeSet = getUnfilteredSourceNodes(true);
        return node.getChildren().stream().filter(n -> nodeSet.contains(n)).collect(Collectors.toSet());
    }

    public Set<HGNode> getFilteredTargetNodeChildren(HGNode node) {
        // TODO: AutoResolve for Incoming/Outgoing ProxyDependencies
        Set<HGNode> nodeSet = getUnfilteredTargetNodes(true);
        return node.getChildren().stream().filter(n -> nodeSet.contains(n)).collect(Collectors.toSet());
    }

    public void resolveAllProxyDependencies() {
        Utilities.resolveProxyDependencies(_unfilteredCoreDependencies);
    }

    Set<HGNode> getUnfilteredSourceNodes(boolean includePredecessors) {
        return  includePredecessors ?
                Stream.of(_unfilteredSourceNodes, _unfilteredSourceNodePredecessors)
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet()) :
                _unfilteredSourceNodes;
    }

    Set<HGNode> getUnfilteredTargetNodes(boolean includePredecessors) {
        return  includePredecessors ?
                Stream.of(_unfilteredTargetNodes, _unfilteredTargetNodePredecessors)
                        .flatMap(Set::stream)
                        .collect(Collectors.toSet()) :
                _unfilteredTargetNodes;
    }

    private void initialize() {
        _sourceNode2CoreDependenciesMap = new HashMap<>();
        _targetNode2CoreDependenciesMap = new HashMap<>();
        _sourceNode2UnresolvedProxyDependenciesMap = new HashMap<>();
        _targetNode2UnresolvedProxyDependenciesMap = new HashMap<>();
        _unfilteredSourceNodes = new HashSet<HGNode>();
        _unfilteredSourceNodePredecessors = new HashSet<HGNode>();
        _unfilteredTargetNodes = new HashSet<HGNode>();
        _unfilteredTargetNodePredecessors = new HashSet<HGNode>();
        _unfilteredCoreDependencies.forEach(dep -> {
            cacheDependency(dep);
            if (dep instanceof HGProxyDependency) {
                _targetNode2UnresolvedProxyDependenciesMap.computeIfAbsent(dep.getTo(), n -> new HashSet<>()).add((HGProxyDependency)dep);
                _sourceNode2UnresolvedProxyDependenciesMap.computeIfAbsent(dep.getFrom(), n -> new HashSet<>()).add((HGProxyDependency)dep);
            }
        });
    }

    private void cacheDependency(HGCoreDependency coreDependency) {
        _sourceNode2CoreDependenciesMap.computeIfAbsent(coreDependency.getFrom(), key -> new HashSet<>()).add(coreDependency);
        _targetNode2CoreDependenciesMap.computeIfAbsent(coreDependency.getTo(), key -> new HashSet<>()).add(coreDependency);
        _unfilteredSourceNodes.add(coreDependency.getFrom());
        _unfilteredSourceNodePredecessors.addAll(coreDependency.getFrom().getPredecessors());
        _unfilteredTargetNodes.add(coreDependency.getTo());
        _unfilteredTargetNodePredecessors.addAll(coreDependency.getTo().getPredecessors());
    }
}
