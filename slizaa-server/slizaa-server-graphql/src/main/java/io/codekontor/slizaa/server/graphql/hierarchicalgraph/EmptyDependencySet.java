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
package io.codekontor.slizaa.server.gql.hierarchicalgraph;

import java.util.Collections;
import java.util.List;

public class EmptyDependencySet implements DependencySet {

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public List<Dependency> getDependencies() {
        return Collections.emptyList();
    }

    @Override
    public DependencyPage getDependencyPage(int pageNumber, int pageSize) {
        return new DependencyPage(new PageInfo(1, pageSize, 0, 0), Collections.emptyList());
    }

    @Override
    public List<Node> filteredChildren(String parentNode, NodeType parentNodeType) {
        return Collections.emptyList();
    }

    @Override
    public List<String> filteredChildrenIds(String parentNode, NodeType parentNodeType) {
        return Collections.emptyList();
    }

    @Override
    public FilteredDependencies filteredDependencies(List<NodeSelection> nodeSelection) {
        return new FilteredDependencies();
    }
}
