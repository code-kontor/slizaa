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
// GraphQL query operation: FilterReferencingNodes
// ====================================================

export interface FilterReferencingNodes_hierarchicalGraph_nodes_filterReferencingNodes_nodes_parent {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
}

export interface FilterReferencingNodes_hierarchicalGraph_nodes_filterReferencingNodes_nodes {
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
  parent: FilterReferencingNodes_hierarchicalGraph_nodes_filterReferencingNodes_nodes_parent | null;
}

export interface FilterReferencingNodes_hierarchicalGraph_nodes_filterReferencingNodes {
  __typename: "NodeSet";
  /**
   * contained nodes as node array
   */
  nodes: FilterReferencingNodes_hierarchicalGraph_nodes_filterReferencingNodes_nodes[];
}

export interface FilterReferencingNodes_hierarchicalGraph_nodes {
  __typename: "NodeSet";
  filterReferencingNodes: FilterReferencingNodes_hierarchicalGraph_nodes_filterReferencingNodes;
}

export interface FilterReferencingNodes_hierarchicalGraph {
  __typename: "HierarchicalGraph";
  /**
   * Returns the nodes with the given ids
   */
  nodes: FilterReferencingNodes_hierarchicalGraph_nodes;
}

export interface FilterReferencingNodes {
  hierarchicalGraph: FilterReferencingNodes_hierarchicalGraph | null;
}

export interface FilterReferencingNodesVariables {
  databaseId: string;
  hierarchicalGraphId: string;
  nodeIds: string[];
  nodeIdsToFilter: string[];
  nodesToConsider: NodesToConsider;
  includePredecessorsInResult: boolean;
}
