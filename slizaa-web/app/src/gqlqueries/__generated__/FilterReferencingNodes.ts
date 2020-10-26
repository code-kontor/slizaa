/* tslint:disable */
/* eslint-disable */
// @generated
// This file was automatically generated and should not be edited.

import { NodesToConsider } from "./../query-types";

// ====================================================
// GraphQL query operation: FilterReferencingNodes
// ====================================================

export interface FilterReferencingNodes_hierarchicalGraph_nodes_filterReferencingNodes {
  __typename: "NodeSet";
  /**
   * the node ids as an array
   */
  nodeIds: string[];
}

export interface FilterReferencingNodes_hierarchicalGraph_nodes {
  __typename: "NodeSet";
  filterReferencingNodes: FilterReferencingNodes_hierarchicalGraph_nodes_filterReferencingNodes;
}

export interface FilterReferencingNodes_hierarchicalGraph {
  __typename: "HierarchicalGraph";
  /**
   * Returns the nodes with the given ids
   */
  nodes: FilterReferencingNodes_hierarchicalGraph_nodes;
}

export interface FilterReferencingNodes {
  hierarchicalGraph: FilterReferencingNodes_hierarchicalGraph | null;
}

export interface FilterReferencingNodesVariables {
  databaseId: string;
  hierarchicalGraphId: string;
  nodeIds: string[];
  nodeIdsToFilter: string[];
  nodesToConsider: NodesToConsider;
  includePredecessorsInResult: boolean;
}
