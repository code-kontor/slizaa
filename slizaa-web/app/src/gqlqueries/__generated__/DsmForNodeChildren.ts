/*
 * slizaa-web - Slizaa Static Software Analysis Tools
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
/* tslint:disable */
/* eslint-disable */
// @generated
// This file was automatically generated and should not be edited.

// ====================================================
// GraphQL query operation: DsmForNodeChildren
// ====================================================

export interface DsmForNodeChildren_hierarchicalGraph_node_children_orderedAdjacencyMatrix_orderedNodes {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
  /**
   * The text label
   */
  text: string;
  /**
   * the icon identifier
   */
  iconIdentifier: string;
}

export interface DsmForNodeChildren_hierarchicalGraph_node_children_orderedAdjacencyMatrix_cells {
  __typename: "Cell";
  row: number;
  column: number;
  value: number;
}

export interface DsmForNodeChildren_hierarchicalGraph_node_children_orderedAdjacencyMatrix_stronglyConnectedComponents {
  __typename: "StronglyConnectedComponent";
  nodePositions: number[];
}

export interface DsmForNodeChildren_hierarchicalGraph_node_children_orderedAdjacencyMatrix {
  __typename: "OrderedAdjacencyMatrix";
  orderedNodes: DsmForNodeChildren_hierarchicalGraph_node_children_orderedAdjacencyMatrix_orderedNodes[];
  cells: DsmForNodeChildren_hierarchicalGraph_node_children_orderedAdjacencyMatrix_cells[];
  stronglyConnectedComponents: DsmForNodeChildren_hierarchicalGraph_node_children_orderedAdjacencyMatrix_stronglyConnectedComponents[];
}

export interface DsmForNodeChildren_hierarchicalGraph_node_children {
  __typename: "NodeSet";
  /**
   * the adjacency matrix
   */
  orderedAdjacencyMatrix: DsmForNodeChildren_hierarchicalGraph_node_children_orderedAdjacencyMatrix;
}

export interface DsmForNodeChildren_hierarchicalGraph_node {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
  /**
   * The children
   */
  children: DsmForNodeChildren_hierarchicalGraph_node_children;
}

export interface DsmForNodeChildren_hierarchicalGraph {
  __typename: "HierarchicalGraph";
  /**
   * Returns the node with the given id
   */
  node: DsmForNodeChildren_hierarchicalGraph_node | null;
}

export interface DsmForNodeChildren {
  hierarchicalGraph: DsmForNodeChildren_hierarchicalGraph | null;
}

export interface DsmForNodeChildrenVariables {
  databaseId: string;
  hierarchicalGraphId: string;
  nodeId: string;
}
