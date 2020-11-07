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

import { NodesToConsider } from "./../query-types";

// ====================================================
// GraphQL query operation: FilterReferencedNodes
// ====================================================

export interface FilterReferencedNodes_hierarchicalGraph_nodes_filterReferencedNodes_nodes_parent {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
}

export interface FilterReferencedNodes_hierarchicalGraph_nodes_filterReferencedNodes_nodes {
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
  /**
   * The parent node
   */
  parent: FilterReferencedNodes_hierarchicalGraph_nodes_filterReferencedNodes_nodes_parent | null;
}

export interface FilterReferencedNodes_hierarchicalGraph_nodes_filterReferencedNodes {
  __typename: "NodeSet";
  /**
   * contained nodes as node array
   */
  nodes: FilterReferencedNodes_hierarchicalGraph_nodes_filterReferencedNodes_nodes[];
}

export interface FilterReferencedNodes_hierarchicalGraph_nodes {
  __typename: "NodeSet";
  filterReferencedNodes: FilterReferencedNodes_hierarchicalGraph_nodes_filterReferencedNodes;
}

export interface FilterReferencedNodes_hierarchicalGraph {
  __typename: "HierarchicalGraph";
  /**
   * Returns the nodes with the given ids
   */
  nodes: FilterReferencedNodes_hierarchicalGraph_nodes;
}

export interface FilterReferencedNodes {
  hierarchicalGraph: FilterReferencedNodes_hierarchicalGraph | null;
}

export interface FilterReferencedNodesVariables {
  databaseId: string;
  hierarchicalGraphId: string;
  nodeIds: string[];
  nodeIdsToFilter: string[];
  nodesToConsider: NodesToConsider;
  includePredecessorsInResult: boolean;
}
