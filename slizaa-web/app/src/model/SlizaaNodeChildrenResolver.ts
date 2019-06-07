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
import { NodeChildren, NodeChildrenVariables } from './__generated__/NodeChildren';
import { NodeChildrenQuery } from './GqlQueries';
import { SlizaaNode } from "./SlizaaNode";

export function fetchChildren(parentNode: SlizaaNode, apolloClient: ApolloClient<any>, aDatabaseId: string, aHierarchicalGraphId: string, callback: () => void): Promise<{}> {

    return new Promise(async (resolve, reject) => {

        // return if children already have been resolved
        if (parentNode.internalChildren) {
            resolve();
            return;
        }

        const key: string = parentNode.key;

        await apolloClient.query<NodeChildren, NodeChildrenVariables>({
            query: NodeChildrenQuery,
            variables: {
                databaseId: aDatabaseId,
                hierarchicalGraphId: aHierarchicalGraphId,
                nodeId: key
            }
        })
            .then(result => {

                if (result.data.hierarchicalGraph && result.data.hierarchicalGraph.node) {
                    const resultChildren = result.data.hierarchicalGraph.node.children.nodes
                    parentNode.internalChildren = new Array<SlizaaNode>(resultChildren.length);

                    for (let i = 0; i < resultChildren.length; i++) {
                        parentNode.internalChildren[i] = SlizaaNode.createNode(
                            resultChildren[i].id,
                            resultChildren[i].text,
                            resultChildren[i].iconIdentifier,
                            resultChildren[i].hasChildren,
                        )
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


