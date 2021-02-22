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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.codekontor.slizaa.hierarchicalgraph.core.algorithms.IOrderedAdjacencyMatrix;

public class OrderedAdjacencyMatrix {

  private List<Node>                       _orderedNodes;

  private int[][]                          _matrix;

  private List<StronglyConnectedComponent> _stronglyConnectedComponents;

  /**
   * 
   * @param dependencyStructureMatrix
   */
  public OrderedAdjacencyMatrix(IOrderedAdjacencyMatrix dependencyStructureMatrix) {

    //
    this._orderedNodes = dependencyStructureMatrix.getOrderedNodes().stream().map(hgNode -> new Node(hgNode))
        .collect(Collectors.toList());

    //
    this._matrix = dependencyStructureMatrix.getMatrix();
    
    //
    this._stronglyConnectedComponents = dependencyStructureMatrix.getCycles().stream()
        .map(hgNodes -> new StronglyConnectedComponent(hgNodes, dependencyStructureMatrix.getOrderedNodes())).collect(Collectors.toList());


  }

  public List<Node> orderedNodes() {
    return this._orderedNodes;
  }

  public List<Cell> getCells() {
    List<Cell> result = new ArrayList<>();
    for (int i = 0; i < this._matrix.length; i++) {
      for (int j = 0; j < this._matrix[i].length; j++) {
        result.add(new Cell(i, j, this._matrix[i][j]));
      }
    }
    return result;
  }

  /**
   * 
   * @return
   */
  public List<StronglyConnectedComponent> getStronglyConnectedComponents() {
    return _stronglyConnectedComponents;
  }
}
