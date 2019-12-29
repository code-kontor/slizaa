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
import io.codekontor.slizaa.hierarchicalgraph.core.selection.IReferencedNodes;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * The result of computeReferencedNodes
 */
public class DefaultReferencedNodes implements IReferencedNodes {

    private Collection<HGNode> _selectedNodes;

    private SourceOrTarget _selectedNodesType;

    private Set<HGCoreDependency> _selectedCoreDependencies;

    private Set<HGNode> _referencedNodes;

    public DefaultReferencedNodes(Collection<HGNode> selectedNodes, SourceOrTarget selectedNodesType,
                                  Set<HGCoreDependency> selectedCoreDependencies, Set<HGNode> referencedNodes) {
        this._selectedNodes = checkNotNull(selectedNodes);
        this._selectedNodesType = checkNotNull(selectedNodesType);
        this._selectedCoreDependencies = checkNotNull(selectedCoreDependencies);
        this._referencedNodes = checkNotNull(referencedNodes);
    }

    @Override
    public Collection<HGNode> getReferencedNodes() {
        return Collections.unmodifiableCollection(_selectedNodes);
    }

    @Override
    public SourceOrTarget getSelectedNodesType() {
        return _selectedNodesType;
    }

    @Override
    public Set<HGCoreDependency> getSelectedCoreDependencies() {
        return _selectedCoreDependencies;
    }

    @Override
    public Set<HGNode> getReferencedNodes(boolean includePredecessors) {
        return includePredecessors
                ? _referencedNodes.stream().flatMap(node -> Stream.concat(node.getPredecessors().stream(), Stream.of(node)))
                .collect(Collectors.toSet())
                : _referencedNodes;
    }
}
