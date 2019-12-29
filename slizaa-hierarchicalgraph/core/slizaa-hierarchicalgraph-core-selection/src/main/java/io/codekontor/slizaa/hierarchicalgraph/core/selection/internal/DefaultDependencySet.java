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
package io.codekontor.slizaa.hierarchicalgraph.core.selection.internal;

import io.codekontor.slizaa.hierarchicalgraph.core.model.*;
import io.codekontor.slizaa.hierarchicalgraph.core.model.impl.Utilities;
import io.codekontor.slizaa.hierarchicalgraph.core.selection.IDependencySet;
import io.codekontor.slizaa.hierarchicalgraph.core.selection.IReferencedNodes;
import io.codekontor.slizaa.hierarchicalgraph.core.selection.NodeSelections;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a set of {@link HGCoreDependency HGCoreDependencies}.
 */
public class DefaultDependencySet implements IDependencySet {

    private final Collection<HGCoreDependency> _unfilteredCoreDependencies;

    private Map<HGNode, Set<HGCoreDependency>> _sourceNode2CoreDependenciesMap;

    private Map<HGNode, Set<HGCoreDependency>> _targetNode2CoreDependenciesMap;

    private Map<HGNode, Set<HGProxyDependency>> _sourceNode2UnresolvedProxyDependenciesMap;

    private Map<HGNode, Set<HGProxyDependency>> _targetNode2UnresolvedProxyDependenciesMap;

    private Set<HGNode> _unfilteredSourceNodes;

    private Set<HGNode> _unfilteredSourceNodePredecessors;

    private Set<HGNode> _unfilteredProxyDependencySourceNodes;

    private Set<HGNode> _unfilteredProxyDependencySourceNodePredecessors;

    private Set<HGNode> _unfilteredTargetNodes;

    private Set<HGNode> _unfilteredTargetNodePredecessors;

    private Set<HGNode> _unfilteredProxyDependencyTargetNodes;

    private Set<HGNode> _unfilteredProxyDependencyTargetNodePredecessors;

    private ProxyDependencyAdapter proxyDependencyAdapter;

    public DefaultDependencySet(Collection<HGCoreDependency> dependencies) {

        //
        _unfilteredCoreDependencies = Collections.unmodifiableCollection(checkNotNull(dependencies));

        initialize();
    }

    @Override
    public IReferencedNodes computeReferencedNodes(Collection<HGNode> selectedNodes, SourceOrTarget selectedNodesType, boolean includeResolvedProxyDependencies) {
        checkNotNull(selectedNodesType);
        checkNotNull(selectedNodes);

        // fetch the appropriate dependencies map
        Map<HGNode, Set<HGCoreDependency>> dependenciesMap = selectedNodesType == SourceOrTarget.SOURCE
                ? _sourceNode2CoreDependenciesMap
                : _targetNode2CoreDependenciesMap;

        Set<HGNode> unfilteredDependencyNodes = selectedNodesType == SourceOrTarget.SOURCE ?
                getUnfilteredSourceNodes(true, includeResolvedProxyDependencies) :
                getUnfilteredTargetNodes(true, includeResolvedProxyDependencies);

        // fetch the selected nodes with successors
        Set<HGNode> selectedNodesWithSuccessors = selectedNodes.stream()
                .flatMap(node -> NodeSelections.getSuccessors(node, true).stream())
                .filter(n -> unfilteredDependencyNodes.contains(n))
                .collect(Collectors.toSet());

        // get the selected nodes that are keys in the dependencies map
        Set<HGNode> keys = dependenciesMap.keySet().stream().filter(selectedNodesWithSuccessors::contains)
                .collect(Collectors.toSet());

        // resolve the filtered core dependencies
        Set<HGCoreDependency> filteredCoreDependencies = keys.stream().flatMap(key -> dependenciesMap.get(key).stream())
                .collect(Collectors.toSet());

        // resolve the filtered nodes
        Set<HGNode> filterNodes = keys.stream().flatMap(key -> {
            return dependenciesMap.get(key).stream()
                    .map(dep -> selectedNodesType == SourceOrTarget.SOURCE ? dep.getTo() : dep.getFrom());
        }).collect(Collectors.toSet());

        // return the result
        return new DefaultReferencedNodes(selectedNodes, selectedNodesType, filteredCoreDependencies,
                filterNodes);
    }

    @Override
    public Collection<HGCoreDependency> getUnfilteredCoreDependencies() {
        return _unfilteredCoreDependencies;
    }

