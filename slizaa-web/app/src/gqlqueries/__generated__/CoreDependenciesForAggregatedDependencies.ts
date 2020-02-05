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

// ====================================================
// GraphQL query operation: CoreDependenciesForAggregatedDependencies
// ====================================================

export interface CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_pageInfo {
  __typename: "PageInfo";
  pageNumber: number;
  maxPages: number;
  pageSize: number;
  totalCount: number;
}

export interface CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_dependencies_sourceNode {
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

export interface CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_dependencies_targetNode {
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

export interface CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_dependencies {
  __typename: "Dependency";
  id: string;
  isProxyDependency: boolean;
  type: string;
  weight: number;
  sourceNode: CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_dependencies_sourceNode;
  targetNode: CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_dependencies_targetNode;
}

export interface CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage {
  __typename: "DependencyPage";
  pageInfo: CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_pageInfo | null;
  dependencies: CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage_dependencies[];
}

export interface CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency {
  __typename: "DependencySet";
  size: number;
  /**
   * Returns a dependency page with with the specified size
   */
  dependencyPage: CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_dependencyPage | null;
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
}
