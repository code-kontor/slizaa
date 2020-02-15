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
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.selection.IFilteredDependencies;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;

import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

public class FilteredDependencies {

    private IFilteredDependencies filteredDependencies;

    private ILabelDefinitionProvider labelDefinitionProvider;

    public FilteredDependencies() {
    }

    public FilteredDependencies(IFilteredDependencies filteredDependencies) {

        this.filteredDependencies = checkNotNull(filteredDependencies);

        if (!filteredDependencies.getCoreDependencies().isEmpty()) {
            labelDefinitionProvider = filteredDependencies.getCoreDependencies().
                    iterator().next().getFrom().getRootNode().getExtension(ILabelDefinitionProvider.class);
        }
    }

    public int getSize() {

        if (filteredDependencies == null || filteredDependencies.getCoreDependencies().isEmpty()) {
            return 0;
        }

        return filteredDependencies.getCoreDependencies().size();
    }

    public DependencyPage getDependencyPage(int pageNumber, int pageSize) {

        if (pageNumber < 1) {
            throw new IndexOutOfBoundsException("Invalid");
        }

        if (filteredDependencies == null || filteredDependencies.getCoreDependencies().isEmpty()) {
            return new DependencyPage(new PageInfo(1, 0, 0, 0), Collections.emptyList());
        }

        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, filteredDependencies.getCoreDependencies().size());

        List<Dependency> partialResultList = startIndex > filteredDependencies.getCoreDependencies().size() ?
                Collections.emptyList() :
                new LinkedList<>(filteredDependencies.getCoreDependencies()).subList(startIndex, endIndex)
                        .stream().map(coreDependency -> new Dependency(coreDependency))
                        .collect(Collectors.toList());

        int maxPages = IntMath.divide(filteredDependencies.getCoreDependencies().size(), pageSize, RoundingMode.CEILING);
        PageInfo pageInfo = new PageInfo(pageNumber, maxPages, pageSize, partialResultList.size());

        return new DependencyPage(pageInfo, partialResultList);
    }

    public List<Dependency> getDependencies() {

        if (filteredDependencies == null || filteredDependencies.getCoreDependencies().isEmpty()) {
            return Collections.emptyList();
        }

        return filteredDependencies.getCoreDependencies().stream()
                .sorted(Comparator.comparing(dep -> labelDefinitionProvider.getLabelDefinition(dep.getFrom()).getText()))
                .map(dep -> new Dependency(dep)).collect(Collectors.toList());
    }

    public List<Node> getNodes(NodeType nodeType, boolean includedPredecessors) {
        checkNotNull(nodeType);

        if (filteredDependencies == null || filteredDependencies.getCoreDependencies().isEmpty()) {
            return Collections.emptyList();
        }

        List<HGNode> nodes = filteredDependencies.getCoreDependencies().stream().map(dep -> NodeType.SOURCE.equals(nodeType) ? dep.getFrom() : dep.getTo()).distinct().collect(Collectors.toList());
        return NodeUtils.toNodes(nodes);
    }

    public List<String> getNodeIds(NodeType nodeType, boolean includedPredecessors) {
        checkNotNull(nodeType);

        if (filteredDependencies == null || filteredDependencies.getCoreDependencies().isEmpty()) {
            return Collections.emptyList();
        }

        Stream<HGNode> nodeIdStream = filteredDependencies.getCoreDependencies().stream().
                map(dep -> NodeType.SOURCE.equals(nodeType) ? dep.getFrom() : dep.getTo());

        if (includedPredecessors) {
            nodeIdStream = nodeIdStream.flatMap(node -> Stream.concat(Stream.of(node), node.getPredecessors().stream()));
        }

        return NodeUtils.toNodeIds(nodeIdStream.distinct().collect(Collectors.toList()));
    }
}
