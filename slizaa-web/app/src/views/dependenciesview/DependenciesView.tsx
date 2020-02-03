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
import {IDsmSelection} from "../../components/dsm/IDsmProps";
import {SlizaaDependencyList} from "../../components/slizaadependencylist/SlizaaDependencyList";
import {SlizaaDependencyTree} from "../../components/slizaadependencytree";
import {DsmForNodeChildren, DsmForNodeChildrenVariables} from "../../gqlqueries/__generated__/DsmForNodeChildren";
import {GQ_DSM_FOR_NODE_CHILDREN} from "../../gqlqueries/GqlQueries";
import {ISlizaaNode} from "../../model/ISlizaaNode";
import {NodeType} from "../../model/NodeType";
import './DependenciesView.css';
import {DependencyOverviewPart} from "./dsmPart/DependencyOverviewPart";
import {IDependenciesViewProps} from "./IDependenciesViewProps";
import {IDependenciesViewState} from "./IDependenciesViewState";


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
                lowerDividerPosition: 400,
                upperDividerPosition: 400,
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
                        gutter={8}
                        initialRatio={this.state.layout.horizontalRatio}
                        onRatioChanged={this.onHorizontalRatioChanged}
                        top={
                            <HorizontalSplitLayout
                                id="upper"
                                gutter={8}
                                initialWidth={this.state.layout.upperDividerPosition}
                                onWidthChanged={this.onUpperSplitLayoutWidthChanged}
                                left={
                                    <Card title="Hierarchical Graph">
                                        {this.hierarchicalGraph(cl)}
                                    </Card>
                                }
                                right={
                                    <Card title="Dependencies Overview"
                                          allowOverflow={false}>
                                        {this.dependenciesOverview(cl)}
                                    </Card>
                                }
                            />
                        }
                        bottom={
                            <HorizontalSplitLayout
                                id="upper"
                                gutter={8}
                                initialWidth={this.state.layout.lowerDividerPosition}
                                onWidthChanged={this.onLowerSplitLayoutWidthChanged}
                                left={
                                    <Card title="Dependencies List">
                                        {this.dependenciesList(cl)}
                                    </Card>
                                }
                                right={
                                    <Card title="Dependencies Tree" allowOverflow={false}>
                                        {this.dependenciesTree(cl)}
                                    </Card>
                                }
                            />
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

    /**
     * Creates the dependencies overview component.
     */
    private dependenciesOverview(client: ApolloClient<any>): React.ReactNode {

        if (this.state.mainTreeNodeSelection.selectedNodeIds.length > 0) {

            const query = GQ_DSM_FOR_NODE_CHILDREN;
            const queryVariables = {
                databaseId: this.props.databaseId,
                hierarchicalGraphId: this.props.hierarchicalGraphId,
                nodeId: this.state.mainTreeNodeSelection.selectedNodeIds[0]
            };

            return <Query<DsmForNodeChildren, DsmForNodeChildrenVariables> query={query} variables={queryVariables}
                                                                           fetchPolicy="cache-first">
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

                    // get  the data
                    const {orderedNodes, cells, stronglyConnectedComponents} = data.hierarchicalGraph.node.children.dependencyMatrix

                    return <DependencyOverviewPart
                        labels={orderedNodes}
                        cells={cells}
                        stronglyConnectedComponents={stronglyConnectedComponents}
                        horizontalBoxSize={35}
                        verticalBoxSize={35}
                        horizontalSideMarkerHeight={this.state.layout.dsmSetting.horizontalSideMarkerHeight}
                        verticalSideMarkerWidth={this.state.layout.dsmSetting.verticalSideMarkerWidth}
                        onSideMarkerResize={this.onSideMarkerResize}
                        onSelect={this.onSelectDependency}
                        dependencySelection={this.state.mainDependencySelection}
                    />
                }}
            </Query>

        }
        return null;
    }

    private dependenciesTree(client: ApolloClient<any>): React.ReactNode {

        // return empty div if selected dependency is undefined
        if (this.state.mainDependencySelection === undefined) {
            return <div/>;
        }

        const selNodeIds: string[] = this.state.dependenciesTree ?
            (this.state.dependenciesTree.selectionNodeType === NodeType.SOURCE ?
                this.state.dependenciesTree.sourceTreeNodeSelection.selectedNodeIds :
                this.state.dependenciesTree.targetTreeNodeSelection.selectedNodeIds) :
            [];

        const selNodeType: NodeType = this.state.dependenciesTree ?
            this.state.dependenciesTree.selectionNodeType :
            NodeType.SOURCE;

        //
        return <SlizaaDependencyTree
            key={this.props.databaseId + "-" + this.props.hierarchicalGraphId + "-" + this.state.mainDependencySelection.sourceNodeLabel.id + "-" + this.state.mainDependencySelection.targetNodeLabel.id}
            client={client}
            databaseId={this.props.databaseId}
            hierarchicalGraphId={this.props.hierarchicalGraphId}
            sourceNodeId={this.state.mainDependencySelection.sourceNodeLabel.id}
            targetNodeId={this.state.mainDependencySelection.targetNodeLabel.id}
            selectedNodeIds={selNodeIds}
            selectedNodesType={selNodeType}
            onNodesSelected={this.onDependencyTreeNodesSelected}
        />
    }

    private dependenciesList(client: ApolloClient<any>): React.ReactNode {

        // return empty div if selected dependency is undefined
        if (this.state.mainDependencySelection === undefined) {
            return <div/>;
        }

        //
        return <SlizaaDependencyList
            key={this.props.databaseId + "-" + this.props.hierarchicalGraphId + "-" + this.state.mainDependencySelection.sourceNodeLabel.id + "-" + this.state.mainDependencySelection.targetNodeLabel.id}
            client={client}
            databaseId={this.props.databaseId}
            hierarchicalGraphId={this.props.hierarchicalGraphId}
            sourceNodeId={this.state.mainDependencySelection.sourceNodeLabel.id}
            targetNodeId={this.state.mainDependencySelection.targetNodeLabel.id}
        />
    }


    private onDependencyTreeNodesSelected = (aSelectedNodeIds: string[], aSelectedNodesType: NodeType): void => {

        const expandedSourceNodeIds: string[] = this.state.dependenciesTree ? this.state.dependenciesTree.sourceTreeNodeSelection.selectedNodeIds : [];
        const expandedTargetNodeIds: string[] = this.state.dependenciesTree ? this.state.dependenciesTree.targetTreeNodeSelection.selectedNodeIds : [];

        if (aSelectedNodesType === NodeType.SOURCE) {
            this.setState({
                dependenciesTree: {
                    selectionNodeType: NodeType.SOURCE,
                    sourceTreeNodeSelection: {
                        expandedNodeIds: expandedSourceNodeIds,
                        selectedNodeIds: aSelectedNodeIds,
                    },
                    targetTreeNodeSelection: {
                        expandedNodeIds: expandedTargetNodeIds,
                        selectedNodeIds: [],
                    }
                }
            });
        } else if (aSelectedNodesType === NodeType.TARGET) {
            this.setState({
                dependenciesTree: {
                    selectionNodeType: NodeType.TARGET,
                    sourceTreeNodeSelection: {
                        expandedNodeIds: expandedSourceNodeIds,
                        selectedNodeIds: [],
                    },
                    targetTreeNodeSelection: {
                        expandedNodeIds: expandedTargetNodeIds,
                        selectedNodeIds: aSelectedNodeIds,
                    }
                }
            });
        } else {
            this.setState({
                dependenciesTree: {
                    selectionNodeType: NodeType.SOURCE,
                    sourceTreeNodeSelection: {
                        expandedNodeIds: expandedSourceNodeIds,
                        selectedNodeIds: [],
                    },
                    targetTreeNodeSelection: {
                        expandedNodeIds: expandedTargetNodeIds,
                        selectedNodeIds: [],
                    }
                }
            });
        }
    }

    private onSelectDependency = (aSelection: IDsmSelection | undefined): void => {
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
                    sourceNodeLabel: aSelection.sourceLabel,
                    targetNodeLabel: aSelection.targetLabel,
                    weight: aSelection.selectedCell.value
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

    private onLowerSplitLayoutWidthChanged = (id: string, newWidth: number): void => {
        this.setState({
            layout: {
                ...this.state.layout,
                lowerDividerPosition: newWidth,
            }
        });
    }


    private updateWindowDimensions = (): void => {

        const HEADER_SIZE_WITH_MARGIN = 80;

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