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

import { NodeType } from "./../../__generated__/query-types";

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
