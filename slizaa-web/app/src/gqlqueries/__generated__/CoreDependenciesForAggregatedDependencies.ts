/* tslint:disable */
/* eslint-disable */
// This file was automatically generated and should not be edited.

import { NodeSelection } from "./../query-types";

// ====================================================
// GraphQL query operation: CoreDependenciesForAggregatedDependencies
// ====================================================

export interface CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_filteredDependencies_dependencyPage_pageInfo {
  __typename: "PageInfo";
  pageNumber: number;
  maxPages: number;
  pageSize: number;
  totalCount: number;
}

export interface CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_filteredDependencies_dependencyPage_dependencies_sourceNode {
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

export interface CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_filteredDependencies_dependencyPage_dependencies_targetNode {
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

export interface CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_filteredDependencies_dependencyPage_dependencies {
  __typename: "Dependency";
  id: string;
  isProxyDependency: boolean;
  type: string;
  weight: number;
  sourceNode: CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_filteredDependencies_dependencyPage_dependencies_sourceNode;
  targetNode: CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_filteredDependencies_dependencyPage_dependencies_targetNode;
}

export interface CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_filteredDependencies_dependencyPage {
  __typename: "DependencyPage";
  pageInfo: CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_filteredDependencies_dependencyPage_pageInfo | null;
  dependencies: CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_filteredDependencies_dependencyPage_dependencies[];
}

export interface CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_filteredDependencies {
  __typename: "FilteredDependencies";
  size: number;
  /**
   * Returns a dependency page with with the specified size
   */
  dependencyPage: CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_filteredDependencies_dependencyPage | null;
}

export interface CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency {
  __typename: "DependencySet";
  filteredDependencies: CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_filteredDependencies;
}

export interface CoreDependenciesForAggregatedDependencies_hierarchicalGraph {
  __typename: "HierarchicalGraph";
  /**
   * Returns a dependency set based on an aggregated dependency
   */
  dependencySetForAggregatedDependency: CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency | null;
}

export interface CoreDependenciesForAggregatedDependencies {
  hierarchicalGraph: CoreDependenciesForAggregatedDependencies_hierarchicalGraph | null;
}

export interface CoreDependenciesForAggregatedDependenciesVariables {
  databaseId: string;
  hierarchicalGraphId: string;
  dependencySourceNodeId: string;
  dependencyTargetNodeId: string;
  pageSize: number;
  pageNumber: number;
  nodeSelections: NodeSelection[];
}
