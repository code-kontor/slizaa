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
// GraphQL query operation: ChildrenFilteredByReferencingNodes
// ====================================================

export interface ChildrenFilteredByReferencingNodes_hierarchicalGraph_node_childrenFilteredByReferencingNodes_nodes {
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

export interface ChildrenFilteredByReferencingNodes_hierarchicalGraph_node_childrenFilteredByReferencingNodes {
  __typename: "NodeSet";
  /**
   * contained nodes as node array
   */
  nodes: ChildrenFilteredByReferencingNodes_hierarchicalGraph_node_childrenFilteredByReferencingNodes_nodes[];
}

export interface ChildrenFilteredByReferencingNodes_hierarchicalGraph_node {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
  childrenFilteredByReferencingNodes: ChildrenFilteredByReferencingNodes_hierarchicalGraph_node_childrenFilteredByReferencingNodes;
}

export interface ChildrenFilteredByReferencingNodes_hierarchicalGraph {
  __typename: "HierarchicalGraph";
  /**
   * Returns the node with the given id
   */
  node: ChildrenFilteredByReferencingNodes_hierarchicalGraph_node | null;
}

export interface ChildrenFilteredByReferencingNodes {
  hierarchicalGraph: ChildrenFilteredByReferencingNodes_hierarchicalGraph | null;
}

export interface ChildrenFilteredByReferencingNodesVariables {
  databaseId: string;
  hierarchicalGraphId: string;
  parentNodeId: string;
  selectedReferencingNodeIds: string[];
}
