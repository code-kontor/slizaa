/* tslint:disable */
/* eslint-disable */
// This file was automatically generated and should not be edited.

import { NodeType } from "./../query-types";

// ====================================================
// GraphQL query operation: AggregatedDependencyDetails
// ====================================================

export interface AggregatedDependencyDetails_hierarchicalGraph_dependencySetForAggregatedDependency_filteredDependencies {
  __typename: "FilteredDependencies";
  sourceNodeIds: string[];
  targetNodeIds: string[];
}

export interface AggregatedDependencyDetails_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_pageInfo {
  __typename: "PageInfo";
  pageNumber: number;
  maxPages: number;
  pageSize: number;
  totalCount: number;
}

export interface AggregatedDependencyDetails_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_dependencies_sourceNode {
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

export interface AggregatedDependencyDetails_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_dependencies_targetNode {
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

export interface AggregatedDependencyDetails_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_dependencies {
  __typename: "Dependency";
  id: string;
  type: string;
  weight: number;
  sourceNode: AggregatedDependencyDetails_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_dependencies_sourceNode;
  targetNode: AggregatedDependencyDetails_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_dependencies_targetNode;
}

export interface AggregatedDependencyDetails_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage {
  __typename: "DependencyPage";
  pageInfo: AggregatedDependencyDetails_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_pageInfo | null;
  dependencies: AggregatedDependencyDetails_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_dependencies[];
}

export interface AggregatedDependencyDetails_hierarchicalGraph_dependencySetForAggregatedDependency {
  __typename: "DependencySet";
  size: number;
  filteredDependencies: AggregatedDependencyDetails_hierarchicalGraph_dependencySetForAggregatedDependency_filteredDependencies;
  /**
   * Returns a dependency page with with the specified size
   */
  dependencyPage: AggregatedDependencyDetails_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage | null;
}

export interface AggregatedDependencyDetails_hierarchicalGraph_sourcePredecessors_predecessors {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
}

export interface AggregatedDependencyDetails_hierarchicalGraph_sourcePredecessors {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
  predecessors: AggregatedDependencyDetails_hierarchicalGraph_sourcePredecessors_predecessors[];
}

export interface AggregatedDependencyDetails_hierarchicalGraph_targetPredecessors_predecessors {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
}

export interface AggregatedDependencyDetails_hierarchicalGraph_targetPredecessors {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
  predecessors: AggregatedDependencyDetails_hierarchicalGraph_targetPredecessors_predecessors[];
}

export interface AggregatedDependencyDetails_hierarchicalGraph {
  __typename: "HierarchicalGraph";
  /**
   * Returns a dependency set based on an aggregated dependency
   */
  dependencySetForAggregatedDependency: AggregatedDependencyDetails_hierarchicalGraph_dependencySetForAggregatedDependency | null;
  /**
   * Returns the node with the given id
   */
  sourcePredecessors: AggregatedDependencyDetails_hierarchicalGraph_sourcePredecessors | null;
  /**
   * Returns the node with the given id
   */
  targetPredecessors: AggregatedDependencyDetails_hierarchicalGraph_targetPredecessors | null;
}

export interface AggregatedDependencyDetails {
  hierarchicalGraph: AggregatedDependencyDetails_hierarchicalGraph | null;
}

export interface AggregatedDependencyDetailsVariables {
  databaseId: string;
  hierarchicalGraphId: string;
  dependencySourceNodeId: string;
  dependencyTargetNodeId: string;
  selectedNodeIds: string[];
  selectedNodesType: NodeType;
  pageSize: number;
  pageNumber: number;
}
