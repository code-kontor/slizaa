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
import gql from 'graphql-tag';

export const NodeChildrenQuery = gql`
query NodeChildren($databaseId: ID!, $hierarchicalGraphId: ID!, $nodeId: ID!)  {
  hierarchicalGraph(databaseIdentifier: $databaseId, hierarchicalGraphIdentifier: $hierarchicalGraphId) {
    node(id: $nodeId) {
      id
      children {
        nodes {
          id
          text
          iconIdentifier
          hasChildren
        }
      } 
    }
  }
}`



export const NodeChildrenFilteredByDependencySetQuery = gql`
query NodeChildrenFilteredByDependencySet($databaseId: ID!, $hierarchicalGraphId: ID!, $sourceNodeId: ID!, $targetNodeId:  ID!, $nodeId: ID!, $nodeType: NodeType!) {
	hierarchicalGraph(databaseIdentifier: $databaseId, hierarchicalGraphIdentifier: $hierarchicalGraphId) {
    dependencySetForAggregatedDependency(sourceNodeId: $sourceNodeId, targetNodeId: $targetNodeId) {
      filteredChildren(parentNode: $nodeId, parentNodeType: $nodeType) {
          id
          text
          iconIdentifier
          hasChildren
      }
    }
  }
}`

export const GQ_DSM_FOR_NODE_CHILDREN = gql`query DsmForNodeChildren($databaseId: ID!, $hierarchicalGraphId: ID!, $nodeId: ID!)  {
    hierarchicalGraph(databaseIdentifier: $databaseId, hierarchicalGraphIdentifier: $hierarchicalGraphId) {
        node(id: $nodeId) {
            id
            children {
                dependencyMatrix {
                    orderedNodes {
                        id
                        text
                        iconIdentifier
                    }
                    cells {
                        row
                        column
                        value
                    }
                    stronglyConnectedComponents {
                        nodePositions
                    }
                }
            }
        }
    }
}`

export const GQ_GET_SVG = gql`
    query Svg($identifier: ID!) {
        svg(identifier: $identifier)
    }
`;

export const GQ_GRAPH_DATABASES_WITH_HIERARCHICAL_GRAPHS = gql`query GraphDatabasesWithHierarchicalGraphs {
    graphDatabases {
        identifier
        hierarchicalGraphs {
            identifier
        }
    }
}`

export const GQ_HAS_INSTALLED_EXTENSIONS = gql`
    query HasInstalledExtensions {
        serverConfiguration {
            hasInstalledExtensions
        }
    }
`;

export const GQ_AVAILABLE_SERVER_EXTENSIONS = gql`
    query AvailableServerExtensions {
        availableServerExtensions {
            symbolicName
            version
        }
    }`;

export const GQ_REFERENCED_NODES_FOR_AGGREGATED_DEPENDENCY = gql`
    query ReferencedNodesForAggregatedDependencies($databaseId: ID!, $hierarchicalGraphId: ID!, $dependencySourceNodeId: ID!, $dependencyTargetNodeId: ID!, $selectedNodeIds: [ID!]!,  $selectedNodesType: NodeType!) {
        hierarchicalGraph(databaseIdentifier: $databaseId, hierarchicalGraphIdentifier: $hierarchicalGraphId) {
            dependencySetForAggregatedDependency(sourceNodeId: $dependencySourceNodeId, targetNodeId: $dependencyTargetNodeId) {
                size
                referencedNodeIds(selectedNodes: $selectedNodeIds, selectedNodesType: $selectedNodesType, includedPredecessors: true)
            }
            sourcePredecessors: node(id: $dependencySourceNodeId) {
                id
                predecessors {
                    id
                }
            }
            targetPredecessors: node(id: $dependencyTargetNodeId) {
                id
                predecessors {
                    id
                }
            }
        }
    }`