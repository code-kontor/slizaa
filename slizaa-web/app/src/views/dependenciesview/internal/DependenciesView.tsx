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

import { Spin } from 'antd';
import ApolloClient from 'apollo-client';
import * as React from 'react';
import { ApolloConsumer, Query} from 'react-apollo';
import { connect } from 'react-redux';
import { Dispatch } from 'redux';
import { NodeType } from 'src/__generated__/query-types';
import { Card } from 'src/components/card';
import { DSM } from 'src/components/dsm';
import { HierarchicalGraphTree } from 'src/components/hierarchicalgraphtree';
import { HorizontalSplitLayout, ResizableBox } from 'src/components/layout';
import { SlizaaIcon } from 'src/components/slizaaicon';
import STree from 'src/components/stree/STree';
import { ISlizaaNode } from 'src/model/ISlizaaNode';
import { SlizaaNode } from 'src/model/SlizaaNode';
import { fetchChildrenFilterByDependencySet } from 'src/model/SlizaaNodeChildrenResolver';
import { action_DsmView_SetDsmSidemarkerSize, actionSetTreeNodeSelection_DsmView } from 'src/redux/Actions';
import { IAppState, IDependenciesViewState } from 'src/redux/IAppState';
import { DsmForNodeChildren, DsmForNodeChildrenVariables } from './__generated__/DsmForNodeChildren';
import './DependenciesView.css';
import { GQ_DSM_FOR_NODE_CHILDREN } from './GqlQueries';

interface IProps {
    databaseId: string
    hierarchicalGraphId: string
    dependenciesViewState: IDependenciesViewState
    dispatchSelectNodeSelection: (expandedNodeIds: string[], selectedNodeIds: string[]) => void
    dispatchSidemarkerResize: (horizontalHeight: number, verticalWidth: number) => void
}

interface IState {
    treeWidth: number
    upperHeight: number
    lowerHeight: number
}

export class ViewDsm extends React.Component<IProps, IState> {

    constructor(props: IProps) {
        super(props);

        this.state = {
            lowerHeight: 371,
            treeWidth: 400,
            upperHeight: 600,
        }
    }

    public render() {

        if (!this.props.hierarchicalGraphId) {
            return null
        }

        const rootNodeDependencySource = SlizaaNode.createRoot("Root", "default");
        const rootNodeDependencyTarget = SlizaaNode.createRoot("Root", "default");
 
        return (

            <div>
                <ResizableBox id="upperResizableBox" intitalHeight={this.state.upperHeight} onHeightChanged={this.onHeightChanged} >
                    <HorizontalSplitLayout id="upper" initialWidth={this.state.treeWidth} onWidthChanged={this.onWidthChanged}
                        left={
                            <Card title="Hierarchical Graph" >
                                <ApolloConsumer>
                                    {cl =>
                                        <HierarchicalGraphTree
                                            client={cl}
                                            databaseId={this.props.databaseId}
                                            hierarchicalGraphId={this.props.hierarchicalGraphId}
                                            onSelect={this.onSelect}
                                            onExpand={this.onExpand}
                                            expandedKeys={this.props.dependenciesViewState.treeNodeSelection ? this.props.dependenciesViewState.treeNodeSelection.expandedNodeIds : [] } />
                                    }
                                </ApolloConsumer>
                            </Card>
                        }
                        right={
                            <Card title="Dependencies Overview" >
                                {this.dependenciesOverwiew()}

                            </Card>
                        }
                    />
                </ResizableBox>
                <ResizableBox id="lowerResizableBox" intitalHeight={this.state.lowerHeight} onHeightChanged={this.onHeightChanged}>
                    <Card title="Dependencies Details" >
                        <ApolloConsumer>
                            {cl =>
                                 <HorizontalSplitLayout id="upper" initialWidth={450}
                                    left={
                                        <STree
                                            rootNode={rootNodeDependencySource}
                                            // onExpand={this.onExpand}
                                            // onSelect={this.onSelect}
                                            loadData={this.loadChildrenFilteredByDependencySource(cl)}
                                            fetchIcon={this.fetchIcon}
                                        />
                                    }
                                    right={
                                        <STree
                                            rootNode={rootNodeDependencyTarget}
                                            // onExpand={this.onExpand}
                                            // onSelect={this.onSelect}
                                            loadData={this.loadChildrenFilteredByDependencyTarget(cl)}
                                            fetchIcon={this.fetchIcon}
                                        />
                                    }
                                />
                            }
                        </ApolloConsumer>
                    </Card>
                </ResizableBox>
            </div>
        );
    }

    private onSelect = (selectedKeys: string[]): void => {
        const exapndedNodeIds = this.props.dependenciesViewState.treeNodeSelection ? this.props.dependenciesViewState.treeNodeSelection.expandedNodeIds : [];
        this.props.dispatchSelectNodeSelection(exapndedNodeIds, selectedKeys);
    }

