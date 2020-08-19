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
import {Menu, Spin} from 'antd';
import {ClickParam} from "antd/lib/menu";
import ApolloClient from 'apollo-client';
import * as React from 'react';
import {ApolloConsumer, Query} from 'react-apollo';
import {HierarchicalGraphTree} from 'src/components/hierarchicalgraphtree';
import {EmptyIcon} from "../../components/card/CardIcons";
import {SlizaaDependencyViewer} from "../../components/slizaadependencyviewer/SlizaaDependencyViewer";
import {
    DsmForNodeChildren,
    DsmForNodeChildren_hierarchicalGraph_node_children_orderedAdjacencyMatrix,
    DsmForNodeChildrenVariables
} from "../../gqlqueries/__generated__/DsmForNodeChildren";
import {GQ_DSM_FOR_NODE_CHILDREN} from "../../gqlqueries/GqlQueries";
import {ISlizaaNode} from "../../model/ISlizaaNode";
import {NodeType} from "../../model/NodeType";
import {Slizaa21SplitView} from "../fwk/Slizaa21SplitView";
import './DependenciesView.css';
import {DependencyGraphPart} from "./dependencyGraphPart";
import {DsmPart} from "./dsmPart/DsmPart";
import {IDependenciesViewProps} from "./IDependenciesViewProps";
import {IDependenciesViewState} from "./IDependenciesViewState";
import {IDependencySelection} from "./IDependencyViewModel";


export class DependenciesView extends React.Component<IDependenciesViewProps, IDependenciesViewState> {

    constructor(props: IDependenciesViewProps) {
        super(props);

        this.state = {
            databaseId: props.databaseId,
            hierarchicalGraphId: props.hierarchicalGraphId,
            layout: {
                dsmSetting: {
                    horizontalSideMarkerHeight: 10,
                    verticalSideMarkerWidth: 150
                },
                selectedDependencyVisualization: DependencyVisualization.DependencyStructureMatrix,
            },
            mainTreeNodeSelection: {
                expandedNodeIds: ["-1"],
                selectedNodeIds: [],
            },
        }
    }

    public render() {

        return (
            <ApolloConsumer>
                {cl => (
                    <Slizaa21SplitView
                        id="dependencyViewMain"
                        topLeft={{
                            allowOverflow: true,
                            element: this.hierarchicalGraphCard(cl),
                            title: "Hierarchical Graph",

                        }}
                        topRight={{
                            allowOverflow: true,
                            element: this.dependenciesOverviewCard(cl),
                            overlayMenuFunc: this.dependenciesOverviewMenu,
                            title: "Dependency Overview",
                        }}
                        bottom={{
                            element: this.dependencyDetailsCard(cl),
                            title: "Dependencies Details",
                        }}
                    />)
                }
            </ApolloConsumer>
        );
    }

    private dependenciesOverviewMenu = (): React.ReactElement => {
        return (
            <Menu onClick={this.handleMenuClick}>
                <Menu.Item key={DependencyVisualization.DependencyStructureMatrix.toString()}>
                    <EmptyIcon/>
                    Dependency Structure Matrix
                </Menu.Item>
                <Menu.Item key={DependencyVisualization.DependencyGraph.toString()}>
                    <EmptyIcon/>
                    Dependency Graph
                </Menu.Item>
            </Menu>
        );
    }

    private hierarchicalGraphCard(cl: ApolloClient<any>): React.ReactElement {

        return <HierarchicalGraphTree
            key={this.state.databaseId + "-" + this.state.hierarchicalGraphId}
            client={cl}
            databaseId={this.state.databaseId}
            hierarchicalGraphId={this.state.hierarchicalGraphId}
            onSelect={this.onMainTreeSelect}
            onExpand={this.onMainTreeExpand}
            expandedKeys={this.state.mainTreeNodeSelection.expandedNodeIds}
            checkedKeys={this.state.mainTreeNodeSelection.selectedNodeIds}/>
    }

    private handleMenuClick = (param: ClickParam): void => {

        if (param.key === DependencyVisualization.DependencyStructureMatrix.toString()) {
            this.setState({
                layout: {
                    ...this.state.layout,
                    selectedDependencyVisualization: DependencyVisualization.DependencyStructureMatrix
                }
            })
        } else if (param.key === DependencyVisualization.DependencyGraph.toString()) {
            this.setState({
                layout: {
                    ...this.state.layout,
                    selectedDependencyVisualization: DependencyVisualization.DependencyGraph
                }
            })
        }
    }

    private dependenciesOverviewCard(cl: ApolloClient<any>): React.ReactNode {

        if (this.state.layout.selectedDependencyVisualization === DependencyVisualization.DependencyGraph) {
            return this.dependencyGraph(cl);
        }

        if (this.state.layout.selectedDependencyVisualization === DependencyVisualization.DependencyStructureMatrix) {
            return this.dependencyStructureMatrixPart(cl);
        }

        return null;
    }

    private dependencyDetailsCard(client: ApolloClient<any>): React.ReactElement {

        return this.state.mainDependencySelection === undefined ?

            // return empty div if selected dependency is undefined
            <div/> :

            // else retunr the dependency viewer
            <SlizaaDependencyViewer
                key={this.state.mainDependencySelection.sourceNode.id + "-" + this.state.mainDependencySelection.targetNode.id}
                client={client}
                databaseId={this.state.databaseId}
                hierarchicalGraphId={this.state.hierarchicalGraphId}
                sourceNodeId={this.state.mainDependencySelection.sourceNode.id}
                targetNodeId={this.state.mainDependencySelection.targetNode.id}
            />
    }

