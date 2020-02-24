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

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.SourceOrTarget;

import java.util.List;
import java.util.Set;

public interface IFilteredDependencies {

    //
    boolean hasNodeSelections(SourceOrTarget sourceOrTarget);

    //
    List<INodeSelection> getNodeSelections(SourceOrTarget sourceOrTarget);

    // returns the intersection of source-selected and target-selected dependencies
    Set<HGCoreDependency> getEffectiveCoreDependencies();

    // returns all nodes that are part of the filtered core dependencies
    Set<HGNode> getEffectiveNodes(SourceOrTarget sourceOrTarget, boolean includePredecessors);

    //
    Set<HGNode> getReferencedNodes(SourceOrTarget sourceOrTarget, boolean includePredecessors);
}
