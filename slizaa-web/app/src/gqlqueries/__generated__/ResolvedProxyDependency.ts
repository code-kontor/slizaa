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
// @generated
// This file was automatically generated and should not be edited.

// ====================================================
// GraphQL query operation: ResolvedProxyDependency
// ====================================================

export interface ResolvedProxyDependency_hierarchicalGraph_dependency_resolvedDependencies_sourceNode {
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
}

export interface ResolvedProxyDependency_hierarchicalGraph_dependency_resolvedDependencies_targetNode {
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
}

export interface ResolvedProxyDependency_hierarchicalGraph_dependency_resolvedDependencies {
  __typename: "Dependency";
  id: string;
  type: string;
  weight: number;
  sourceNode: ResolvedProxyDependency_hierarchicalGraph_dependency_resolvedDependencies_sourceNode;
  targetNode: ResolvedProxyDependency_hierarchicalGraph_dependency_resolvedDependencies_targetNode;
}

export interface ResolvedProxyDependency_hierarchicalGraph_dependency {
  __typename: "Dependency";
  id: string;
  resolvedDependencies: ResolvedProxyDependency_hierarchicalGraph_dependency_resolvedDependencies[];
}

export interface ResolvedProxyDependency_hierarchicalGraph {
  __typename: "HierarchicalGraph";
  /**
   * Returns the node with the given id
   */
  dependency: ResolvedProxyDependency_hierarchicalGraph_dependency | null;
}

export interface ResolvedProxyDependency {
  hierarchicalGraph: ResolvedProxyDependency_hierarchicalGraph | null;
}

export interface ResolvedProxyDependencyVariables {
  databaseId: string;
  hierarchicalGraphId: string;
  proxyDependencyId: string;
}
