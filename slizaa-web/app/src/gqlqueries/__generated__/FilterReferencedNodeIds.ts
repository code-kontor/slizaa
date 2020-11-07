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
// GraphQL query operation: FilterReferencedNodeIds
// ====================================================

export interface FilterReferencedNodeIds_hierarchicalGraph_nodes_filterReferencedNodes {
  __typename: "NodeSet";
  /**
   * the node ids as an array
   */
  nodeIds: string[];
}

export interface FilterReferencedNodeIds_hierarchicalGraph_nodes {
  __typename: "NodeSet";
  filterReferencedNodes: FilterReferencedNodeIds_hierarchicalGraph_nodes_filterReferencedNodes;
}

export interface FilterReferencedNodeIds_hierarchicalGraph {
  __typename: "HierarchicalGraph";
  /**
   * Returns the nodes with the given ids
   */
  nodes: FilterReferencedNodeIds_hierarchicalGraph_nodes;
}

export interface FilterReferencedNodeIds {
  hierarchicalGraph: FilterReferencedNodeIds_hierarchicalGraph | null;
}

export interface FilterReferencedNodeIdsVariables {
  databaseId: string;
  hierarchicalGraphId: string;
  nodeIds: string[];
  nodeIdsToFilter: string[];
  nodesToConsider: NodesToConsider;
  includePredecessorsInResult: boolean;
}
