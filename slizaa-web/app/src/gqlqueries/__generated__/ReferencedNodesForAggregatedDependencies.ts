/* tslint:disable */
/* eslint-disable */
// This file was automatically generated and should not be edited.

// ====================================================
// GraphQL query operation: ReferencedNodesForAggregatedDependencies
// ====================================================

export interface ReferencedNodesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_filteredDependencies {
  __typename: "FilteredDependencies";
  sourceNodeIds: string[];
  targetNodeIds: string[];
}

export interface ReferencedNodesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency {
  __typename: "DependencySet";
  size: number;
  filteredDependencies: ReferencedNodesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_filteredDependencies;
}

export interface ReferencedNodesForAggregatedDependencies_hierarchicalGraph_sourcePredecessors_predecessors {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
}

export interface ReferencedNodesForAggregatedDependencies_hierarchicalGraph_sourcePredecessors {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
  predecessors: ReferencedNodesForAggregatedDependencies_hierarchicalGraph_sourcePredecessors_predecessors[];
}

export interface ReferencedNodesForAggregatedDependencies_hierarchicalGraph_targetPredecessors_predecessors {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
}

export interface ReferencedNodesForAggregatedDependencies_hierarchicalGraph_targetPredecessors {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
  predecessors: ReferencedNodesForAggregatedDependencies_hierarchicalGraph_targetPredecessors_predecessors[];
}

export interface ReferencedNodesForAggregatedDependencies_hierarchicalGraph {
  __typename: "HierarchicalGraph";
  /**
   * Returns a dependency set based on an aggregated dependency
   */
  dependencySetForAggregatedDependency: ReferencedNodesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency | null;
  /**
   * Returns the node with the given id
   */
  sourcePredecessors: ReferencedNodesForAggregatedDependencies_hierarchicalGraph_sourcePredecessors | null;
  /**
   * Returns the node with the given id
   */
  targetPredecessors: ReferencedNodesForAggregatedDependencies_hierarchicalGraph_targetPredecessors | null;
}

export interface ReferencedNodesForAggregatedDependencies {
  hierarchicalGraph: ReferencedNodesForAggregatedDependencies_hierarchicalGraph | null;
}

export interface ReferencedNodesForAggregatedDependenciesVariables {
  databaseId: string;
  hierarchicalGraphId: string;
  dependencySourceNodeId: string;
  dependencyTargetNodeId: string;
  selectedSourceNodeIds: string[];
  selectedTargetNodeIds: string[];
}
