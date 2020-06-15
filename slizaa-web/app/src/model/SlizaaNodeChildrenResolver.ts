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

import ApolloClient from 'apollo-client';
import { NodeType } from 'src/gqlqueries/query-types';
import {NodeChildren, NodeChildrenVariables} from "../gqlqueries/__generated__/NodeChildren";
import {
    NodeChildrenFilteredByDependencySet,
    NodeChildrenFilteredByDependencySetVariables
} from "../gqlqueries/__generated__/NodeChildrenFilteredByDependencySet";
import {NodeChildrenFilteredByDependencySetQuery, NodeChildrenQuery} from "../gqlqueries/GqlQueries";
import { SlizaaNode } from "./SlizaaNode";

export function fetchChildren( aApolloClient: ApolloClient<NodeChildren>, aParentNode: SlizaaNode, aDatabaseId: string, aHierarchicalGraphId: string, callback: () => void): Promise<{}> {

    // create new result promise
    return new Promise(async (resolve, reject) => {

        // return if children already have been resolved...
        if (aParentNode.internalChildren) {
            resolve();
            return;
        }

        // otherwise try to resolve the children...
        await aApolloClient.query<NodeChildren, NodeChildrenVariables>({
            query: NodeChildrenQuery,
            variables: {
                databaseId: aDatabaseId,
                hierarchicalGraphId: aHierarchicalGraphId,
                nodeId: aParentNode.key
            }
        })
        .then(result => {
            // create children 
            if (result.data.hierarchicalGraph && result.data.hierarchicalGraph.node) {
                const resultChildren = result.data.hierarchicalGraph.node.children.nodes;
                aParentNode.internalChildren = new Array<SlizaaNode>(resultChildren.length);
                for (let i = 0; i < resultChildren.length; i++) {
                    aParentNode.internalChildren[i] = SlizaaNode.createNode(resultChildren[i].id, resultChildren[i].text, resultChildren[i].iconIdentifier, resultChildren[i].hasChildren);
                }
            }
            callback();
            resolve();
        })
        .catch(reason => {
            reject();
        });
    });
}

export function fetchChildrenFilterByDependencySet( aApolloClient: ApolloClient<NodeChildren>, aParentNode: SlizaaNode, aNodeType: NodeType, dependencySourceNodeId: string, dependencyTargetNodeId: string, aDatabaseId: string | undefined, aHierarchicalGraphId: string | undefined, callback: () => void): Promise<{}> {

    // create new result promise
    return new Promise(async (resolve, reject) => {

        if (!aDatabaseId || !aHierarchicalGraphId) {
            resolve();
            return;
        }

        // return if children already have been resolved...
        if (aParentNode.internalChildren) {
            resolve();
            return;
        }

        // otherwise try to resolve the children...
        await aApolloClient.query<NodeChildrenFilteredByDependencySet, NodeChildrenFilteredByDependencySetVariables>({
            query: NodeChildrenFilteredByDependencySetQuery,
            variables: {
                databaseId: aDatabaseId,
                hierarchicalGraphId: aHierarchicalGraphId,
                nodeId: aParentNode.key,
                nodeType: aNodeType,
                sourceNodeId: dependencySourceNodeId,
                targetNodeId: dependencyTargetNodeId
            }
        })
        .then(result => {
            // create children 
            if (result.data.hierarchicalGraph && 
                result.data.hierarchicalGraph.dependencySetForAggregatedDependency && 
                result.data.hierarchicalGraph.dependencySetForAggregatedDependency.filteredChildren) {
                const resultChildren = result.data.hierarchicalGraph.dependencySetForAggregatedDependency.filteredChildren;
                aParentNode.internalChildren = new Array<SlizaaNode>(resultChildren.length);
                for (let i = 0; i < resultChildren.length; i++) {
                    aParentNode.internalChildren[i] = SlizaaNode.createNode(resultChildren[i].id, resultChildren[i].text, resultChildren[i].iconIdentifier, resultChildren[i].hasChildren);
                }
            }
            callback();
            resolve();
        })
        .catch(reason => {
            reject();
        });
    });
}

// export function fetchChildrenFilterByDependencySet( apolloClient: ApolloClient<any>, aParentNode: SlizaaNode, aDatabaseId: string, aHierarchicalGraphId: string, callback: () => void): Promise<{}> {
//     return queryChildren(aParentNode, (slizaaNode) => apolloClient.query<NodeChildrenFilteredByDependencySet, NodeChildrenFilteredByDependencySetVariables>({
//         query: NodeChildrenFilteredByDependencySetQuery,
//         variables: {
//             databaseId: aDatabaseId,
//             hierarchicalGraphId: aHierarchicalGraphId,
//             nodeId: aParentNode.key,
//             nodeType: NodeType.SOURCE,
//             sourceNodeId: "1361",
//             targetNodeId: "19440"
//         }
//     }), callback);
// }

// /**
//  * 
//  * @param parentNode the parent node to request the children for
//  * @param query the query used to request the children
//  * @param callback a callback to execute
//  */
// function queryChildren(parentNode: SlizaaNode, query: (parentNode: SlizaaNode) => Promise<ApolloQueryResult<any>>, callback: () => void): Promise<{}> {

//     return new Promise(async (resolve, reject) => {

//         // return if children already have been resolved...
//         if (parentNode.internalChildren) {
//             resolve();
//             return;
//         }

//         // otherwise try to resolve the children...
//         await query(parentNode)
//             .then(result => {
//                 if (result.data.hierarchicalGraph && result.data.hierarchicalGraph.node) {
//                     const resultChildren = result.data.hierarchicalGraph.node.children.nodes;
//                     parentNode.internalChildren = new Array<SlizaaNode>(resultChildren.length);
//                     for (let i = 0; i < resultChildren.length; i++) {
//                         parentNode.internalChildren[i] = SlizaaNode.createNode(resultChildren[i].id, resultChildren[i].text, resultChildren[i].iconIdentifier, resultChildren[i].hasChildren);
//                     }
//                 }
//                 callback();
//                 resolve();
//             })
//             .catch(reason => {
//                 reject();
//             });
//     });
// }

