/* tslint:disable */
/* eslint-disable */
// @generated
// This file was automatically generated and should not be edited.

import { NodeType } from "./../query-types";

// ====================================================
// GraphQL query operation: NodeChildrenFilteredByDependencySet
// ====================================================

export interface NodeChildrenFilteredByDependencySet_hierarchicalGraph_dependencySetForAggregatedDependency_filteredChildren {
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

export interface NodeChildrenFilteredByDependencySet_hierarchicalGraph_dependencySetForAggregatedDependency {
  __typename: "DependencySet";
  /**
   * Returns a set of nodes filtered by the specified node type.
   */
  filteredChildren: NodeChildrenFilteredByDependencySet_hierarchicalGraph_dependencySetForAggregatedDependency_filteredChildren[];
}

export interface NodeChildrenFilteredByDependencySet_hierarchicalGraph {
  __typename: "HierarchicalGraph";
  /**
   * Returns the node with the given id
   * dependencies(ids: [ID!]!): DependencySet
   * Returns a dependency set based on an aggregated dependency
   */
  dependencySetForAggregatedDependency: NodeChildrenFilteredByDependencySet_hierarchicalGraph_dependencySetForAggregatedDependency | null;
}

export interface NodeChildrenFilteredByDependencySet {
  hierarchicalGraph: NodeChildrenFilteredByDependencySet_hierarchicalGraph | null;
}

export interface NodeChildrenFilteredByDependencySetVariables {
  databaseId: string;
  hierarchicalGraphId: string;
  sourceNodeId: string;
  targetNodeId: string;
  nodeId: string;
  nodeType: NodeType;
}
