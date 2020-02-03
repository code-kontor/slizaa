/* tslint:disable */
/* eslint-disable */
// This file was automatically generated and should not be edited.

import { NodeType } from "./../query-types";

// ====================================================
// GraphQL query operation: DependenciesForAggregatedDependencies
// ====================================================

export interface DependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_pageInfo {
  __typename: "PageInfo";
  pageNumber: number;
  maxPages: number;
  pageSize: number;
  totalCount: number;
}

export interface DependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_dependencies_sourceNode {
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

export interface DependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_dependencies_targetNode {
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

export interface DependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_dependencies {
  __typename: "Dependency";
  type: string;
  weight: number;
  sourceNode: DependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_dependencies_sourceNode;
  targetNode: DependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_dependencies_targetNode;
}

export interface DependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage {
  __typename: "DependencyPage";
  pageInfo: DependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_pageInfo | null;
  dependencies: DependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_dependencies[];
}

export interface DependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency {
  __typename: "DependencySet";
  size: number;
  referencedNodeIds: string[];
  /**
   * Returns a dependency page with with the specified size
   */
  dependencyPage: DependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage | null;
}

export interface DependenciesForAggregatedDependencies_hierarchicalGraph_sourcePredecessors_predecessors {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
}

export interface DependenciesForAggregatedDependencies_hierarchicalGraph_sourcePredecessors {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
  predecessors: DependenciesForAggregatedDependencies_hierarchicalGraph_sourcePredecessors_predecessors[];
}

export interface DependenciesForAggregatedDependencies_hierarchicalGraph_targetPredecessors_predecessors {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
}

export interface DependenciesForAggregatedDependencies_hierarchicalGraph_targetPredecessors {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
  predecessors: DependenciesForAggregatedDependencies_hierarchicalGraph_targetPredecessors_predecessors[];
}

export interface DependenciesForAggregatedDependencies_hierarchicalGraph {
  __typename: "HierarchicalGraph";
  /**
   * Returns a dependency set based on an aggregated dependency
   */
  dependencySetForAggregatedDependency: DependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency | null;
  /**
   * Returns the node with the given id
   */
  sourcePredecessors: DependenciesForAggregatedDependencies_hierarchicalGraph_sourcePredecessors | null;
  /**
   * Returns the node with the given id
   */
  targetPredecessors: DependenciesForAggregatedDependencies_hierarchicalGraph_targetPredecessors | null;
}

export interface DependenciesForAggregatedDependencies {
  hierarchicalGraph: DependenciesForAggregatedDependencies_hierarchicalGraph | null;
}

export interface DependenciesForAggregatedDependenciesVariables {
  databaseId: string;
  hierarchicalGraphId: string;
  dependencySourceNodeId: string;
  dependencyTargetNodeId: string;
  selectedNodeIds: string[];
  selectedNodesType: NodeType;
  pageSize: number;
  pageNumber: number;
}
