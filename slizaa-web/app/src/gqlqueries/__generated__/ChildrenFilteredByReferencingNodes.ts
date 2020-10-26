/* tslint:disable */
/* eslint-disable */
// @generated
// This file was automatically generated and should not be edited.

// ====================================================
// GraphQL query operation: ChildrenFilteredByReferencingNodes
// ====================================================

export interface ChildrenFilteredByReferencingNodes_hierarchicalGraph_node_childrenFilteredByReferencingNodes_nodes {
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

export interface ChildrenFilteredByReferencingNodes_hierarchicalGraph_node_childrenFilteredByReferencingNodes {
  __typename: "NodeSet";
  /**
   * contained nodes as node array
   */
  nodes: ChildrenFilteredByReferencingNodes_hierarchicalGraph_node_childrenFilteredByReferencingNodes_nodes[];
}

export interface ChildrenFilteredByReferencingNodes_hierarchicalGraph_node {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
  childrenFilteredByReferencingNodes: ChildrenFilteredByReferencingNodes_hierarchicalGraph_node_childrenFilteredByReferencingNodes;
}

export interface ChildrenFilteredByReferencingNodes_hierarchicalGraph {
  __typename: "HierarchicalGraph";
  /**
   * Returns the node with the given id
   */
  node: ChildrenFilteredByReferencingNodes_hierarchicalGraph_node | null;
}

export interface ChildrenFilteredByReferencingNodes {
  hierarchicalGraph: ChildrenFilteredByReferencingNodes_hierarchicalGraph | null;
}

export interface ChildrenFilteredByReferencingNodesVariables {
  databaseId: string;
  hierarchicalGraphId: string;
  parentNodeId: string;
  selectedReferencingNodeIds: string[];
}