    private onResizeDsmSidemarker = (horizontalHeight: number, verticalWidth: number): void => {
         this.props.dispatchSidemarkerResize(horizontalHeight, verticalWidth);
    }

    private onExpand = (expandedKeys: string[]): void => {
        const selectedNodeIds = this.props.dependenciesViewState.treeNodeSelection ? this.props.dependenciesViewState.treeNodeSelection.selectedNodeIds : [];
        this.props.dispatchSelectNodeSelection(expandedKeys, selectedNodeIds);
    }

    private onWidthChanged = (id: string, newWidth: number): void => {
        this.setState({ treeWidth: newWidth });
    }

    private onHeightChanged = (id: string, newHeight: number): void => {
        if (id === "upperResizableBox") {
            this.setState({ upperHeight: newHeight });
        }
        else if (id === "lowerResizableBox") {
            this.setState({ lowerHeight: newHeight });
        }
    }

    private fetchIcon = (item: ISlizaaNode): React.ReactNode => {
        return <SlizaaIcon iconId={item.iconId} />
      }

    private loadChildrenFilteredByDependencySource = (client : ApolloClient<any>): (parent: SlizaaNode, callback: () => void) => Promise<{}> => {
        return (p: SlizaaNode, c: () => void) => fetchChildrenFilterByDependencySet(client, p, NodeType.SOURCE, this.props.databaseId, this.props.hierarchicalGraphId, c );
    }

    private loadChildrenFilteredByDependencyTarget = (client : ApolloClient<any>): (parent: SlizaaNode, callback: () => void) => Promise<{}> => {
        return (p: SlizaaNode, c: () => void) => fetchChildrenFilterByDependencySet(client, p, NodeType.TARGET, this.props.databaseId, this.props.hierarchicalGraphId, c );
    }

    private dependenciesOverwiew(): React.ReactNode {

        if (this.props.dependenciesViewState.treeNodeSelection && this.props.dependenciesViewState.treeNodeSelection.selectedNodeIds && this.props.dependenciesViewState.treeNodeSelection.selectedNodeIds.length > 0) {

            const query = GQ_DSM_FOR_NODE_CHILDREN;
            const queryVariables = {
            databaseId: this.props.databaseId,
            hierarchicalGraphId: this.props.hierarchicalGraphId,
            nodeId: this.props.dependenciesViewState.treeNodeSelection && this.props.dependenciesViewState.treeNodeSelection.selectedNodeIds && this.props.dependenciesViewState.treeNodeSelection.selectedNodeIds.length > 0 ? this.props.dependenciesViewState.treeNodeSelection.selectedNodeIds[0] : "-1"
        };                            

        return <Query<DsmForNodeChildren, DsmForNodeChildrenVariables> query={query} variables={queryVariables} fetchPolicy="no-cache" >
        {({ loading, data, error }) => {
            
            if (loading) {
                return <Spin size="large" />;
                                        }
                                        
                                        if (error) {
                                            return <h1>{error.message}</h1>
                                        }
                                        
                                        if (!data || !data.hierarchicalGraph || !data.hierarchicalGraph.node) {
                                            return <div>UNDEFINED - TODO</div>
                                        }
                                        
                                        // get  the data
                                        const { orderedNodes, cells, stronglyConnectedComponents } = data.hierarchicalGraph.node.children.dependencyMatrix
                                        
                                        return <DSM labels={orderedNodes}
                                        cells={cells}
                                        stronglyConnectedComponents={stronglyConnectedComponents}
                                        horizontalBoxSize={35}
                                        verticalBoxSize={35} 
                                        horizontalSideMarkerHeight={this.props.dependenciesViewState.dsmSettings.horizontalSideMarkerHeight}
                                        verticalSideMarkerWidth={this.props.dependenciesViewState.dsmSettings.verticalSideMarkerWidth}
                                        onSideMarkerResize={this.onResizeDsmSidemarker}
                                        />
                                    }}
                                </Query>
                               
                            }
        return null;
    }
}

const mapStateToProps = (state: IAppState) => {
    return {
        databaseId: state.currentDatabase,
        dependenciesViewState : state.dependenciesViewState,
        hierarchicalGraphId: state.currentHierarchicalGraph
    };
};

const mapDispatchToProps = (dispatch: Dispatch) => {
    return {
        dispatchSelectNodeSelection: (expandedNodeIds: string[], selectedNodeIds: string[]) => {
            dispatch(actionSetTreeNodeSelection_DsmView(expandedNodeIds, selectedNodeIds)); 
        },
        dispatchSidemarkerResize: (horizontalHeight: number, verticalWidth: number) => {
            dispatch(action_DsmView_SetDsmSidemarkerSize(verticalWidth, horizontalHeight)); 
        },
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(ViewDsm);