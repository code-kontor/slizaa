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
                orderedAdjacencyMatrix {
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

export const GQ_REFERENCED_NODES_FOR_AGGREGATED_DEPENDENCY = gql`
    query ReferencedNodesForAggregatedDependencies(
        $databaseId: ID!,
        $hierarchicalGraphId: ID!,
        $dependencySourceNodeId: ID!,
        $dependencyTargetNodeId: ID!,
        $selectedSourceNodeIds: [ID!]!,
        $selectedTargetNodeIds: [ID!]!,
    ) {
        hierarchicalGraph(databaseIdentifier: $databaseId, hierarchicalGraphIdentifier: $hierarchicalGraphId) {
            dependencySetForAggregatedDependency(sourceNodeId: $dependencySourceNodeId, targetNodeId: $dependencyTargetNodeId) {
                size
                filteredDependencies(nodeSelection: [ {selectedNodeIds: $selectedSourceNodeIds, selectedNodesType: SOURCE}, {selectedNodeIds: $selectedTargetNodeIds, selectedNodesType: TARGET}]) {
                    sourceNodeIds: referencedNodeIds(nodeType: SOURCE, includedPredecessors: true)
                    targetNodeIds: referencedNodeIds(nodeType: TARGET, includedPredecessors: true)
                }
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

export const GQ_CORE_DEPENDENCIES_FOR_AGGREGATED_DEPENDENCY = gql`
    query CoreDependenciesForAggregatedDependencies(
        $databaseId: ID!,
        $hierarchicalGraphId: ID!,
        $dependencySourceNodeId: ID!,
        $dependencyTargetNodeId: ID!,
        $pageSize: Int!,
        $pageNumber: Int!,
        $nodeSelections: [NodeSelection!]!
    ) {
        hierarchicalGraph(databaseIdentifier: $databaseId, hierarchicalGraphIdentifier: $hierarchicalGraphId) {
            dependencySetForAggregatedDependency(sourceNodeId: $dependencySourceNodeId, targetNodeId: $dependencyTargetNodeId) {
                filteredDependencies(nodeSelection: $nodeSelections) {

                    size
                    dependencyPage(pageNumber: $pageNumber, pageSize: $pageSize) {
                        pageInfo {
                            pageNumber
                            maxPages
                            pageSize
                            totalCount
                        }
                        dependencies {
                            id
                            isProxyDependency
                            type
                            weight
                            sourceNode {
                                id
                                text
                                iconIdentifier
                            }
                            targetNode {
                                id
                                text
                                iconIdentifier
                            }
                        }
                    }
                }
            }
        }
    }`

export const GQ_RESOLVED_PROXY_DEPENDENCY = gql`
    query ResolvedProxyDependency(
        $databaseId: ID!,
        $hierarchicalGraphId: ID!,
        $proxyDependencyId: ID!,
    ) {
        hierarchicalGraph(databaseIdentifier: $databaseId, hierarchicalGraphIdentifier: $hierarchicalGraphId) {
            dependency(id: $proxyDependencyId) {
                id
                resolvedDependencies {
                    id
                    type
                    weight
                    sourceNode {
                        id
                        text
                        iconIdentifier
                    }
                    targetNode {
                        id
                        text
                        iconIdentifier
                    }
                }
            }
        }
    }`

export const GQ_FILTER_REFERENCED_NODES = gql`
    query FilterReferencedNodes (
        $databaseId: ID!,
        $hierarchicalGraphId: ID!,
        $nodeIds: [ID!]!,
        $nodeIdsToFilter: [ID!]!,
        $nodesToConsider: NodesToConsider!,
        $includePredecessorsInResult: Boolean!
    ) {
  hierarchicalGraph(databaseIdentifier: $databaseId, hierarchicalGraphIdentifier: $hierarchicalGraphId) {
    nodes(ids: $nodeIds) {
      filterReferencedNodes(nodeIds: $nodeIdsToFilter, nodesToConsider: $nodesToConsider, includePredecessorsInResult: $includePredecessorsInResult) {
        nodeIds
      }
      filterReferencingNodes(nodeIds: ["126628", "126629"], nodesToConsider: SELF_AND_CHILDREN, includePredecessorsInResult: true) {
        nodeIds
      }
    }
  }
}
`

export const GQ_FILTER_REFERENCING_NODES = gql`
    query FilterReferencingNodes (
        $databaseId: ID!,
        $hierarchicalGraphId: ID!,
        $nodeIds: [ID!]!,
        $nodeIdsToFilter: [ID!]!,
        $nodesToConsider: NodesToConsider!,
        $includePredecessorsInResult: Boolean!
    ) {
        hierarchicalGraph(databaseIdentifier: $databaseId, hierarchicalGraphIdentifier: $hierarchicalGraphId) {
            nodes(ids: $nodeIds) {
                filterReferencingNodes(nodeIds: $nodeIdsToFilter, nodesToConsider: $nodesToConsider, includePredecessorsInResult: $includePredecessorsInResult) {
                    nodeIds
                }
            }
        }
    }
`

export const GQ_CHILDREN_FILTERED_BY_REFERENCED_NODES = gql`
    query ChildrenFilteredByReferencedNodes (
        $databaseId: ID!,
        $hierarchicalGraphId: ID!,
        $parentNodeId: ID!,
        $selectedReferencedNodeIds: [ID!]!,
    ) {
        hierarchicalGraph(databaseIdentifier: $databaseId, hierarchicalGraphIdentifier: $hierarchicalGraphId) {
            node(id: $parentNodeId) {
                id
                childrenFilteredByReferencedNodes(referencedNodeIds: $selectedReferencedNodeIds) {
                    nodes {
                        id
                        text
                        iconIdentifier
                        hasChildren
                    }
                }
            }
        }
    }
`

export const GQ_CHILDREN_FILTERED_BY_REFERENCING_NODES = gql`
    query ChildrenFilteredByReferencingNodes (
        $databaseId: ID!,
        $hierarchicalGraphId: ID!,
        $parentNodeId: ID!,
        $selectedReferencingNodeIds: [ID!]!,
    ) {
        hierarchicalGraph(databaseIdentifier: $databaseId, hierarchicalGraphIdentifier: $hierarchicalGraphId) {
            node(id: $parentNodeId) {
                id
                childrenFilteredByReferencingNodes(referencingNodeIds: $selectedReferencingNodeIds) {
                    nodes {
                        id
                        text
                        iconIdentifier
                        hasChildren
                    }
                }
            }
        }
    }
`