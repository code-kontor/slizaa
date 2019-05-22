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

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.server.service.slizaa.IGraphDatabase;
import io.codekontor.slizaa.server.service.slizaa.IHierarchicalGraph;
import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLResolver;

@Component
public class HierarchicalGraphResolver implements GraphQLResolver<HierarchicalGraph> {

  //
  @Autowired
  private ISlizaaService slizaaService;

  /**
   * @return
   */
  public Node rootNode(HierarchicalGraph hierarchicalGraph) {
    return nullSafe(hierarchicalGraph, hgRootNode -> new Node(hgRootNode));
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

  private HGNode hgNode(HierarchicalGraph hierarchicalGraph, String id) {
    return nullSafe(hierarchicalGraph, hgRootNode -> {
      return "-1".equals(id) ? hgRootNode : hgRootNode.lookupNode(Long.parseLong(id));
    });
  }

  /**
   * @param function
   * @param <T>
   * @return
   */
  private <T> T nullSafe(HierarchicalGraph hierarchicalGraph, Function<HGRootNode, T> function) {

    // lookup the root node
    IGraphDatabase graphDatabase = slizaaService.getGraphDatabase(hierarchicalGraph.getDatabaseIdentifier());
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
