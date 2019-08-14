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
import {DSM} from 'src/components/dsm';
import {HierarchicalGraphTree} from 'src/components/hierarchicalgraphtree';
import {HorizontalSplitLayout, ResizableBox} from 'src/components/layout';
import {IAppState} from 'src/redux/IAppState';
import {SlizaaDependencyTree} from "../../../components/slizaadependencytree";
import {NodeType} from "../../../model/NodeType";
import {DsmForNodeChildren, DsmForNodeChildrenVariables} from './__generated__/DsmForNodeChildren';
import './DependenciesView.css';
import {GQ_DSM_FOR_NODE_CHILDREN} from './GqlQueries';
import {IDependenciesViewProps} from "./IDependenciesViewProps";
import {IDependenciesViewState} from "./IDependenciesViewState";


export class DependenciesView extends React.Component<IDependenciesViewProps, IDependenciesViewState> {

    constructor(props: IDependenciesViewProps) {
        super(props);

        this.state = {
            layout: {
                dsmSetting: {
                    horizontalSideMarkerHeight: 10,
                    verticalSideMarkerWidth: 150
                },
                lowerHeight: 371,
                treeWidth: 400,
                upperHeight: 600,
            },
            mainTreeNodeSelection: {
                expandedNodeIds: ["-1"],
                selectedNodeIds: [],
            },
        }
    }

    public render() {

        if (!this.props.hierarchicalGraphId) {
            return null
        }

        return (
            <ApolloConsumer>
                {cl =>
                    <div>
                        <ResizableBox id="upperResizableBox"
                                      intitalHeight={this.state.layout.upperHeight}
                                      onHeightChanged={this.onHeightChanged}>
                            <HorizontalSplitLayout id="upper"
                                                   initialWidth={this.state.layout.treeWidth}
                                                   onWidthChanged={this.onSplitLayoutWidthChanged}
                                                   left={
                                                       <Card title="Hierarchical Graph">
                                                           {this.hierarchicalGraph(cl)}
                                                       </Card>
                                                   }
                                                   right={
                                                       <Card title="Dependencies Overview">
                                                           {this.dependenciesOverview()}
                                                       </Card>
                                                   }
                            />
                        </ResizableBox>
                        <ResizableBox id="lowerResizableBox"
                                      intitalHeight={this.state.layout.lowerHeight}
                                      onHeightChanged={this.onHeightChanged}>
                            <Card title="Dependencies Details" allowOverflow={true}>
                                {this.dependenciesDetails(cl)}
                            </Card>
                        </ResizableBox>
                    </div>
                }
            </ApolloConsumer>
        );
    }

    private hierarchicalGraph(client: ApolloClient<any>): React.ReactNode {
        return <HierarchicalGraphTree
            client={client}
            databaseId={this.props.databaseId}
            hierarchicalGraphId={this.props.hierarchicalGraphId}
            onSelect={this.onMainTreeSelect}
            onExpand={this.onMainTreeExpand}
            expandedKeys={this.state.mainTreeNodeSelection.expandedNodeIds}
            checkedKeys={this.state.mainTreeNodeSelection.selectedNodeIds} />

    }

    /**
     * Creates the dependencies overview component.
     */
    private dependenciesOverview(): React.ReactNode {

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

                    // get  the data
                    const {orderedNodes, cells, stronglyConnectedComponents} = data.hierarchicalGraph.node.children.dependencyMatrix

                    return <DSM labels={orderedNodes}
                                cells={cells}
                                stronglyConnectedComponents={stronglyConnectedComponents}
                                horizontalBoxSize={35}
                                verticalBoxSize={35}
                                horizontalSideMarkerHeight={this.state.layout.dsmSetting.horizontalSideMarkerHeight}
                                verticalSideMarkerWidth={this.state.layout.dsmSetting.verticalSideMarkerWidth}
                                onSideMarkerResize={this.onSideMarkerResize}
                                onSelect={this.onSelectDependency}
                    />
                }}
            </Query>

        }
        return null;
    }

    private dependenciesDetails(client: ApolloClient<any>): React.ReactNode {

        // return empty div if selected dependency is undefined
        if (this.state.mainDependencySelection === undefined) {
            return <div/>;
        }

        //
        return <SlizaaDependencyTree client={client}
                                     databaseId={this.props.databaseId}
                                     hierarchicalGraphId={this.props.hierarchicalGraphId}
                                     sourceNodeId={this.state.mainDependencySelection.sourceNodeId}
                                     targetNodeId={this.state.mainDependencySelection.targetNodeId}
                                     selectedNodeIds={[]}
                                     selectedNodesType={NodeType.SOURCE}
        />
    }

    private onSelectDependency = (aColumnNodeId: string | undefined, aRowNodeId: string | undefined): void => {
        if (aColumnNodeId !== undefined && aRowNodeId !== undefined) {
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
                    sourceNodeId: aRowNodeId,
                    targetNodeId: aColumnNodeId,
                    // TODO: weight
                    weight: 0
                }
            });
        } else {
            this.setState({
                mainDependencySelection: undefined
            });
        }
    }

    private onSplitLayoutWidthChanged = (id: string, newWidth: number): void => {
        this.setState({
            layout: {
                ...this.state.layout,
                treeWidth: newWidth,
            }
        });
    }

    private onHeightChanged = (id: string, newHeight: number): void => {
        if (id === "upperResizableBox") {
            this.setState(
                {
                    layout: {
                        ...this.state.layout,
                        upperHeight: newHeight,
                    }
                });
        } else if (id === "lowerResizableBox") {
            this.setState(
                {
                    layout: {
                        ...this.state.layout,
                        lowerHeight: newHeight,
                    }
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

    private onMainTreeSelect = (aSelectedNodeIds: string[]): void => {
        this.setState(
            {
                dependenciesTree: undefined,
                mainDependencySelection: undefined,
                mainTreeNodeSelection: {
                    ...this.state.mainTreeNodeSelection,
                    selectedNodeIds: aSelectedNodeIds,
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
        dependenciesViewState: state.dependenciesViewState,
        hierarchicalGraphId: state.currentHierarchicalGraph
    };
};

export default connect(mapStateToProps)(DependenciesView);