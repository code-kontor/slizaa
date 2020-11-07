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
import ApolloClient from "apollo-client";
import {
    FilterReferencedNodeIds,
    FilterReferencedNodeIdsVariables
} from "../gqlqueries/__generated__/FilterReferencedNodeIds";
import {
    FilterReferencingNodeIds,
    FilterReferencingNodeIdsVariables
} from "../gqlqueries/__generated__/FilterReferencingNodeIds";
import {NodeChildren} from "../gqlqueries/__generated__/NodeChildren";
import {GQ_FILTER_REFERENCED_NODE_IDS, GQ_FILTER_REFERENCING_NODE_IDS} from "../gqlqueries/GqlQueries";
import {NodesToConsider} from "../gqlqueries/query-types";

export function filterByReferencedNodes(
    aApolloClient: ApolloClient<NodeChildren>,
    aDatabaseId: string | undefined,
    aHierarchicalGraphId: string | undefined,
    aNodeIds: string[],
    aNodeIdsToFilter: string[],
    aNodesToConsider: NodesToConsider,
    aIncludePredecessorsInResult: boolean,
    aCallback: (filteredNodeIds: string[]) => void): Promise<{}> {

    // create new result promise
    return new Promise(async (resolve, reject) => {

            //
            if (!aDatabaseId || !aHierarchicalGraphId) {
                resolve();
                return;
            }

            // otherwise try to resolve the children...
            await aApolloClient.query<FilterReferencedNodeIds, FilterReferencedNodeIdsVariables>({
                query: GQ_FILTER_REFERENCED_NODE_IDS,
                variables: {
                    databaseId: aDatabaseId,
                    hierarchicalGraphId: aHierarchicalGraphId,
                    includePredecessorsInResult: aIncludePredecessorsInResult,
                    nodeIds: aNodeIds,
                    nodeIdsToFilter: aNodeIdsToFilter,
                    nodesToConsider: aNodesToConsider,
                }
            })
                .then(result => {
                    // create children
                    if (result.data.hierarchicalGraph &&
                        result.data.hierarchicalGraph.nodes &&
                        result.data.hierarchicalGraph.nodes.filterReferencedNodes &&
                        result.data.hierarchicalGraph.nodes.filterReferencedNodes &&
                        result.data.hierarchicalGraph.nodes.filterReferencedNodes.nodeIds) {

                        aCallback(result.data.hierarchicalGraph.nodes.filterReferencedNodes.nodeIds);
                    }
                    resolve();
                })
                .catch(reason => {
                    reject();
                });
    });
}

export function filterByReferencingNodes(
    aApolloClient: ApolloClient<NodeChildren>,
    aDatabaseId: string | undefined,
    aHierarchicalGraphId: string | undefined,
    aNodeIds: string[],
    aNodeIdsToFilter: string[],
    aNodesToConsider: NodesToConsider,
    aIncludePredecessorsInResult: boolean,
    aCallback: (filteredNodeIds: string[]) => void): Promise<{}> {

    // create new result promise
    return new Promise(async (resolve, reject) => {

        //
        if (!aDatabaseId || !aHierarchicalGraphId) {
            resolve();
            return;
        }

        // otherwise try to resolve the children...
        await aApolloClient.query<FilterReferencingNodeIds, FilterReferencingNodeIdsVariables>({
            query: GQ_FILTER_REFERENCING_NODE_IDS,
            variables: {
                databaseId: aDatabaseId,
                hierarchicalGraphId: aHierarchicalGraphId,
                includePredecessorsInResult: aIncludePredecessorsInResult,
                nodeIds: aNodeIds,
                nodeIdsToFilter: aNodeIdsToFilter,
                nodesToConsider: aNodesToConsider,
            }
        })
            .then(result => {
                // create children
                if (result.data.hierarchicalGraph &&
                    result.data.hierarchicalGraph.nodes &&
                    result.data.hierarchicalGraph.nodes.filterReferencingNodes &&
                    result.data.hierarchicalGraph.nodes.filterReferencingNodes &&
                    result.data.hierarchicalGraph.nodes.filterReferencingNodes.nodeIds) {

                    aCallback(result.data.hierarchicalGraph.nodes.filterReferencingNodes.nodeIds);
                }
                resolve();
            })
            .catch(reason => {
                reject();
            });
    });
}