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
