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

import {Alert, Dropdown, Menu} from "antd";
import * as React from 'react';
import {NodesToConsider} from "../../gqlqueries/query-types";
import {ISlizaaNode} from "../../model/ISlizaaNode";
import {filterByReferencedNodes, filterByReferencingNodes} from "../../model/ReferencedNodesResolver";
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

        // tslint:disable:object-literal-sort-keys
        this.state = {
            centerNode: SlizaaNodeFactory.createRoot(props.databaseId + "-" + props.hierarchicalGraphId),
            leftNode: SlizaaNodeFactory.createRoot(props.databaseId + "-" + props.hierarchicalGraphId),
            rightNode: SlizaaNodeFactory.createRoot(props.databaseId + "-" + props.hierarchicalGraphId),
            selectedCenterNodeIds: [],
            expandedCenterNodeIds: ["-1"],
            markedCenterNodeIds: [],
            selectedLeftNodeIds: [],
            expandedLeftNodeIds: ["-1"],
            selectedRightNodeIds: [],
            expandedRightNodeIds: ["-1"],
        };
    }

    public render() {

        if (!this.props.databaseId || !this.props.hierarchicalGraphId) {
            return null;
        }

        const leftTree = this.state.selectedCenterNodeIds.length > 0 ?
            <STree rootNode={this.state.leftNode}
                   onExpand={this.onExpand(TreePosition.LEFT)}
                   onSelect={this.onSelect(TreePosition.LEFT)}
                   expandedKeys={this.state.expandedLeftNodeIds}
                   selectedKeys={this.state.selectedLeftNodeIds}
                   loadData={this.loadChildrenFilteredByReferencedNodes}
                   fetchIcon={this.fetchIcon}
            /> :
            <div/>

        const rightTree = this.state.selectedCenterNodeIds.length > 0 ?
            <STree rootNode={this.state.rightNode}
                   onExpand={this.onExpand(TreePosition.RIGHT)}
                   onSelect={this.onSelect(TreePosition.RIGHT)}
                   expandedKeys={this.state.expandedRightNodeIds}
                   selectedKeys={this.state.selectedRightNodeIds}
                   loadData={this.loadChildrenFilteredByReferencingNodes}
                   fetchIcon={this.fetchIcon}
            /> :
            <div/>

        // the 'hidden selection' alert
        const leftOrRightTreeSelectionNotVisible = this.state.markedCenterNodeIds.length > 0 &&
            this.state.selectedCenterNodeIds.some(id => !this.state.markedCenterNodeIds.includes(id));
        const leftSelectionNotVisibleAlert = this.hiddenSelectionMessageBox(
            leftOrRightTreeSelectionNotVisible && this.state.selectedLeftNodeIds.length > 0);
        const rightSelectionNotVisibleAlert = this.hiddenSelectionMessageBox(
            leftOrRightTreeSelectionNotVisible && this.state.selectedRightNodeIds.length > 0);

            const menu = (
                <Menu>
                  {/* <Menu.Item key="1">1st menu item</Menu.Item>
                  <Menu.Item key="2">2nd menu item</Menu.Item>
                  <Menu.Item key="3">3rd menu item</Menu.Item> */}
                </Menu>
              );
                         

        return <div className="crossReferenceViewerContainer">
            <div className="crossReferenceViewerContainer-Left">
                <div style={{width: "100%", height: "100%", overflow: "auto"}}>
                    {leftTree}
                </div>
                {leftSelectionNotVisibleAlert}
            </div>
            <Dropdown overlay={menu} trigger={['contextMenu']}>
            <div className="crossReferenceViewerContainer-Center">
                <STree rootNode={this.state.centerNode}
                    onExpand={this.onExpand(TreePosition.CENTERED)}
                    onSelect={this.onSelect(TreePosition.CENTERED)}
                    expandedKeys={this.state.expandedCenterNodeIds}
                    selectedKeys={this.state.selectedCenterNodeIds}
                    markedKeys={this.state.markedCenterNodeIds}
                    loadData={this.loadChildren}
                    fetchIcon={this.fetchIcon}
                />
            </div>
            </Dropdown>
            <div className="crossReferenceViewerContainer-Right">
                <div style={{width: "100%", height: "100%", overflow: "auto"}}>
                    {rightTree}
                </div>
                {rightSelectionNotVisibleAlert}
            </div>
        </div>
    }

    private onExpand = (tree: TreePosition): ((expandedItems: string[]) => void) => {
        return (expandedItems: string[]): void => {

            switch (tree) {
                case TreePosition.LEFT: {
                    this.setState({
                        expandedLeftNodeIds: expandedItems,
                    });
                    break;
                }
                case TreePosition.CENTERED: {
                    this.setState((state) => {

                        // create updated state object
                        const newState = this.mergeState(state, {
                            expandedCenterNodeIds: expandedItems
                        });

                        this.updateReferencedCenterNodes(newState);

                        return state;
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
                    const selection = selectedItems.map(i => i.key);
                    this.computeReferencedCenterNodes(selection);
                    this.setState({
                        selectedLeftNodeIds: selection,
                        selectedRightNodeIds: [],
                    });
                    if (this.props.onSelect) {
                        this.props.onSelect(selection, this.state.selectedCenterNodeIds);
                    }
                    break;
                }
                case TreePosition.CENTERED: {
                    this.setState((state => {

                        // create updated state object
                        const newState = this.mergeState(state, {
                            leftNode: SlizaaNodeFactory.createRoot(this.props.databaseId + "-" + this.props.hierarchicalGraphId),
                            rightNode: SlizaaNodeFactory.createRoot(this.props.databaseId + "-" + this.props.hierarchicalGraphId),
                            selectedCenterNodeIds: selectedItems.map(i => i.key),
                            selectedRightNodeIds: selectedItems.length > 0 ? this.state.selectedRightNodeIds : [],
                            selectedLeftNodeIds: selectedItems.length > 0 ? this.state.selectedLeftNodeIds : []
                        });

                        this.updateReferencedCenterNodes(newState);

                        const sourceSel = newState.selectedLeftNodeIds.length > 0 ? newState.selectedLeftNodeIds : newState.selectedCenterNodeIds;
                        const targetSel = newState.selectedLeftNodeIds.length > 0 ? newState.selectedCenterNodeIds : newState.selectedRightNodeIds;
                        if (this.props.onSelect) {
                            this.props.onSelect(sourceSel, targetSel);
                        }

                        return newState;
                    }));
   
                    break;
                }
                case TreePosition.RIGHT: {
                    const selection = selectedItems.map(i => i.key);
                    this.computeReferencingCenterNodes(selection);
                    this.setState({
                        selectedLeftNodeIds: [],
                        selectedRightNodeIds: selection
                    });
                    if (this.props.onSelect) {
                        this.props.onSelect(this.state.selectedCenterNodeIds, selection);
                    }
                    break;
                }
            }
        }
    }

    private updateReferencedCenterNodes = (state: ISlizaaCrossReferenceViewerState): void => {
        if (this.state.selectedRightNodeIds.length > 0) {
            this.computeReferencingCenterNodes(state.selectedRightNodeIds);
        } else {
            this.computeReferencedCenterNodes(state.selectedLeftNodeIds);
        }
    }

    private computeReferencedCenterNodes = (selection: string[]): void => {
        if (selection === undefined || selection.length === 0) {
            this.setState({
                markedCenterNodeIds: []
            })
        } else {
            filterByReferencedNodes(
                this.props.client,
                this.props.databaseId,
                this.props.hierarchicalGraphId,
                selection,
                this.state.expandedCenterNodeIds,
                NodesToConsider.SELF_AND_CHILDREN,
                true,
                filteredNodeIds => {
                    this.setState({
                        markedCenterNodeIds: filteredNodeIds
                    })
                }
            )
        }
    }

    private computeReferencingCenterNodes = (selection: string[]): void => {
        if (selection === undefined || selection.length === 0) {
            this.setState({
                markedCenterNodeIds: []
            })
        } else {
            filterByReferencingNodes(
                this.props.client,
                this.props.databaseId,
                this.props.hierarchicalGraphId,
                selection,
                this.state.expandedCenterNodeIds,
                NodesToConsider.SELF_AND_CHILDREN,
                true,
                filteredNodeIds => {
                    this.setState({
                        markedCenterNodeIds: filteredNodeIds
                    })
                }
            )
        }
    }

    private hiddenSelectionMessageBox = (showMessageBox: boolean): React.ReactNode => {
        return showMessageBox ?
            <Alert
                style={{zIndex: 10, position: "absolute", width: "100%", bottom: 0, left: 0}}
                message="The current selection is filtered and therefore not visible"
                description={"Select a marked item in the center tree to make the current select visible again."}
                type="error"
                closable={true}
            /> :
            null;
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

    private mergeState = (state: ISlizaaCrossReferenceViewerState, toMerge: any): ISlizaaCrossReferenceViewerState => {
        return {...state, ...toMerge}
    }
}