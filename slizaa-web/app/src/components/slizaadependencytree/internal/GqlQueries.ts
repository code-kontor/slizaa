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
import gql from 'graphql-tag'

export const GQ_REFERENCED_NODES_FOR_AGGREGATED_DEPENDENCY = gql`
    query ReferencedNodesForAggregatedDependencies($databaseId: ID!, $hierarchicalGraphId: ID!, $dependencySourceNodeId: ID!, $dependencyTargetNodeId: ID!, $selectedNodeIds: [ID!]!,  $selectedNodesType: NodeType!) {
        hierarchicalGraph(databaseIdentifier: $databaseId, hierarchicalGraphIdentifier: $hierarchicalGraphId) {
            dependencySetForAggregatedDependency(sourceNodeId: $dependencySourceNodeId, targetNodeId: $dependencyTargetNodeId) {
                referencedNodeIds(selectedNodes: $selectedNodeIds, selectedNodesType: $selectedNodesType, includedPredecessors: true)
            }
            sourcePredecessors: node(id: $dependencySourceNodeId) {
              predecessors {
                id
              }
            }
            targetPredecessors: node(id: $dependencyTargetNodeId) {
              predecessors {
                id
              }
          }
        }
    }`