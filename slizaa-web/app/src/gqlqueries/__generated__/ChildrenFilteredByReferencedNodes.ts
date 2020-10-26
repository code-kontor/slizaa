/* tslint:disable */
/* eslint-disable */
// @generated
// This file was automatically generated and should not be edited.

// ====================================================
// GraphQL query operation: ChildrenFilteredByReferencedNodes
// ====================================================

export interface ChildrenFilteredByReferencedNodes_hierarchicalGraph_node_childrenFilteredByReferencedNodes_nodes {
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
  hasChildren: boolean;
}

export interface ChildrenFilteredByReferencedNodes_hierarchicalGraph_node_childrenFilteredByReferencedNodes {
  __typename: "NodeSet";
  /**
   * contained nodes as node array
   */
  nodes: ChildrenFilteredByReferencedNodes_hierarchicalGraph_node_childrenFilteredByReferencedNodes_nodes[];
}

export interface ChildrenFilteredByReferencedNodes_hierarchicalGraph_node {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
  childrenFilteredByReferencedNodes: ChildrenFilteredByReferencedNodes_hierarchicalGraph_node_childrenFilteredByReferencedNodes;
}

export interface ChildrenFilteredByReferencedNodes_hierarchicalGraph {
  __typename: "HierarchicalGraph";
  /**
   * Returns the node with the given id
   */
  node: ChildrenFilteredByReferencedNodes_hierarchicalGraph_node | null;
}

export interface ChildrenFilteredByReferencedNodes {
  hierarchicalGraph: ChildrenFilteredByReferencedNodes_hierarchicalGraph | null;
}

export interface ChildrenFilteredByReferencedNodesVariables {
  databaseId: string;
  hierarchicalGraphId: string;
  parentNodeId: string;
  selectedReferencedNodeIds: string[];
}
