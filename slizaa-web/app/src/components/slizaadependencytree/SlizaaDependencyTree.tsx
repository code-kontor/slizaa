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
import * as React from "react";
import {CSSProperties} from "react";
import {Query} from "react-apollo";
import {SlizaaIcon} from 'src/components/slizaaicon';
import {ISlizaaNode} from "../../model/ISlizaaNode";
import {NodeType} from "../../model/NodeType";
import {SlizaaNode} from "../../model/SlizaaNode";
import {fetchChildrenFilterByDependencySet} from "../../model/SlizaaNodeChildrenResolver";
import STree from "../stree/STree";
import {ISlizaaDependencyTreeProps} from "./ISlizaaDependencyTreeProps";
import {ISlizaaDependencyTreeState} from "./ISlizaaDependencyTreeState";

import {
    ReferencedNodesForAggregatedDependencies,
    ReferencedNodesForAggregatedDependenciesVariables
} from "../../gqlqueries/__generated__/ReferencedNodesForAggregatedDependencies";
import {GQ_REFERENCED_NODES_FOR_AGGREGATED_DEPENDENCY} from "../../gqlqueries/GqlQueries";
import './SlizaaDependencyTree.css';

export class SlizaaDependencyTree extends React.Component<ISlizaaDependencyTreeProps, ISlizaaDependencyTreeState> {

    constructor(props: ISlizaaDependencyTreeProps) {
        super(props);

        this.state = {
            expandedSourceNodeIds: [],
            expandedTargetNodeIds: [],
            selectedSourceNodeIds: props.selectedSourceNodeIds,
            selectedTargetNodeIds: props.selectedTargetNodeIds,
            sourceNode: SlizaaNode.createRoot("Root", "default"),
            targetNode: SlizaaNode.createRoot("Root", "default"),
        };
    }

    public render() {

        const variables : ReferencedNodesForAggregatedDependenciesVariables = {
            databaseId: this.props.databaseId,
            dependencySourceNodeId: this.props.sourceNodeId,
            dependencyTargetNodeId: this.props.targetNodeId,
            hierarchicalGraphId: this.props.hierarchicalGraphId,
            selectedSourceNodeIds: this.state.selectedSourceNodeIds,
            selectedTargetNodeIds: this.state.selectedTargetNodeIds,
        }

        return <Query<ReferencedNodesForAggregatedDependencies, ReferencedNodesForAggregatedDependenciesVariables>
            query={GQ_REFERENCED_NODES_FOR_AGGREGATED_DEPENDENCY}
            fetchPolicy={"no-cache"}
            pollInterval={1000}
            variables={variables}>

            {({loading, data, error, refetch}) => {

                if (error) {
                    return <h1>{error.message}</h1>
                }

                // TODO: merge with expanded IDs
                const sourcePredecessors: string[] = !data || !data.hierarchicalGraph || data.hierarchicalGraph.sourcePredecessors == null ? [] : data.hierarchicalGraph.sourcePredecessors.predecessors.map((p) => p.id);
                const targetPredecessors: string[] = !data || !data.hierarchicalGraph || data.hierarchicalGraph.targetPredecessors == null ? [] : data.hierarchicalGraph.targetPredecessors.predecessors.map((p) => p.id);

                const combinedExpandedSourceNodeIds = sourcePredecessors.concat(this.state.expandedSourceNodeIds);
                const combinedExpandedTargetNodeIds = targetPredecessors.concat(this.state.expandedTargetNodeIds);

                //
                const stylesNoEvents: CSSProperties = {pointerEvents: loading ? "none" : "unset"};
                const stylesCursorWait: CSSProperties = {cursor: loading ? "wait" : "unset"};

                const hasDependencies = data &&
                    data.hierarchicalGraph &&
                    data.hierarchicalGraph.dependencySetForAggregatedDependency &&
                    data.hierarchicalGraph.dependencySetForAggregatedDependency.size !== 0;

                const elementLeft = hasDependencies ?
                    <STree
                        rootNode={this.state.sourceNode}
                        onExpand={this.onSourceExpand}
                        onSelect={this.onSourceSelect}
                        expandedKeys={combinedExpandedSourceNodeIds}
                        selectedKeys={this.state.selectedSourceNodeIds}
                        markedKeys={this.state.selectedSourceNodeIds}
                        loadData={this.loadChildrenFilteredByDependencySource(this.props.client, this.props.sourceNodeId, this.props.targetNodeId)}
                        fetchIcon={this.fetchIcon}
                    /> :
                    <div/>

                const elementRight = hasDependencies ?
                    <STree
                        rootNode={this.state.targetNode}
                        onExpand={this.onTargetExpand}
                        onSelect={this.onTargetSelect}
                        expandedKeys={combinedExpandedTargetNodeIds}
                        selectedKeys={this.state.selectedTargetNodeIds}
                        markedKeys={this.state.selectedTargetNodeIds}
                        loadData={this.loadChildrenFilteredByDependencyTarget(this.props.client, this.props.sourceNodeId, this.props.targetNodeId)}
                        fetchIcon={this.fetchIcon}
                    /> :
                    <div/>

                return <div className="slizaa-dependency-tree" style={stylesCursorWait}>
                    <div className="slizaa-dependency-tree-container" style={stylesNoEvents}>
                        {elementLeft}
                    </div>
                    <div className="slizaa-dependency-tree-container" style={stylesNoEvents}>
                        {elementRight}
                    </div>
                </div>
            }}
        </Query>
    }

    private fetchIcon = (item: ISlizaaNode): React.ReactNode => {
        return <SlizaaIcon iconId={item.iconId}/>
    }

    private loadChildrenFilteredByDependencySource = (client: ApolloClient<any>, dependencySourceNodeId: string, dependencyTargetNodeId: string): (parent: SlizaaNode, callback: () => void) => Promise<{}> => {
        return (p: SlizaaNode, c: () => void) => fetchChildrenFilterByDependencySet(client, p, NodeType.SOURCE, dependencySourceNodeId, dependencyTargetNodeId, this.props.databaseId, this.props.hierarchicalGraphId, c);
    }

    private loadChildrenFilteredByDependencyTarget = (client: ApolloClient<any>, dependencySourceNodeId: string, dependencyTargetNodeId: string): (parent: SlizaaNode, callback: () => void) => Promise<{}> => {
        return (p: SlizaaNode, c: () => void) => fetchChildrenFilterByDependencySet(client, p, NodeType.TARGET, dependencySourceNodeId, dependencyTargetNodeId, this.props.databaseId, this.props.hierarchicalGraphId, c);
    }

    private onSourceSelect = (selectedNodes: ISlizaaNode[]): void => {
        this.setState({
            selectedSourceNodeIds: selectedNodes.map((node) => node.key),
        })
    }

    private onSourceExpand = (expandedItems: string[]): void => {
        this.setState({
            expandedSourceNodeIds: expandedItems,
        })
    }

    private onTargetSelect = (selectedNodes: ISlizaaNode[]): void => {
        this.setState({
           selectedTargetNodeIds: selectedNodes.map((node) => node.key),
        })
    }

    private onTargetExpand = (expandedItems: string[]): void => {
        this.setState({
            expandedTargetNodeIds: expandedItems,
        })
    }
}