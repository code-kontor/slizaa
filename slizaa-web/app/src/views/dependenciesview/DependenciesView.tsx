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
import {Spin} from 'antd';
import ApolloClient from 'apollo-client';
import * as React from 'react';
import {ApolloConsumer, Query} from 'react-apollo';
import {Card} from 'src/components/card';
import {HierarchicalGraphTree} from 'src/components/hierarchicalgraphtree';
import {VerticalSplitLayout} from 'src/components/layout';
import {SlizaaDependencyViewer} from "../../components/slizaadependencyviewer/SlizaaDependencyViewer";
import {
    DsmForNodeChildren,
    DsmForNodeChildren_hierarchicalGraph_node_children_orderedAdjacencyMatrix,
    DsmForNodeChildrenVariables
} from "../../gqlqueries/__generated__/DsmForNodeChildren";
import {GQ_DSM_FOR_NODE_CHILDREN} from "../../gqlqueries/GqlQueries";
import {ISlizaaNode} from "../../model/ISlizaaNode";
import {NodeType} from "../../model/NodeType";
import {SlizaaHorizontalSplitView} from "../fwk/SlizaaHorizontalSplitView";
import './DependenciesView.css';
import {DependenciesViewProps} from "./DependenciesViewLayoutConstants";
import {DependencyGraphPart} from "./dependencyGraphPart";
import {DsmPart} from "./dsmPart/DsmPart";
import {IDependenciesViewProps} from "./IDependenciesViewProps";
import {IDependenciesViewState} from "./IDependenciesViewState";
import {IDependencySelection} from "./IDependencyViewModel";


export class DependenciesView extends React.Component<IDependenciesViewProps, IDependenciesViewState> {

    // public static getDerivedStateFromProps(props: IDependenciesViewProps, state: IDependenciesViewState) {
    //     if (props.databaseId !== state.databaseId ||
    //         props.hierarchicalGraphId !== state.hierarchicalGraphId) {
    //         return {
    //             databaseId: props.databaseId,
    //             hierarchicalGraphId: props.hierarchicalGraphId,
    //             mainTreeNodeSelection: {
    //                 expandedNodeIds: ["-1"],
    //                 selectedNodeIds: [],
    //             }
    //         };
    //     }
    //     return null;
    // }

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
                height: 600,
                horizontalRatio: 500,
                upperDividerPosition: 450,
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
                    <SlizaaHorizontalSplitView
                        id="dependencyViewMain"
                        top={
                            <VerticalSplitLayout
                                id="upper"
                                gutter={DependenciesViewProps.GUTTER_SIZE}
                                initialWidth={this.state.layout.upperDividerPosition}
                                onWidthChanged={this.onUpperSplitLayoutWidthChanged}
                                left={
                                    this.hierarchicalGraphCard(cl)
                                }
                                right={
                                    this.dependenciesOverviewCard(cl)
                                }
                            />
                        }
                        bottom={this.dependencyDetailsCard(cl)}
                    />)
                }
            </ApolloConsumer>
        );
    }

    private hierarchicalGraphCard(cl: ApolloClient<any>): React.ReactElement {
        return <Card title="Hierarchical Graph"
                     id="hierarchicalGraph"
                     padding={0}>
            <HierarchicalGraphTree
                key={this.state.databaseId + "-" + this.state.hierarchicalGraphId}
                client={cl}
                databaseId={this.state.databaseId}
                hierarchicalGraphId={this.state.hierarchicalGraphId}
                onSelect={this.onMainTreeSelect}
                onExpand={this.onMainTreeExpand}
                expandedKeys={this.state.mainTreeNodeSelection.expandedNodeIds}
                checkedKeys={this.state.mainTreeNodeSelection.selectedNodeIds}/>
        </Card>;
    }

    private dependenciesOverviewCard(cl: ApolloClient<any>): React.ReactElement {
        return <Card title="Dependencies Overview"
                     id="dependenciesOverview"
                     allowOverflow={true}
                     padding={0}>
            {this.dependencyGraph(cl)}
        </Card>;
    }

    private dependencyDetailsCard(client: ApolloClient<any>): React.ReactElement {

        const node: React.ReactElement = this.state.mainDependencySelection === undefined ?

            // return empty div if selected dependency is undefined
            <div/> :

            // else retunr the dependency viewer
            <SlizaaDependencyViewer
                key={this.state.mainDependencySelection.sourceNode.id + "-" + this.state.mainDependencySelection.targetNode.id}
                client={client}
                height={((this.state.layout.height * (1000 - this.state.layout.horizontalRatio)) / 1000) - 135}
                databaseId={this.state.databaseId}
                hierarchicalGraphId={this.state.hierarchicalGraphId}
                sourceNodeId={this.state.mainDependencySelection.sourceNode.id}
                targetNodeId={this.state.mainDependencySelection.targetNode.id}
            />

        return <Card
            id="dependenciesDetails"
            title="Dependencies Details" allowOverflow={false} padding={0}>
            {node}
        </Card>;
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
                        return <Spin size="large"/>;
                    }

                    if (error) {
                        return <h1>{error.message}</h1>
                    }

                    if (!data || !data.hierarchicalGraph || !data.hierarchicalGraph.node) {
                        return <div>UNDEFINED - TODO</div>
                    }

                    const matrix = data.hierarchicalGraph.node.children.orderedAdjacencyMatrix;

                    // tslint:disable-next-line:no-console
                    console.log(matrix)

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


    private onUpperSplitLayoutWidthChanged = (id: string, newWidth: number): void => {
        this.setState({
            layout: {
                ...this.state.layout,
                upperDividerPosition: newWidth,
            }
        });
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