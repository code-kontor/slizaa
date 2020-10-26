/* tslint:disable */
/* eslint-disable */
// @generated
// This file was automatically generated and should not be edited.

import { NodesToConsider } from "./../query-types";

// ====================================================
// GraphQL query operation: FilterReferencedNodes
// ====================================================

export interface FilterReferencedNodes_hierarchicalGraph_nodes_filterReferencedNodes {
  __typename: "NodeSet";
  /**
   * the node ids as an array
   */
  nodeIds: string[];
}

export interface FilterReferencedNodes_hierarchicalGraph_nodes_filterReferencingNodes {
  __typename: "NodeSet";
  /**
   * the node ids as an array
   */
  nodeIds: string[];
}

export interface FilterReferencedNodes_hierarchicalGraph_nodes {
  __typename: "NodeSet";
  filterReferencedNodes: FilterReferencedNodes_hierarchicalGraph_nodes_filterReferencedNodes;
  filterReferencingNodes: FilterReferencedNodes_hierarchicalGraph_nodes_filterReferencingNodes;
}

export interface FilterReferencedNodes_hierarchicalGraph {
  __typename: "HierarchicalGraph";
  /**
   * Returns the nodes with the given ids
   */
  nodes: FilterReferencedNodes_hierarchicalGraph_nodes;
}

export interface FilterReferencedNodes {
  hierarchicalGraph: FilterReferencedNodes_hierarchicalGraph | null;
}

export interface FilterReferencedNodesVariables {
  databaseId: string;
  hierarchicalGraphId: string;
  nodeIds: string[];
  nodeIdsToFilter: string[];
  nodesToConsider: NodesToConsider;
  includePredecessorsInResult: boolean;
}
