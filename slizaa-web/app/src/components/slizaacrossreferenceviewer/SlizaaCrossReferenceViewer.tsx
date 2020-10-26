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

import * as React from 'react';
import {CSSProperties} from 'react';
import {ISlizaaNode} from "../../model/ISlizaaNode";
import {
    fetchChildren,
    fetchChildrenFilteredByReferencedNodes,
    fetchChildrenFilteredByReferencingNodes
} from "../../model/SlizaaNodeChildrenResolver";
import {SlizaaNodeFactory} from "../../model/SlizaaNodeFactory";
import {SlizaaIcon} from "../slizaaicon";
import STree from "../stree/STree";
import {ISlizaaCrossReferenceViewerProps} from "./ISlizaaCrossReferenceViewerProps";
import {ISlizaaCrossReferenceViewerState} from "./ISlizaaCrossReferenceViewerState";
import './SlizaaCrossReferenceViewer.css';

enum TreePosition {
    LEFT, CENTERED, RIGHT
}

export class SlizaaCrossReferenceViewer extends React.Component<ISlizaaCrossReferenceViewerProps, ISlizaaCrossReferenceViewerState> {

    constructor(props: any) {
        super(props);

        this.state = {
            centerNode: SlizaaNodeFactory.createRoot(props.databaseId + "-" + props.hierarchicalGraphId),
            expandedCenterNodeIds: ["-1"],
            expandedLeftNodeIds: ["-1"],
            expandedRightNodeIds: ["-1"],
            key: Math.random(),
            leftNode: SlizaaNodeFactory.createRoot(props.databaseId + "-" + props.hierarchicalGraphId),
            rightNode: SlizaaNodeFactory.createRoot(props.databaseId + "-" + props.hierarchicalGraphId),
            selectedCenterNodeIds: [],
            selectedLeftNodeIds: [],
            selectedRightNodeIds: [],
        };
    }

    public render() {

        if (!this.props.databaseId || !this.props.hierarchicalGraphId) {
            return null;
        }

        if (this.state.selectedCenterNodeIds.length === 0) {
            return this.renderTrees(false, false);
        }

        // const variables: FilteredReferencedNodesVariables = {
        //     databaseId: this.props.databaseId,
        //     expandedTargetNodeIds: this.state.expandedRightNodeIds,
        //     hierarchicalGraphId: this.props.hierarchicalGraphId,
        //     selectedNodeIds: this.state.selectedCenterNodeIds,
        // }

        return this.renderTrees(true, false);

        // return <Query<FilteredReferencedNodes, FilteredReferencedNodesVariables>
        //     query={GQ_FILTERED_REFERENCED_NODES}
        //     fetchPolicy={"no-cache"}
        //     // pollInterval={1000}
        //     variables={variables}>
        //
        //     {({loading, data, error, refetch}) => {
        //
        //         if (error) {
        //             return <h1>{error.message}</h1>
        //         }
        //
        //         return this.renderTrees(true, loading);
        //     }}
        // </Query>
    }

    private renderTrees = (nodesSelected: boolean, loading: boolean): React.ReactNode => {

        const stylesNoEvents: CSSProperties = {pointerEvents: loading ? "none" : "unset"};
        const stylesCursorWait: CSSProperties = {cursor: loading ? "wait" : "unset"};

        const leftTree = nodesSelected ?
            <STree /* key={this.state.key} */
                   rootNode={this.state.leftNode}
                   onExpand={this.onExpand(TreePosition.LEFT)}
                   onSelect={this.onSelect(TreePosition.LEFT)}
                   expandedKeys={this.state.expandedLeftNodeIds}
                   selectedKeys={this.state.selectedLeftNodeIds}
                   loadData={this.loadChildrenFilteredByReferencedNodes}
                   fetchIcon={this.fetchIcon}
            /> :
            <div/>

        const rightTree = nodesSelected ?
            <STree /* key={this.state.key} */
                   rootNode={this.state.rightNode}
                   onExpand={this.onExpand(TreePosition.RIGHT)}
                   onSelect={this.onSelect(TreePosition.RIGHT)}
                   expandedKeys={this.state.expandedRightNodeIds}
                   selectedKeys={this.state.selectedRightNodeIds}
                   loadData={this.loadChildrenFilteredByReferencingNodes}
                   fetchIcon={this.fetchIcon}
            /> :
            <div/>

        // tslint:disable-next-line:no-console
        console.log(this.state.expandedLeftNodeIds)

        // tslint:disable-next-line:no-console
        console.log(this.state.expandedRightNodeIds)

        return <div className="crossReferenceViewerContainer" style={stylesCursorWait}>
            <div className="crossReferenceViewerContainer-Left" style={stylesNoEvents}>
                {leftTree}
            </div>
            <div className="crossReferenceViewerContainer-Center" style={stylesNoEvents}>
                <STree rootNode={this.state.centerNode}
                       onExpand={this.onExpand(TreePosition.CENTERED)}
                       onSelect={this.onSelect(TreePosition.CENTERED)}
                       expandedKeys={this.state.expandedCenterNodeIds}
                       selectedKeys={this.state.selectedCenterNodeIds}
                       loadData={this.loadChildren}
                       fetchIcon={this.fetchIcon}
                />
            </div>
            <div className="crossReferenceViewerContainer-Right" style={stylesNoEvents}>
                {rightTree}
            </div>
        </div>
    }

