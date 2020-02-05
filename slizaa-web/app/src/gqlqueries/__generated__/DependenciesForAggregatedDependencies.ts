/*
 * slizaa-web - Slizaa Static Software Analysis Tools
 * Copyright © 2019 Code-Kontor GmbH and others (slizaa@codekontor.io)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
