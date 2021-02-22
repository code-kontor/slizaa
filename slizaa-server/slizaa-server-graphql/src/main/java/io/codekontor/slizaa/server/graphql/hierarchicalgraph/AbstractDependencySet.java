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

import io.codekontor.slizaa.server.graphql.hierarchicalgraph.internal.Utils;

import java.util.Collections;
import java.util.List;

public abstract class AbstractDependencySet implements DependencySet {

    private List<Dependency> _dependencies;

    public int getSize() {
        return dependencies().size();
    }

    public List<Dependency> getDependencies() {
        return Collections.unmodifiableList(dependencies());
    }

    public DependencyPage getDependencyPage(int pageNumber, int pageSize) {
        return Utils.getDependencyPage(dependencies(), pageNumber, pageSize, dep -> dep);
    }

    public List<Node> filteredChildren(String parentNode, NodeType parentNodeType) {

        if (dependencies().isEmpty()) {
            return Collections.emptyList();
        }

        return onFilteredChildren(parentNode, parentNodeType);
    }

    public List<String> filteredChildrenIds(String parentNode, NodeType parentNodeType) {

        if (dependencies().isEmpty()) {
            return Collections.emptyList();
        }

        return onFilteredChildrenIds(parentNode, parentNodeType);
    }

    public FilteredDependencies filteredDependencies(List<NodeSelection> nodeSelection) {
        if (dependencies().isEmpty()) {
            return new FilteredDependencies();
        }

        return onFilteredDependencies(nodeSelection);
    }

    protected abstract FilteredDependencies onFilteredDependencies(List<NodeSelection> nodeSelection);

    protected abstract List<Node> onFilteredChildren(String parentNode, NodeType parentNodeType);

    protected abstract List<String> onFilteredChildrenIds(String parentNode, NodeType parentNodeType);

    protected abstract List<Dependency> createDependencies();

    private List<Dependency> dependencies() {

        if (_dependencies == null) {
            _dependencies = createDependencies();

            //
            if (_dependencies == null) {
                _dependencies = Collections.emptyList();
            }
        }
        return _dependencies;
    }
}
