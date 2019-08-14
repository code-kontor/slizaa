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
import {Spin} from "antd";
import ApolloClient from 'apollo-client';
import * as React from "react";
import {Query} from "react-apollo";
import {SlizaaIcon} from 'src/components/slizaaicon';
import {ISlizaaNode} from "../../../model/ISlizaaNode";
import {NodeType} from "../../../model/NodeType";
import {SlizaaNode} from "../../../model/SlizaaNode";
import {fetchChildrenFilterByDependencySet} from "../../../model/SlizaaNodeChildrenResolver";
import STree from "../../stree/STree";
import {
    ReferencedNodesForAggregatedDependencies,
    ReferencedNodesForAggregatedDependenciesVariables
} from "./__generated__/ReferencedNodesForAggregatedDependencies";
import {GQ_REFERENCED_NODES_FOR_AGGREGATED_DEPENDENCY} from "./GqlQueries";
import {ISlizaaDependencyTreeProps} from "./ISlizaaDependencyTreeProps";
import {ISlizaaDependencyTreeState} from "./ISlizaaDependencyTreeState";

import './SlizaaDependencyTree.css';

export class SlizaaDependencyTree extends React.Component<ISlizaaDependencyTreeProps, ISlizaaDependencyTreeState> {

    constructor(props: ISlizaaDependencyTreeProps) {
        super(props);

        this.state = {
            expandedSourceNodeIds: [],
            expandedTargetNodeIds: [],
            selectedNodeIds: props.selectedNodeIds,
            selectedNodesType: props.selectedNodesType,
            sourceNode: SlizaaNode.createRoot("Root", "default"),
            targetNode: SlizaaNode.createRoot("Root", "default"),
        };
    }

    public componentWillReceiveProps(nextProps: ISlizaaDependencyTreeProps) {

        if (nextProps.sourceNodeId !== this.props.sourceNodeId ||
            nextProps.selectedNodeIds !== this.props.selectedNodeIds ||
            nextProps.selectedNodesType !== this.props.selectedNodesType) {

            this.setState({
                selectedNodeIds: nextProps.selectedNodeIds,
                selectedNodesType: nextProps.selectedNodesType,
                sourceNode: SlizaaNode.createRoot("Root", "default"),
                targetNode: SlizaaNode.createRoot("Root", "default"),
            })
        }
    }

    public render() {

        const selectedSourceNodeIds = this.state.selectedNodesType === NodeType.SOURCE ? this.state.selectedNodeIds : [];
        const selectedTargetNodeIds = this.state.selectedNodesType === NodeType.TARGET ? this.state.selectedNodeIds : [];

        const variables = {
            databaseId: this.props.databaseId,
            dependencySourceNodeId: this.props.sourceNodeId,
            dependencyTargetNodeId: this.props.targetNodeId,
            hierarchicalGraphId: this.props.hierarchicalGraphId,
            selectedNodeIds: this.state.selectedNodeIds,
            selectedNodesType: this.state.selectedNodesType
        }

        return <Query<ReferencedNodesForAggregatedDependencies, ReferencedNodesForAggregatedDependenciesVariables>
            query={GQ_REFERENCED_NODES_FOR_AGGREGATED_DEPENDENCY}
            variables={variables}>

            {({loading, data, error}) => {

                if (loading) {
                    return <Spin size="large"/>;
                }

                if (error) {
                    return <h1>{error.message}</h1>
                }

                if (!data ||
                    !data.hierarchicalGraph ||
                    !data.hierarchicalGraph.dependencySetForAggregatedDependency ||
                    !data.hierarchicalGraph.dependencySetForAggregatedDependency.referencedNodeIds) {
                    return <div>UNDEFINED - TODO</div>
                }

                const markedSourceNodeIds = this.state.selectedNodeIds.length === 0 || this.state.selectedNodesType === NodeType.SOURCE ? undefined : data.hierarchicalGraph.dependencySetForAggregatedDependency.referencedNodeIds;
                const markedTargetNodeIds = this.state.selectedNodeIds.length === 0 || this.state.selectedNodesType === NodeType.TARGET ? undefined : data.hierarchicalGraph.dependencySetForAggregatedDependency.referencedNodeIds;

                return <div className="slizaa-dependency-tree-container">
                    <div>
                        <STree
                            rootNode={this.state.sourceNode}
                            onExpand={this.onSourceExpand}
                            onSelect={this.onSourceSelect}
                            expandedKeys={this.state.expandedSourceNodeIds}
                            selectedKeys={selectedSourceNodeIds}
                            markedKeys={markedSourceNodeIds}
                            loadData={this.loadChildrenFilteredByDependencySource(this.props.client, this.props.sourceNodeId, this.props.targetNodeId)}
                            fetchIcon={this.fetchIcon}
                        />
                    </div>
                    <div>
                        <STree
                            rootNode={this.state.targetNode}
                            onExpand={this.onTargetExpand}
                            onSelect={this.onTargetSelect}
                            expandedKeys={this.state.expandedTargetNodeIds}
                            selectedKeys={selectedTargetNodeIds}
                            markedKeys={markedTargetNodeIds}
                            loadData={this.loadChildrenFilteredByDependencyTarget(this.props.client, this.props.sourceNodeId, this.props.targetNodeId)}
                            fetchIcon={this.fetchIcon}
                        />
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

    private onSourceSelect = (selectedItems: string[]): void => {
        this.setState({
            selectedNodeIds: selectedItems,
            selectedNodesType: NodeType.SOURCE
        })
    }

    private onSourceExpand = (expandedItems: string[]): void => {
        this.setState({
            expandedSourceNodeIds: expandedItems,
        })
    }

    private onTargetSelect = (selectedItems: string[]): void => {
        this.setState({
            selectedNodeIds: selectedItems,
            selectedNodesType: NodeType.TARGET
        })
    }

    private onTargetExpand = (expandedItems: string[]): void => {
        this.setState({
            expandedTargetNodeIds: expandedItems,
        })
    }
}