    @Override
    public Set<HGNode> getFilteredNodeChildren(HGNode node, SourceOrTarget sourceOrTarget, boolean resolveAndIncludeProxyDependencies) {

        checkNotNull(node);
        checkNotNull(sourceOrTarget);

        // auto-resolve proxy dependencies
        if (resolveAndIncludeProxyDependencies) {
            if (SourceOrTarget.SOURCE.equals(sourceOrTarget)) {
                resolveProxyDependenciesForNode(node, this._sourceNode2UnresolvedProxyDependenciesMap);
            } else {
                resolveProxyDependenciesForNode(node, this._targetNode2UnresolvedProxyDependenciesMap);
            }
        }

        Set<HGNode> nodeSet = SourceOrTarget.SOURCE.equals(sourceOrTarget) ?
                getUnfilteredSourceNodes(true, resolveAndIncludeProxyDependencies) :
                getUnfilteredTargetNodes(true, resolveAndIncludeProxyDependencies);

        Set<HGNode> result = node.getChildren().stream().filter(n -> nodeSet.contains(n)).collect(Collectors.toSet());

        return result;
    }

    @Override
    public void resolveAllProxyDependencies() {
        resolveProxyDependencies(_unfilteredCoreDependencies);
    }

    @Override
    public void resolveProxyDependencies(HGNode node, SourceOrTarget sourceOrTarget) {
        resolveProxyDependencies(Collections.singleton(node), sourceOrTarget);
    }

    @Override
    public void resolveProxyDependencies(Collection<HGNode> nodes, SourceOrTarget sourceOrTarget) {

        Map<HGNode, Set<HGProxyDependency>> unresolvedProxyDependencies =
                SourceOrTarget.SOURCE.equals(sourceOrTarget) ?
                        _sourceNode2UnresolvedProxyDependenciesMap :
                        _targetNode2UnresolvedProxyDependenciesMap;

        Set<HGNode> nodesToResolve = NodeSelections.getPredecessors(nodes, true);

        Set<HGCoreDependency> coreDependencies = unresolvedProxyDependencies.entrySet().stream()
                .filter(entry -> nodesToResolve.contains(entry.getKey()))
                .flatMap(entry -> entry.getValue().stream())
                .collect(Collectors.toSet());

        resolveProxyDependencies(coreDependencies);
    }

    Set<HGNode> getUnfilteredSourceNodes(boolean includePredecessors, boolean includeResolvedProxyDependencies) {
        if (includeResolvedProxyDependencies) {
            if (includePredecessors) {
                return Stream.of(_unfilteredSourceNodes, _unfilteredSourceNodePredecessors, _unfilteredProxyDependencySourceNodes, _unfilteredProxyDependencySourceNodePredecessors)
                        .flatMap(Set::stream).collect(Collectors.toSet());
            } else {
                return Stream.of(_unfilteredSourceNodes, _unfilteredProxyDependencySourceNodes)
                        .flatMap(Set::stream).collect(Collectors.toSet());
            }
        } else {
            if (includePredecessors) {
                return Stream.of(_unfilteredSourceNodes, _unfilteredSourceNodePredecessors)
                        .flatMap(Set::stream).collect(Collectors.toSet());
            } else {
                return _unfilteredSourceNodes;
            }
        }
    }

    Set<HGNode> getUnfilteredTargetNodes(boolean includePredecessors, boolean includeResolvedProxyDependencies) {

        if (includeResolvedProxyDependencies) {
            if (includePredecessors) {
                return Stream.of(_unfilteredTargetNodes, _unfilteredTargetNodePredecessors, _unfilteredProxyDependencyTargetNodes, _unfilteredProxyDependencyTargetNodePredecessors)
                        .flatMap(Set::stream).collect(Collectors.toSet());
            } else {
                return Stream.of(_unfilteredTargetNodes, _unfilteredProxyDependencyTargetNodes)
                        .flatMap(Set::stream).collect(Collectors.toSet());
            }
        } else {
            if (includePredecessors) {
                return Stream.of(_unfilteredTargetNodes, _unfilteredTargetNodePredecessors)
                        .flatMap(Set::stream).collect(Collectors.toSet());
            } else {
                return _unfilteredTargetNodes;
            }
        }
    }

    private void initialize() {

        proxyDependencyAdapter = new ProxyDependencyAdapter();

        _sourceNode2CoreDependenciesMap = new HashMap<>();
        _sourceNode2UnresolvedProxyDependenciesMap = new HashMap<>();
        _unfilteredSourceNodes = new HashSet<>();
        _unfilteredSourceNodePredecessors = new HashSet<>();
        _unfilteredProxyDependencySourceNodes = new HashSet<>();
        _unfilteredProxyDependencySourceNodePredecessors = new HashSet<>();

        _targetNode2CoreDependenciesMap = new HashMap<>();
        _targetNode2UnresolvedProxyDependenciesMap = new HashMap<>();
        _unfilteredTargetNodes = new HashSet<>();
        _unfilteredTargetNodePredecessors = new HashSet<>();
        _unfilteredProxyDependencyTargetNodes = new HashSet<>();
        _unfilteredProxyDependencyTargetNodePredecessors = new HashSet<>();

        _unfilteredCoreDependencies.forEach(dep -> {
            cacheDependency(dep);
            if (dep.isProxyDependency()) {
                HGProxyDependency proxyDependency = (HGProxyDependency) dep;
                if (proxyDependency.isResolved()) {
                    proxyDependency.getAccumulatedCoreDependencies().forEach(dependency -> cacheDependency(dependency));
                } else {
                    synchronized (proxyDependency) {
                        proxyDependency.eAdapters().add(proxyDependencyAdapter);
                        _targetNode2UnresolvedProxyDependenciesMap.computeIfAbsent(dep.getTo(), n -> new HashSet<>())
                                .add((HGProxyDependency) dep);
                        _sourceNode2UnresolvedProxyDependenciesMap.computeIfAbsent(dep.getFrom(), n -> new HashSet<>())
                                .add((HGProxyDependency) dep);
                    }
                }
            }
        });
    }

