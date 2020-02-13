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

    private Collection<HGCoreDependency> _filteredCoreDependencies;

    private Set<HGNode> _filteredSourceNodes;

    private Set<HGNode> _filteredTargetNodes;

    public DefaultFilteredDependencies(Collection<HGCoreDependency> selectedCoreDependencies) {
        this._filteredCoreDependencies = checkNotNull(selectedCoreDependencies);
    }

    @Override
    public Collection<HGNode> getNodes(SourceOrTarget sourceOrTarget, boolean includePredecessors) {

        Set<HGNode> filteredNodes = checkNotNull(sourceOrTarget).equals(SourceOrTarget.SOURCE) ?
                filteredSourceNodes() : filteredTargetNodes();

        return includePredecessors
                ? filteredNodes.stream().flatMap(node -> Stream.concat(node.getPredecessors().stream(), Stream.of(node)))
                .collect(Collectors.toSet())
                : filteredNodes;
    }

    @Override
    public Collection<HGCoreDependency> getCoreDependencies() {
        return Collections.unmodifiableCollection(_filteredCoreDependencies);
    }

    /**
     *
     * @return
     */
    private Set<HGNode> filteredSourceNodes() {
        if (_filteredSourceNodes == null) {
            _filteredSourceNodes = _filteredCoreDependencies.stream().map(dep -> dep.getFrom()).collect(Collectors.toSet());
        }
        return _filteredSourceNodes;
    }

    /**
     *
     * @return
     */
    private Set<HGNode> filteredTargetNodes() {
        if (_filteredTargetNodes == null) {
            _filteredTargetNodes = _filteredCoreDependencies.stream().map(dep -> dep.getTo()).collect(Collectors.toSet());
        }
        return _filteredTargetNodes;
    }
}
