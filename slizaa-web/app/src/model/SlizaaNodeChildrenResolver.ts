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
import {NodeType} from 'src/gqlqueries/query-types';
import {
    ChildrenFilteredByReferencedNodes,
    ChildrenFilteredByReferencedNodesVariables
} from "../gqlqueries/__generated__/ChildrenFilteredByReferencedNodes";
import {
    ChildrenFilteredByReferencingNodes,
    ChildrenFilteredByReferencingNodesVariables
} from "../gqlqueries/__generated__/ChildrenFilteredByReferencingNodes";
import {NodeChildren, NodeChildrenVariables} from "../gqlqueries/__generated__/NodeChildren";
import {
    NodeChildrenFilteredByDependencySet,
    NodeChildrenFilteredByDependencySetVariables
} from "../gqlqueries/__generated__/NodeChildrenFilteredByDependencySet";
import {
    GQ_CHILDREN_FILTERED_BY_REFERENCED_NODES,
    GQ_CHILDREN_FILTERED_BY_REFERENCING_NODES,
    NodeChildrenFilteredByDependencySetQuery,
    NodeChildrenQuery
} from "../gqlqueries/GqlQueries";
import {SlizaaNode} from "./internal/SlizaaNode";
import {SlizaaRootNode} from "./internal/SlizaaRootNode";
import {ISlizaaNode} from "./ISlizaaNode";
import {ISlizaaRootNode} from "./ISlizaaRootNode";

export function fetchChildren(aApolloClient: ApolloClient<NodeChildren>, aParentNode: ISlizaaNode, aDatabaseId: string, aHierarchicalGraphId: string, callback: () => void): Promise<{}> {

    // create new result promise
    return new Promise(async (resolve, reject) => {

        if (aParentNode instanceof SlizaaNode) {

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
                            aParentNode.internalChildren[i] = createNode(aParentNode.root(), resultChildren[i].id, resultChildren[i].text, resultChildren[i].iconIdentifier, resultChildren[i].hasChildren);
                        }
                    }
                    callback();
                    resolve();
                })
                .catch(reason => {
                    reject();
                });
        }
    });
}

export function fetchChildrenFilterByDependencySet(aApolloClient: ApolloClient<NodeChildren>, aParentNode: ISlizaaNode, aNodeType: NodeType, dependencySourceNodeId: string, dependencyTargetNodeId: string, aDatabaseId: string | undefined, aHierarchicalGraphId: string | undefined, callback: () => void): Promise<{}> {

    // create new result promise
    return new Promise(async (resolve, reject) => {
        if (aParentNode instanceof SlizaaNode) {
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
                            aParentNode.internalChildren[i] = createNode(aParentNode.root(), resultChildren[i].id, resultChildren[i].text, resultChildren[i].iconIdentifier, resultChildren[i].hasChildren);
                        }
                    }
                    callback();
                    resolve();
                })
                .catch(reason => {
                    reject();
                });
        }
    });
}

export function fetchChildrenFilteredByReferencedNodes(
    aApolloClient: ApolloClient<NodeChildren>,
    aDatabaseId: string | undefined,
    aHierarchicalGraphId: string | undefined,
    aParentNode: ISlizaaNode,
    aSelectedReferencedNodeIds: string[],
    callback: () => void): Promise<{}> {

    // create new result promise
    return new Promise(async (resolve, reject) => {

        if (aParentNode instanceof SlizaaNode) {

            //
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
            await aApolloClient.query<ChildrenFilteredByReferencedNodes, ChildrenFilteredByReferencedNodesVariables>({
                query: GQ_CHILDREN_FILTERED_BY_REFERENCED_NODES,
                variables: {
                    databaseId: aDatabaseId,
                    hierarchicalGraphId: aHierarchicalGraphId,
                    parentNodeId: aParentNode.key,
                    selectedReferencedNodeIds: aSelectedReferencedNodeIds
                }
            })
                .then(result => {
                    // create children
                    if (result.data.hierarchicalGraph &&
                        result.data.hierarchicalGraph.node &&
                        result.data.hierarchicalGraph.node.childrenFilteredByReferencedNodes &&
                        result.data.hierarchicalGraph.node.childrenFilteredByReferencedNodes.nodes) {
                        const resultChildren = result.data.hierarchicalGraph.node.childrenFilteredByReferencedNodes.nodes;
                        aParentNode.internalChildren = new Array<SlizaaNode>(resultChildren.length);
                        for (let i = 0; i < resultChildren.length; i++) {
                            aParentNode.internalChildren[i] = createNode(aParentNode.root(), resultChildren[i].id, resultChildren[i].text, resultChildren[i].iconIdentifier, resultChildren[i].hasChildren);
                        }
                    }
                    callback();
                    resolve();
                })
                .catch(reason => {
                    reject();
                });
        }
    });
}

export function fetchChildrenFilteredByReferencingNodes(
    aApolloClient: ApolloClient<NodeChildren>,
    aDatabaseId: string | undefined,
    aHierarchicalGraphId: string | undefined,
    aParentNode: ISlizaaNode,
    aSelectedReferencingNodeIds: string[],
    callback: () => void): Promise<{}> {

    // create new result promise
    return new Promise(async (resolve, reject) => {

        if (aParentNode instanceof SlizaaNode) {

            //
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
            await aApolloClient.query<ChildrenFilteredByReferencingNodes, ChildrenFilteredByReferencingNodesVariables>({
                query: GQ_CHILDREN_FILTERED_BY_REFERENCING_NODES,
                variables: {
                    databaseId: aDatabaseId,
                    hierarchicalGraphId: aHierarchicalGraphId,
                    parentNodeId: aParentNode.key,
                    selectedReferencingNodeIds: aSelectedReferencingNodeIds
                }
            })
                .then(result => {
                    // create children
                    if (result.data.hierarchicalGraph &&
                        result.data.hierarchicalGraph.node &&
                        result.data.hierarchicalGraph.node.childrenFilteredByReferencingNodes &&
                        result.data.hierarchicalGraph.node.childrenFilteredByReferencingNodes.nodes) {
                        const resultChildren = result.data.hierarchicalGraph.node.childrenFilteredByReferencingNodes.nodes;
                        aParentNode.internalChildren = new Array<SlizaaNode>(resultChildren.length);
                        for (let i = 0; i < resultChildren.length; i++) {
                            aParentNode.internalChildren[i] = createNode(aParentNode.root(), resultChildren[i].id, resultChildren[i].text, resultChildren[i].iconIdentifier, resultChildren[i].hasChildren);
                        }
                    }
                    callback();
                    resolve();
                })
                .catch(reason => {
                    reject();
                });
        }
    });
}

function createNode(root: ISlizaaRootNode, id: string, title: string, iconId: string, hasChildren: boolean): SlizaaNode {
    const result = new SlizaaNode(id, title, iconId, hasChildren);
    if (root instanceof SlizaaRootNode) {
        result.setRoot(root);
    }
    return result;
}