    /**
     * @param node
     * @param dependenciesMap
     */
    private void resolveProxyDependenciesForNode(HGNode node, Map<HGNode, Set<HGProxyDependency>> dependenciesMap) {

        if (dependenciesMap != null && !dependenciesMap.isEmpty()) {

            // find unresolved dependencies
            Set<HGProxyDependency> unresolvedProxyDependencies =
                    new HashSet<>(dependenciesMap.keySet()).stream()
                            .filter(n -> n.equals(node) || node.getPredecessors().contains(n))
                            .flatMap(n -> dependenciesMap.getOrDefault(n, Collections.emptySet()).stream())
                            .collect(Collectors.toSet());

            // resolve it
            resolveProxyDependencies(unresolvedProxyDependencies);
        }
    }

    /**
     * @param proxyDependencies
     */
    private void resolveProxyDependencies(Collection<? extends HGCoreDependency> proxyDependencies) {

        if (proxyDependencies == null) {
            return;
        }

        Set<HGProxyDependency> unresolvedProxyDependencies = proxyDependencies.stream()
                .filter(dep -> dep.isProxyDependency() && !((HGProxyDependency) dep).isResolved())
                .map(dep -> (HGProxyDependency) dep)
                .collect(Collectors.toSet());

        // concurrently resolve dependencies
        Utilities.resolveProxyDependencies(unresolvedProxyDependencies);
    }

    private void cacheDependency(HGCoreDependency coreDependency) {
        _sourceNode2CoreDependenciesMap.computeIfAbsent(coreDependency.getFrom(), key -> new HashSet<>())
                .add(coreDependency);
        _targetNode2CoreDependenciesMap.computeIfAbsent(coreDependency.getTo(), key -> new HashSet<>()).add(coreDependency);
        if (coreDependency.getProxyDependencyParent() != null) {
            _unfilteredProxyDependencySourceNodes.add(coreDependency.getFrom());
            _unfilteredProxyDependencySourceNodePredecessors.addAll(coreDependency.getFrom().getPredecessors());
            _unfilteredProxyDependencyTargetNodes.add(coreDependency.getTo());
            _unfilteredProxyDependencyTargetNodePredecessors.addAll(coreDependency.getTo().getPredecessors());
        } else {
            _unfilteredSourceNodes.add(coreDependency.getFrom());
            _unfilteredSourceNodePredecessors.addAll(coreDependency.getFrom().getPredecessors());
            _unfilteredTargetNodes.add(coreDependency.getTo());
            _unfilteredTargetNodePredecessors.addAll(coreDependency.getTo().getPredecessors());
        }
    }

    private class ProxyDependencyAdapter extends AdapterImpl {
        @Override
        public void notifyChanged(Notification msg) {

            if (msg.getFeatureID(HGProxyDependency.class) == HierarchicalgraphPackage.HG_PROXY_DEPENDENCY__RESOLVED) {
                HGProxyDependency proxyDependency = (HGProxyDependency) msg.getNotifier();

                synchronized (proxyDependency) {

                    proxyDependency.eAdapters().remove(proxyDependencyAdapter);

                    Set<HGProxyDependency> targetNodeSet = _targetNode2UnresolvedProxyDependenciesMap.get(proxyDependency.getTo());
                    targetNodeSet.remove(proxyDependency);
                    if (targetNodeSet.isEmpty()) {
                        _targetNode2UnresolvedProxyDependenciesMap.remove(proxyDependency.getTo());
                    }

                    Set<HGProxyDependency> sourceNodeSet = _sourceNode2UnresolvedProxyDependenciesMap.get(proxyDependency.getFrom());
                    sourceNodeSet.remove(proxyDependency);
                    if (sourceNodeSet.isEmpty()) {
                        _sourceNode2UnresolvedProxyDependenciesMap.remove(proxyDependency.getFrom());
                    }

                    proxyDependency.getAccumulatedCoreDependencies().forEach(d -> cacheDependency(d));
                }
            }
        }
    }
}
