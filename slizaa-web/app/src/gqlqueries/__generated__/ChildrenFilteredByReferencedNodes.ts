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
// GraphQL query operation: ChildrenFilteredByReferencedNodes
// ====================================================

export interface ChildrenFilteredByReferencedNodes_hierarchicalGraph_node_childrenFilteredByReferencedNodes_nodes {
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
  hasChildren: boolean;
}

export interface ChildrenFilteredByReferencedNodes_hierarchicalGraph_node_childrenFilteredByReferencedNodes {
  __typename: "NodeSet";
  /**
   * contained nodes as node array
   */
  nodes: ChildrenFilteredByReferencedNodes_hierarchicalGraph_node_childrenFilteredByReferencedNodes_nodes[];
}

export interface ChildrenFilteredByReferencedNodes_hierarchicalGraph_node {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
  childrenFilteredByReferencedNodes: ChildrenFilteredByReferencedNodes_hierarchicalGraph_node_childrenFilteredByReferencedNodes;
}

export interface ChildrenFilteredByReferencedNodes_hierarchicalGraph {
  __typename: "HierarchicalGraph";
  /**
   * Returns the node with the given id
   */
  node: ChildrenFilteredByReferencedNodes_hierarchicalGraph_node | null;
}

export interface ChildrenFilteredByReferencedNodes {
  hierarchicalGraph: ChildrenFilteredByReferencedNodes_hierarchicalGraph | null;
}

export interface ChildrenFilteredByReferencedNodesVariables {
  databaseId: string;
  hierarchicalGraphId: string;
  parentNodeId: string;
  selectedReferencedNodeIds: string[];
}
