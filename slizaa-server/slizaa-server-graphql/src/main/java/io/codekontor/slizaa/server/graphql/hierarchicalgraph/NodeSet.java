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

import java.util.Collection;

import io.codekontor.slizaa.hierarchicalgraph.core.algorithms.GraphUtils;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.server.graphql.hierarchicalgraph.internal.ReferencedNodesResolver;

public class NodeSet extends AbstractNodeSet {

  public NodeSet(Collection<HGNode> hgNodeSet) {
    super(hgNodeSet);
  }

  public NodeSet referencedNodes(boolean includePredecessors) {
    return new NodeSet(ReferencedNodesResolver.getReferencedNodes(hgNodeSet(), includePredecessors));
  }

  public NodeSet referencingNodes(boolean includePredecessors) {
    return new NodeSet(ReferencedNodesResolver.getReferencingNodes(hgNodeSet(), includePredecessors));
  }

  public NodeSet filterReferencedNodes(Collection<String> nodeIds, NodesToConsider nodesToConsider, boolean includePredecessorsInResult) {
    return new NodeSet(ReferencedNodesResolver.filterReferencedNodes(hgNodeSet(), nodeIds, nodesToConsider, includePredecessorsInResult));
  }

  public NodeSet filterReferencingNodes(Collection<String> nodeIds, NodesToConsider nodesToConsider, boolean includePredecessorsInResult) {
    return new NodeSet(ReferencedNodesResolver.filterReferencingNodes(hgNodeSet(), nodeIds, nodesToConsider, includePredecessorsInResult));
  }
  /**
   * @return
   */
  public OrderedAdjacencyMatrix orderedAdjacencyMatrix() {
    return new OrderedAdjacencyMatrix(GraphUtils.createOrderedAdjacencyMatrix(hgNodeSet()));
  }
}
