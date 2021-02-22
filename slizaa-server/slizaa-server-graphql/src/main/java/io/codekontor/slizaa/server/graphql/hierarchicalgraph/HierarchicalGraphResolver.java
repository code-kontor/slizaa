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

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import graphql.kickstart.tools.GraphQLResolver;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;
import io.codekontor.slizaa.server.slizaadb.IHierarchicalGraph;
import io.codekontor.slizaa.server.service.selection.IAggregatedDependencySelectionService;
import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HierarchicalGraphResolver implements GraphQLResolver<HierarchicalGraph> {

    @Autowired
    private ISlizaaService _slizaaService;

    @Autowired
    private IAggregatedDependencySelectionService _aggregatedDependencySelectionService;

    /**
     * @return
     */
    public Node rootNode(HierarchicalGraph hierarchicalGraph) {
        return nullSafe(hierarchicalGraph, hgRootNode -> new Node(hgRootNode));
    }

    public Dependency dependency(HierarchicalGraph hierarchicalGraph, String id) {
        return nullSafe(hierarchicalGraph, hgRootNode -> {
            HGCoreDependency coreDependency = hgRootNode.lookupCoreDependency(Long.parseLong(id));
            return coreDependency != null ? new Dependency(coreDependency) : null;
        });
    }

    /**
     * @param id
     * @return
     */
    public Node node(HierarchicalGraph hierarchicalGraph, String id) {
        return nullSafe(hierarchicalGraph, hgRootNode -> {
            HGNode hgNode = "-1".equals(id) ? hgRootNode : hgRootNode.lookupNode(Long.parseLong(id));
            return hgNode != null ? new Node(hgNode) : null;
        });
    }

    /**
     * @param ids
     * @return
     */
    public NodeSet nodes(HierarchicalGraph hierarchicalGraph, List<String> ids) {

        List<HGNode> nodes = ids.stream().map(id -> hgNode(hierarchicalGraph, id)).filter(node -> node != null)
                .collect(Collectors.toList());

        return new NodeSet(nodes);
    }

    public DependencySet dependencySetForAggregatedDependency(HierarchicalGraph hierarchicalGraph, String sourceNodeId, String targetNodeId) {
        return nullSafe(hierarchicalGraph, hgRootNode -> {
            HGNode toNode = hgRootNode.lookupNode(Long.parseLong(sourceNodeId));
            HGNode fromNode =  hgRootNode.lookupNode(Long.parseLong(targetNodeId));
            HGAggregatedDependency aggregatedDependency = toNode.getOutgoingDependenciesTo(fromNode);

            return aggregatedDependency == null ?
                new EmptyDependencySet() :
                new AggregatedDependencyDependencySet(_aggregatedDependencySelectionService, aggregatedDependency);
        });
    }

    private HGNode hgNode(HierarchicalGraph hierarchicalGraph, String id) {
        return nullSafe(hierarchicalGraph, hgRootNode -> {
            return hgRootNode.lookupNode(Long.parseLong(id));
        });
    }

    /**
     * @param function
     * @param <T>
     * @return
     */
    private <T> T nullSafe(HierarchicalGraph hierarchicalGraph, Function<HGRootNode, T> function) {

        // lookup the root node
        ISlizaaDatabase graphDatabase = _slizaaService.getGraphDatabase(hierarchicalGraph.getDatabaseIdentifier());
        if (graphDatabase != null) {
            IHierarchicalGraph hg = graphDatabase.getHierarchicalGraph(hierarchicalGraph.getIdentifier());
            if (hg != null) {
                HGRootNode rootNode = hg.getRootNode();
                if (rootNode != null) {
                    return function.apply(rootNode);
                }
            }
        }

        return null;
    }
}
