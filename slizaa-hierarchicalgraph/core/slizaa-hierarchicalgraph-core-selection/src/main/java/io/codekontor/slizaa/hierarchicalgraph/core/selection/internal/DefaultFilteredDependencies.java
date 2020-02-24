/**
 * slizaa-hierarchicalgraph-core-selection - Slizaa Static Software Analysis Tools
 * Copyright © 2019 Code-Kontor GmbH and others (slizaa@codekontor.io)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.codekontor.slizaa.hierarchicalgraph.core.selection.internal;

import com.google.common.collect.Streams;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.SourceOrTarget;
import io.codekontor.slizaa.hierarchicalgraph.core.selection.IFilteredDependencies;
import io.codekontor.slizaa.hierarchicalgraph.core.selection.INodeSelection;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * The result of computeReferencedNodes
 */
public class DefaultFilteredDependencies implements IFilteredDependencies {

    private List<INodeSelection> _sourceNodeSelections;

    private List<INodeSelection> _targetNodeSelections;

    private Set<HGCoreDependency> _sourceFilteredCoreDependencies;

    private Set<HGCoreDependency> _targetFilteredCoreDependencies;

    // computed sets
    private Set<HGCoreDependency> _effectiveCoreDependencies;

    private Set<HGNode> _effectiveSourceNodes;

    private Set<HGNode> _effectiveTargetNodes;

    public DefaultFilteredDependencies(List<INodeSelection> sourceNodeSelections,
                                       List<INodeSelection> targetNodeSelections,
                                       Set<HGCoreDependency> sourceFilteredCoreDependencies,
                                       Set<HGCoreDependency> targetFilteredCoreDependencies) {

        this._sourceNodeSelections = sourceNodeSelections;
        this._targetNodeSelections = targetNodeSelections;
        this._sourceFilteredCoreDependencies = sourceFilteredCoreDependencies;
        this._targetFilteredCoreDependencies = targetFilteredCoreDependencies;
    }

    @Override
    public boolean hasNodeSelections(SourceOrTarget sourceOrTarget) {
        return checkNotNull(sourceOrTarget).equals(SourceOrTarget.SOURCE) ? _sourceNodeSelections != null && !_sourceNodeSelections.isEmpty() : _targetNodeSelections != null && !_targetNodeSelections.isEmpty();
    }

    @Override
    public List<INodeSelection> getNodeSelections(SourceOrTarget sourceOrTarget) {

        switch (checkNotNull(sourceOrTarget)) {
            case SOURCE:
                return _sourceNodeSelections != null ? _sourceNodeSelections : Collections.emptyList();
            case TARGET:
                return _targetNodeSelections != null ? _targetNodeSelections : Collections.emptyList();
        }
        return Collections.emptyList();
    }

    @Override
    public Set<HGNode> getEffectiveNodes(SourceOrTarget sourceOrTarget, boolean includePredecessors) {

        Set<HGNode> filteredNodes = checkNotNull(sourceOrTarget).equals(SourceOrTarget.SOURCE) ?
                effectiveSourceNodes() : effectiveTargetNodes();

        Set<HGNode> result =
                includePredecessors
                        ? filteredNodes.stream().flatMap(node -> Stream.concat(node.getPredecessors().stream(), Stream.of(node)))
                        .collect(Collectors.toSet())
                        : filteredNodes;

        return Collections.unmodifiableSet(result);
    }

    @Override
    public Set<HGCoreDependency> getEffectiveCoreDependencies() {
        return Collections.unmodifiableSet(effectiveCoreDependencies());
    }

    /**
     * @Override public Set<HGCoreDependency> getSelectedCoreDependencies(SourceOrTarget sourceOrTarget) {
     * return checkNotNull(sourceOrTarget).equals(SourceOrTarget.SOURCE) ?
     * this._sourceFilteredCoreDependencies :
     * this._targetFilteredCoreDependencies;
     * }
     * @Override public Set<HGNode> getSelectedNodes(SourceOrTarget sourceOrTarget, boolean includePredecessors) {
     *
     * Collection<HGCoreDependency> selectedCoreDependencies = getSelectedCoreDependencies(checkNotNull(sourceOrTarget));
     *
     * if (selectedCoreDependencies == null) {
     * return Collections.emptySet();
     * }
     *
     * Stream<HGNode> nodeStream = selectedCoreDependencies.stream().map(
     * dep -> sourceOrTarget.equals(SourceOrTarget.SOURCE) ? dep.getFrom() : dep.getTo());
     *
     * if (includePredecessors) {
     * nodeStream = nodeStream.flatMap(node -> Streams.concat(Stream.of(node), node.getPredecessors().stream()));
     * }
     *
     * return nodeStream.collect(Collectors.toSet());
     * }
     */

    @Override
    public Set<HGNode> getReferencedNodes(SourceOrTarget sourceOrTarget, boolean includePredecessors) {

        Collection<HGCoreDependency> selectedCoreDependencies =
                checkNotNull(sourceOrTarget).equals(SourceOrTarget.SOURCE) ?
                        this._targetFilteredCoreDependencies :
                        this._sourceFilteredCoreDependencies;

        if (selectedCoreDependencies == null) {
            return Collections.emptySet();
        }

        Stream<HGNode> nodeStream = selectedCoreDependencies.stream().map(
                dep -> sourceOrTarget.equals(SourceOrTarget.SOURCE) ? dep.getFrom() : dep.getTo());

        if (includePredecessors) {
            nodeStream = nodeStream.flatMap(node -> Streams.concat(Stream.of(node), node.getPredecessors().stream()));
        }

        return nodeStream.collect(Collectors.toSet());
    }



    private Set<HGNode> effectiveSourceNodes() {

        // lazy init
        if (_effectiveSourceNodes == null) {
            _effectiveSourceNodes = effectiveCoreDependencies().stream().map(dep -> dep.getFrom()).collect(Collectors.toSet());
        }

        return _effectiveSourceNodes;
    }

    private Set<HGNode> effectiveTargetNodes() {

        // lazy init
        if (_effectiveTargetNodes == null) {
            _effectiveTargetNodes = effectiveCoreDependencies().stream().map(dep -> dep.getTo()).collect(Collectors.toSet());
        }

        return _effectiveTargetNodes;
    }

    private Set<HGCoreDependency> effectiveCoreDependencies() {

        // lazy init
        if (_effectiveCoreDependencies == null) {
            if (_sourceFilteredCoreDependencies == null && _targetFilteredCoreDependencies == null) {
                _effectiveCoreDependencies = Collections.emptySet();
            } else if (_sourceFilteredCoreDependencies == null) {
                _effectiveCoreDependencies = _targetFilteredCoreDependencies;
            } else if (_targetFilteredCoreDependencies == null) {
                _effectiveCoreDependencies = _sourceFilteredCoreDependencies;
            } else {
                _effectiveCoreDependencies = _sourceFilteredCoreDependencies.stream().filter(_targetFilteredCoreDependencies::contains).collect(Collectors.toSet());
            }
        }

        return _effectiveCoreDependencies;
    }
}