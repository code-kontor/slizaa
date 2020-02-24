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

import com.google.common.math.IntMath;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.SourceOrTarget;
import io.codekontor.slizaa.hierarchicalgraph.core.selection.IFilteredDependencies;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.model.GraphUtil;

import java.math.RoundingMode;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class FilteredDependencies {

    private IFilteredDependencies filteredDependencies;

    public FilteredDependencies() {
    }

    public FilteredDependencies(IFilteredDependencies filteredDependencies) {
        this.filteredDependencies = checkNotNull(filteredDependencies);
    }

    public int getSize() {
        if (filteredDependencies == null || filteredDependencies.getEffectiveCoreDependencies().isEmpty()) {
            return 0;
        }
        return filteredDependencies.getEffectiveCoreDependencies().size();
    }

    public DependencyPage getDependencyPage(int pageNumber, int pageSize) {

        if (pageNumber < 1) {
            throw new IndexOutOfBoundsException("Invalid");
        }

        if (filteredDependencies == null || filteredDependencies.getEffectiveCoreDependencies().isEmpty()) {
            return new DependencyPage(new PageInfo(1, 0, 0, 0), Collections.emptyList());
        }

        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, filteredDependencies.getEffectiveCoreDependencies().size());

        List<Dependency> partialResultList = startIndex > filteredDependencies.getEffectiveCoreDependencies().size() ?
                Collections.emptyList() :
                new LinkedList<>(filteredDependencies.getEffectiveCoreDependencies()).subList(startIndex, endIndex)
                        .stream().map(coreDependency -> new Dependency(coreDependency))
                        .collect(Collectors.toList());

        int maxPages = IntMath.divide(filteredDependencies.getEffectiveCoreDependencies().size(), pageSize, RoundingMode.CEILING);
        PageInfo pageInfo = new PageInfo(pageNumber, maxPages, pageSize, filteredDependencies.getEffectiveCoreDependencies().size());

        return new DependencyPage(pageInfo, partialResultList);
    }

    public List<Dependency> getDependencies() {

        if (filteredDependencies == null || filteredDependencies.getEffectiveCoreDependencies().isEmpty()) {
            return Collections.emptyList();
        }

        //
        List<HGCoreDependency> coreDependencies = GraphUtil.sortCoreDependencies(filteredDependencies.getEffectiveCoreDependencies());
        return coreDependencies.stream().map(dep -> new Dependency(dep)).collect(Collectors.toList());
    }

    public List<Node> getNodes(NodeType nodeType, boolean includedPredecessors) {
        checkNotNull(nodeType);

        if (filteredDependencies == null || filteredDependencies.getEffectiveCoreDependencies().isEmpty()) {
            return Collections.emptyList();
        }

        return NodeUtils.toNodes(filteredDependencies.getEffectiveNodes(sourceOrTarget(nodeType), includedPredecessors), false);
    }

    public List<String> getNodeIds(NodeType nodeType, boolean includedPredecessors) {
        checkNotNull(nodeType);

        if (filteredDependencies == null || filteredDependencies.getEffectiveCoreDependencies().isEmpty()) {
            return Collections.emptyList();
        }

        return NodeUtils.toNodeIds(filteredDependencies.getEffectiveNodes(sourceOrTarget(nodeType), includedPredecessors), false);
    }

    public List<Node> referencedNodes(NodeType nodeType, boolean includedPredecessors) {
        checkNotNull(nodeType);

        if (filteredDependencies == null || filteredDependencies.getEffectiveCoreDependencies().isEmpty()) {
            return Collections.emptyList();
        }

        return NodeUtils.toNodes(filteredDependencies.getReferencedNodes(sourceOrTarget(nodeType), includedPredecessors), false);
    }

    public List<String> referencedNodeIds(NodeType nodeType, boolean includedPredecessors) {
        checkNotNull(nodeType);

        if (filteredDependencies == null || filteredDependencies.getEffectiveCoreDependencies().isEmpty()) {
            return Collections.emptyList();
        }

        return NodeUtils.toNodeIds(filteredDependencies.getReferencedNodes(sourceOrTarget(nodeType), includedPredecessors), false);
    }

    private SourceOrTarget sourceOrTarget(NodeType nodeType) {
        return NodeType.SOURCE.equals(nodeType) ? SourceOrTarget.SOURCE : SourceOrTarget.TARGET;
    }
}
