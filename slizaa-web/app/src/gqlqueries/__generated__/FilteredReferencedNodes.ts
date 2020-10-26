/* tslint:disable */
/* eslint-disable */
// @generated
// This file was automatically generated and should not be edited.

// ====================================================
// GraphQL query operation: FilteredReferencedNodes
// ====================================================

export interface FilteredReferencedNodes_hierarchicalGraph_nodes_nodes {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
  /**
   * The text label
   */
  text: string;
}

export interface FilteredReferencedNodes_hierarchicalGraph_nodes_filteredReferencedNodes_nodes_parent {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
}

export interface FilteredReferencedNodes_hierarchicalGraph_nodes_filteredReferencedNodes_nodes {
  __typename: "Node";
  /**
   * The parent node
   */
  parent: FilteredReferencedNodes_hierarchicalGraph_nodes_filteredReferencedNodes_nodes_parent | null;
  /**
   * The text label
   */
  text: string;
  /**
   * The symbolicName for this node
   */
  id: string;
  /**
   * the icon identifier
   */
  iconIdentifier: string;
  hasChildren: boolean;
}

export interface FilteredReferencedNodes_hierarchicalGraph_nodes_filteredReferencedNodes {
  __typename: "NodeSet";
  /**
   * contained nodes as node array
   */
  nodes: FilteredReferencedNodes_hierarchicalGraph_nodes_filteredReferencedNodes_nodes[];
}

export interface FilteredReferencedNodes_hierarchicalGraph_nodes {
  __typename: "NodeSet";
  /**
   * contained nodes as node array
   */
  nodes: FilteredReferencedNodes_hierarchicalGraph_nodes_nodes[];
  filteredReferencedNodes: FilteredReferencedNodes_hierarchicalGraph_nodes_filteredReferencedNodes;
}

export interface FilteredReferencedNodes_hierarchicalGraph {
  __typename: "HierarchicalGraph";
  /**
   * Returns the nodes with the given ids
   */
  nodes: FilteredReferencedNodes_hierarchicalGraph_nodes;
}

export interface FilteredReferencedNodes {
  hierarchicalGraph: FilteredReferencedNodes_hierarchicalGraph | null;
}

export interface FilteredReferencedNodesVariables {
  databaseId: string;
  hierarchicalGraphId: string;
  selectedNodeIds: string[];
  expandedTargetNodeIds: string[];
}
