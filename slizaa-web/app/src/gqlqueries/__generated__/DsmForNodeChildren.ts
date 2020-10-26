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