    // @ts-ignore
    private dependencyGraph(client: ApolloClient<any>): React.ReactNode {

        return this.queryAndConsume(client, this.state.databaseId, this.state.hierarchicalGraphId, (matrix) => {

            if (matrix === undefined) {
                return <h1>undefined</h1>
            }

            // get  the data
            const {orderedNodes, cells, stronglyConnectedComponents} = matrix;

            return <DependencyGraphPart
                orderedNodes={orderedNodes}
                dependencies={cells}
                stronglyConnectedComponents={stronglyConnectedComponents}
                dependencySelection={this.state.mainDependencySelection}
            />
        })
    }

    /**
     * Creates the dependencies overview component.
     */
    // @ts-ignore
    private dependencyStructureMatrixPart(client: ApolloClient<any>): React.ReactNode {

        return this.queryAndConsume(client, this.state.databaseId, this.state.hierarchicalGraphId, (matrix) => {

            // get  the data
            const {orderedNodes, cells, stronglyConnectedComponents} = matrix;

            return <DsmPart
                orderedNodes={orderedNodes}
                dependencies={cells}
                stronglyConnectedComponents={stronglyConnectedComponents}
                horizontalBoxSize={35}
                verticalBoxSize={35}
                horizontalSideMarkerHeight={this.state.layout.dsmSetting.horizontalSideMarkerHeight}
                verticalSideMarkerWidth={this.state.layout.dsmSetting.verticalSideMarkerWidth}
                onSideMarkerResize={this.onSideMarkerResize}
                onSelect={this.onSelectDependency}
                dependencySelection={this.state.mainDependencySelection}
            />
        })
    }

    private queryAndConsume(client: ApolloClient<any>, aDatabaseId: string | undefined, aHierarchicalGraphId: string | undefined, consumer: (matrix: DsmForNodeChildren_hierarchicalGraph_node_children_orderedAdjacencyMatrix) => React.ReactNode): React.ReactNode {

        if (!aDatabaseId || !aHierarchicalGraphId) {
            return null;
        }

        if (this.state.mainTreeNodeSelection.selectedNodeIds.length > 0) {

            const query = GQ_DSM_FOR_NODE_CHILDREN;
            const queryVariables = {
                databaseId: aDatabaseId,
                hierarchicalGraphId: aHierarchicalGraphId,
                nodeId: this.state.mainTreeNodeSelection.selectedNodeIds[0]
            };

            return <Query<DsmForNodeChildren, DsmForNodeChildrenVariables> query={query} variables={queryVariables}
                                                                           fetchPolicy="no-cache">
                {({loading, data, error}) => {

                    if (loading) {
                        return <Spin className={"slizaaSpinner"} size="large"/>;
                    }

                    if (error) {
                        return <h1>{error.message}</h1>
                    }

                    if (!data || !data.hierarchicalGraph || !data.hierarchicalGraph.node) {
                        return <div>UNDEFINED - TODO</div>
                    }

                    const matrix = data.hierarchicalGraph.node.children.orderedAdjacencyMatrix;
                    return consumer(matrix);
                }}
            </Query>

        }
        return null;
    }

    private onSelectDependency = (aSelection: IDependencySelection | undefined): void => {
        if (aSelection !== undefined) {

            this.setState({
                dependenciesTree: {
                    selectionNodeType: NodeType.SOURCE,
                    sourceTreeNodeSelection: {
                        expandedNodeIds: ["-1"],
                        selectedNodeIds: [],
                    },
                    targetTreeNodeSelection: {
                        expandedNodeIds: ["-1"],
                        selectedNodeIds: [],
                    }
                },
                mainDependencySelection: {
                    selectedEdge: aSelection.selectedEdge,
                    sourceNode: aSelection.sourceNode,
                    targetNode: aSelection.targetNode,
                }
            });
        } else {
            this.setState({
                mainDependencySelection: undefined
            });
        }
    }

    private onMainTreeExpand = (aExpandedNodeIds: string[]): void => {
        this.setState(
            {
                mainTreeNodeSelection: {
                    ...this.state.mainTreeNodeSelection,
                    expandedNodeIds: aExpandedNodeIds,
                }
            });
    }

    private onMainTreeSelect = (aSelectedNodes: ISlizaaNode[]): void => {
        this.setState(
            {
                dependenciesTree: undefined,
                mainDependencySelection: undefined,
                mainTreeNodeSelection: {
                    ...this.state.mainTreeNodeSelection,
                    selectedNodeIds: aSelectedNodes.map(v => v.key),
                },
            });
    }

    private onSideMarkerResize = (aHorizontalSideMarkerHeight: number | undefined, aVerticalSideMarkerWidth: number | undefined): void => {

        this.setState(
            {
                layout: {
                    ...this.state.layout,
                    dsmSetting: {
                        horizontalSideMarkerHeight: aVerticalSideMarkerWidth ? aVerticalSideMarkerWidth : 0,
                        verticalSideMarkerWidth: aHorizontalSideMarkerHeight ? aHorizontalSideMarkerHeight : 0,
                    },
                },
            }
        );
    }
}

export enum DependencyVisualization {
    DependencyStructureMatrix, DependencyGraph
}