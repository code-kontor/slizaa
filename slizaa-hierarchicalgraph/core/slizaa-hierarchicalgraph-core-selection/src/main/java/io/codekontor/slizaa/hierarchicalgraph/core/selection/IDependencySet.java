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
import io.codekontor.slizaa.hierarchicalgraph.core.selection.internal.DefaultDependencySet;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface IDependencySet {

    static IDependencySet create(Collection<HGCoreDependency> dependencies) {
        return new DefaultDependencySet(dependencies);
    }

    void resolveAllProxyDependencies();

    void resolveProxyDependencies(HGNode node, SourceOrTarget sourceOrTarget);

    void resolveProxyDependencies(Collection<HGNode> nodes, SourceOrTarget sourceOrTarget);

    /**
     * Returns the all core dependencies. ProxyDependencies will be returned as ProxyDependencies
     * (not auto-resolved, no children).
     *
     * @return
     */
    Set<HGCoreDependency> getUnfilteredCoreDependencies();

    Set<HGNode> getFilteredNodeChildren(HGNode node, SourceOrTarget sourceOrTarget, boolean resolveAndIncludeProxyDependencies);

    IFilteredDependencies getFilteredDependencies(Collection<INodeSelection> nodeSelections);

    IFilteredDependencies getFilteredDependencies(INodeSelection... nodeSelection);
}