    private fetchIcon = (item: ISlizaaNode): React.ReactNode => {
        return <SlizaaIcon iconId={item.iconId}/>
    }

    private loadChildren = (parent: ISlizaaNode, callback: () => void): Promise<{}> => {
        return fetchChildren(this.props.client, parent, this.props.databaseId ? this.props.databaseId : "", this.props.hierarchicalGraphId ? this.props.hierarchicalGraphId : "", callback);
    }

    private loadChildrenFilteredByReferencedNodes = (parent: ISlizaaNode, callback: () => void): Promise<{}> => {
        return fetchChildrenFilteredByReferencedNodes(
            this.props.client,
            this.props.databaseId ? this.props.databaseId : "",
            this.props.hierarchicalGraphId ? this.props.hierarchicalGraphId : "",
            parent,
            this.state.selectedCenterNodeIds,
            callback);
    }

    private loadChildrenFilteredByReferencingNodes = (parent: ISlizaaNode, callback: () => void): Promise<{}> => {
        return fetchChildrenFilteredByReferencingNodes(
            this.props.client,
            this.props.databaseId ? this.props.databaseId : "",
            this.props.hierarchicalGraphId ? this.props.hierarchicalGraphId : "",
            parent,
            this.state.selectedCenterNodeIds,
            callback);
    }

    private onExpand = (tree: TreePosition): ((expandedItems: string[]) => void) => {
        return (expandedItems: string[]): void => {

            switch (tree) {
                case TreePosition.LEFT: {
                    this.setState({
                        expandedLeftNodeIds: expandedItems,
                    });
                    this.state.expandedLeftNodeIds.forEach(item => {
                        // console.log(item.)
                    });
                    break;
                }
                case TreePosition.CENTERED: {
                    this.setState({
                        expandedCenterNodeIds: expandedItems,
                    });
                    break;
                }
                case TreePosition.RIGHT: {
                    this.setState({
                        expandedRightNodeIds: expandedItems,
                    });
                    break;
                }
            }
        }
    }

    private onSelect = (tree: TreePosition): ((selectedItems: ISlizaaNode[]) => void) => {
        return (selectedItems: ISlizaaNode[]): void => {

            switch (tree) {
                case TreePosition.LEFT: {
                    this.setState({
                        selectedLeftNodeIds: selectedItems.map(i => i.key),
                        selectedRightNodeIds: [],
                    });
                    if (this.props.onSelect) {
                        this.props.onSelect(this.state.selectedLeftNodeIds, this.state.selectedCenterNodeIds);
                    }
                    break;
                }
                case TreePosition.CENTERED: {
                    this.setState({
                        key: Math.random(),
                        leftNode: SlizaaNodeFactory.createRoot(this.props.databaseId + "-" + this.props.hierarchicalGraphId),
                        rightNode: SlizaaNodeFactory.createRoot(this.props.databaseId + "-" + this.props.hierarchicalGraphId),
                        selectedCenterNodeIds: selectedItems.map(i => i.key),
                    });
                    break;
                }
                case TreePosition.RIGHT: {
                    this.setState({
                        selectedLeftNodeIds: [],
                        selectedRightNodeIds: selectedItems.map(i => i.key)
                    });
                    if (this.props.onSelect) {
                        this.props.onSelect(this.state.selectedCenterNodeIds, this.state.selectedRightNodeIds);
                    }
                    break;
                }
            }
        }
    }
}