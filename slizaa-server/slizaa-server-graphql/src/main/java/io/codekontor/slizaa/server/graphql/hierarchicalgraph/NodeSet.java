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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.codekontor.slizaa.hierarchicalgraph.core.algorithms.GraphUtils;
import io.codekontor.slizaa.hierarchicalgraph.core.algorithms.IDependencyStructureMatrix;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;

public class NodeSet extends AbstractNodeSet {

  public NodeSet(Collection<HGNode> hgNodeSet) {
    super(hgNodeSet);
  }

  public NodeSet referencedNodes(boolean includePredecessors) {

    Stream<HGNode> nodeStream = hgNodeSet().stream()
        .flatMap(hgNode -> hgNode.getAccumulatedOutgoingCoreDependencies().stream()).map(dep -> dep.getTo());

    if (includePredecessors) {
      nodeStream = nodeStream.flatMap(node -> Stream.concat(node.getPredecessors().stream(), Stream.of(node)));
    }

    List<HGNode> nodes = nodeStream.distinct().collect(Collectors.toList());

    return new NodeSet(nodes);
  }

  /**
   * @param ids
   * @return
   */
  public DependencyMatrix dependencyMatrix() {
    
    return new DependencyMatrix(GraphUtils.createDependencyStructureMatrix(hgNodeSet()));
  }
}
