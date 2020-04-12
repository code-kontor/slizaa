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
import {connect} from 'react-redux';
import {Card} from 'src/components/card';
import {HierarchicalGraphTree} from 'src/components/hierarchicalgraphtree';
import {HorizontalSplitLayout, VerticalSplitLayout} from 'src/components/layout';
import {IAppState} from 'src/redux/IAppState';
import {SlizaaDependencyViewer} from "../../components/slizaadependencyviewer/SlizaaDependencyViewer";
import {
    DsmForNodeChildren,
    DsmForNodeChildren_hierarchicalGraph_node_children_dependencyMatrix,
    DsmForNodeChildrenVariables
} from "../../gqlqueries/__generated__/DsmForNodeChildren";
import {GQ_DSM_FOR_NODE_CHILDREN} from "../../gqlqueries/GqlQueries";
import {ISlizaaNode} from "../../model/ISlizaaNode";
import {NodeType} from "../../model/NodeType";
import './DependenciesView.css';
import {DependenciesViewProps} from "./DependenciesViewLayoutConstants";
import {DependencyGraphPart} from "./dependencyGraphPart";
import {DsmPart} from "./dsmPart/DsmPart";
import {IDependenciesViewProps} from "./IDependenciesViewProps";
import {IDependenciesViewState} from "./IDependenciesViewState";
import {IDependencySelection} from "./IDependencyViewModel";


export class DependenciesView extends React.Component<IDependenciesViewProps, IDependenciesViewState> {

    public static getDerivedStateFromProps(props: IDependenciesViewProps, state: IDependenciesViewState) {
        if (props.databaseId !== state.databaseId ||
            props.hierarchicalGraphId !== state.hierarchicalGraphId) {
            return {
                databaseId: props.databaseId,
                hierarchicalGraphId: props.hierarchicalGraphId,
                mainTreeNodeSelection: {
                    expandedNodeIds: ["-1"],
                    selectedNodeIds: [],
                }
            };
        }
        return null;
    }

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

    public componentDidMount(): void {
        this.updateWindowDimensions();
        window.addEventListener('resize', this.updateWindowDimensions);
    }

    public componentWillUnmount(): void {
        window.removeEventListener('resize', this.updateWindowDimensions);
    }

    public render() {

        if (!this.props.hierarchicalGraphId) {
            return null
        }

        return (
            <ApolloConsumer>
                {cl =>
                    <VerticalSplitLayout
                        id="dependencyViewMain"
                        height={this.state.layout.height}
                        gutter={DependenciesViewProps.GUTTER_SIZE}
                        initialRatio={this.state.layout.horizontalRatio}
                        onRatioChanged={this.onHorizontalRatioChanged}
                        top={
                            <HorizontalSplitLayout
                                id="upper"
                                gutter={DependenciesViewProps.GUTTER_SIZE}
                                initialWidth={this.state.layout.upperDividerPosition}
                                onWidthChanged={this.onUpperSplitLayoutWidthChanged}
                                left={
                                    <Card title="Hierarchical Graph"
                                          padding={0}>
                                        {this.hierarchicalGraph(cl)}
                                    </Card>
                                }
                                right={
                                    <Card title="Dependencies Overview"
                                          allowOverflow={false}
                                          padding={0}>
                                        {this.dependencyGraph(cl)}
                                    </Card>
                                }
                            />
                        }
                        bottom={
                            <Card title="Dependencies Details" allowOverflow={false} padding={0}>
                                {this.dependencyDetailViewer(cl)}
                            </Card>
                        }/>
                }
            </ApolloConsumer>
        );
    }

    private hierarchicalGraph(client: ApolloClient<any>): React.ReactNode {
        return <HierarchicalGraphTree
            key={this.props.databaseId + "-" + this.props.hierarchicalGraphId}
            client={client}
            databaseId={this.props.databaseId}
            hierarchicalGraphId={this.props.hierarchicalGraphId}
            onSelect={this.onMainTreeSelect}
            onExpand={this.onMainTreeExpand}
            expandedKeys={this.state.mainTreeNodeSelection.expandedNodeIds}
            checkedKeys={this.state.mainTreeNodeSelection.selectedNodeIds}/>
    }

    private dependencyGraph(client: ApolloClient<any>): React.ReactNode {

        return this.queryAndConsume(client, (matrix) => {

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

        return this.queryAndConsume(client, (matrix) => {

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

    private dependencyDetailViewer = (client: ApolloClient<any>): React.ReactNode => {

        // return empty div if selected dependency is undefined
        if (this.state.mainDependencySelection === undefined) {
            return <div/>;
        }

        return <SlizaaDependencyViewer
            key={this.state.mainDependencySelection.sourceNode.id + "-" + this.state.mainDependencySelection.targetNode.id}
            client={client}
            height={((this.state.layout.height * (1000 - this.state.layout.horizontalRatio)) / 1000) - 135}
            databaseId={this.props.databaseId}
            hierarchicalGraphId={this.props.hierarchicalGraphId}
            sourceNodeId={this.state.mainDependencySelection.sourceNode.id}
            targetNodeId={this.state.mainDependencySelection.targetNode.id}
        />
    }

    private queryAndConsume(client: ApolloClient<any>, consumer: (matrix: DsmForNodeChildren_hierarchicalGraph_node_children_dependencyMatrix) => React.ReactNode): React.ReactNode {

        if (this.state.mainTreeNodeSelection.selectedNodeIds.length > 0) {

            const query = GQ_DSM_FOR_NODE_CHILDREN;
            const queryVariables = {
                databaseId: this.props.databaseId,
                hierarchicalGraphId: this.props.hierarchicalGraphId,
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

                    const matrix = data.hierarchicalGraph.node.children.dependencyMatrix;

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

    private updateWindowDimensions = (): void => {

        const HEADER_SIZE_WITH_MARGIN = 55;

        const newHeight = window.innerHeight - HEADER_SIZE_WITH_MARGIN;

        this.setState(
            {
                layout: {
                    ...this.state.layout,
                    height: newHeight,
                }
            });
    }

    private onHorizontalRatioChanged = (id: string, newRation: number): void => {
        this.setState(
            {
                layout: {
                    ...this.state.layout,
                    horizontalRatio: newRation,
                }
            })
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

const mapStateToProps = (state: IAppState) => {
    return {
        databaseId: state.currentDatabase,
        hierarchicalGraphId: state.currentHierarchicalGraph
    };
};

export default connect(mapStateToProps)(
    DependenciesView
);