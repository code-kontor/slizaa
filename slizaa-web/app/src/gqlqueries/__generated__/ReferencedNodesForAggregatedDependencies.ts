/* tslint:disable */
/* eslint-disable */
// This file was automatically generated and should not be edited.

// ====================================================
// GraphQL query operation: ReferencedNodesForAggregatedDependencies
// ====================================================

export interface ReferencedNodesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_pageInfo {
  __typename: "PageInfo";
  pageNumber: number;
  maxPages: number;
  pageSize: number;
  totalCount: number;
}

export interface ReferencedNodesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_dependencies_sourceNode {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
  /**
   * The text label
   */
  text: string;
  iconIdentifier: string;
}

export interface ReferencedNodesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_dependencies_targetNode {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
  /**
   * The text label
   */
  text: string;
  iconIdentifier: string;
}

export interface ReferencedNodesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_dependencies {
  __typename: "Dependency";
  type: string;
  weight: number;
  sourceNode: ReferencedNodesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_dependencies_sourceNode;
  targetNode: ReferencedNodesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_dependencies_targetNode;
}

export interface ReferencedNodesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage {
  __typename: "DependencyPage";
  pageInfo: ReferencedNodesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_pageInfo | null;
  dependencies: ReferencedNodesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_dependencies[];
}

export interface ReferencedNodesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency {
  __typename: "DependencySet";
  size: number;
  /**
   * Returns a dependency page with with the specified size
   */
  dependencyPage: ReferencedNodesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage | null;
}

export interface ReferencedNodesForAggregatedDependencies_hierarchicalGraph {
  __typename: "HierarchicalGraph";
  /**
   * Returns a dependency set based on an aggregated dependency
   */
  dependencySetForAggregatedDependency: ReferencedNodesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency | null;
}

export interface ReferencedNodesForAggregatedDependencies {
  hierarchicalGraph: ReferencedNodesForAggregatedDependencies_hierarchicalGraph | null;
}

export interface ReferencedNodesForAggregatedDependenciesVariables {
  databaseId: string;
  hierarchicalGraphId: string;
  dependencySourceNodeId: string;
  dependencyTargetNodeId: string;
  pageSize: number;
  pageNumber: number;
}
