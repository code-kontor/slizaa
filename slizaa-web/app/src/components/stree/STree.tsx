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

import {Icon, Popover, Tree} from 'antd';
import {AntTreeNode, AntTreeNodeExpandedEvent, AntTreeNodeSelectedEvent} from "antd/lib/tree";
import * as React from 'react';
import * as ReactDOM from "react-dom";
import {ISlizaaNode} from 'src/model/ISlizaaNode';
import {ISTreeProps} from './ISTreeProps';
import {ISTreeState} from './ISTreeState';
import './STree.css';

export class STree extends React.Component<ISTreeProps, ISTreeState> {

    private treeRef: Tree | null;
    private nodeCache: Map<string, ISlizaaNode>;
    private scrollTop: number;
    private scrollLeft: number;

    constructor(props: ISTreeProps) {
        super(props);

        this.nodeCache = new Map();

        this.state = {
            expandedNodeIds: props.expandedKeys ? props.expandedKeys : [],
            rootNodes: [props.rootNode],
            selectedNodeIds: props.selectedKeys ? props.selectedKeys : [],
        };
    }

    public componentWillUpdate(nextProps: Readonly<ISTreeProps>, nextState: Readonly<ISTreeState>, nextContext: any): void {
        this.saveScrollPosition();
    }

    public componentDidUpdate(prevProps: Readonly<ISTreeProps>, prevState: Readonly<ISTreeState>, snapshot?: any): void {
        this.restoreScrollPosition();
    }

    public componentWillReceiveProps(nextProps: ISTreeProps) {
        this.setState({
            expandedNodeIds: nextProps.expandedKeys ? nextProps.expandedKeys : [],
            rootNodes: [nextProps.rootNode],
            selectedNodeIds: nextProps.selectedKeys ? nextProps.selectedKeys : [],
        });
    }

    public renderTreeNodes = (treeNodes: ISlizaaNode[]) => {

        return treeNodes.map((item: ISlizaaNode) => {
            this.nodeCache.set(item.key, item);

            const isSelected = this.state.selectedNodeIds.indexOf(item.key) > -1;
            const isExpanded = this.state.expandedNodeIds.indexOf(item.key) > -1;
            const childNodes = isExpanded ? this.renderTreeNodes(item.children()) : null;

            let className = 'slizaa-tree slizaa-tree-node ';

            if (this.props.markedKeys !== undefined) {
                className = this.props.markedKeys.indexOf(item.key) > -1 ? className + 'slizaa-tree-node-marked' : className + 'slizaa-tree-node-unmarked';
            }

            return (
                <Tree.TreeNode
                    dataRef={item}
                    key={item.key}
                    title={<Popover placement="right" content={<div>Content</div>} title={item.title} mouseEnterDelay={1.5} >
                       {item.title}
                    </Popover>}
                    icon={this.fetchIcon(item)}
                    isLeaf={!item.hasChildren}
                    className={className}
                    selected={isSelected}
                >
                    {childNodes}
                </Tree.TreeNode>
            );
        });
    }

    public render() {

        return (
            <Tree
                ref={elem => this.treeRef = elem}
                checkable={false}
                checkStrictly={false}
                expandedKeys={this.state.expandedNodeIds}
                defaultExpandedKeys={this.state.expandedNodeIds}
                selectedKeys={this.state.selectedNodeIds}
                defaultSelectedKeys={this.state.selectedNodeIds}
                autoExpandParent={true}
                multiple={false}
                selectable={true}
                onSelect={this.onSelect}
                onExpand={this.onExpand}
                loadData={this.loadData}
                showIcon={true}
                showLine={false}
                key={this.createUniqueId(10)}
            >
                {this.renderTreeNodes(this.state.rootNodes)}
            </Tree>
        );
    }

    private saveScrollPosition = (): void => {
        if (this.treeRef) {
            const domNode = ReactDOM.findDOMNode(this.treeRef);
            if (domNode && domNode instanceof Element) {
                const element = domNode as Element;
                this.scrollTop = element.scrollTop;
                this.scrollLeft = element.scrollLeft;
            }
        }
    }

    private restoreScrollPosition = (): void => {
        if (this.treeRef) {
            const domNode = ReactDOM.findDOMNode(this.treeRef);
            if (domNode && domNode instanceof Element) {
                const element = domNode as Element;
                element.scrollTop = this.scrollTop;
                element.scrollLeft = this.scrollLeft;
            }
        }
    }

    private loadData = (treeNode: AntTreeNode) => {
        return Promise.resolve(true).then(async (resolve) => {
            if (this.props.loadData) {

                await this.props.loadData(treeNode.props.dataRef, () => {
                    this.setState({
                        rootNodes: [...this.state.rootNodes]
                    });
                });
            }
        });
    }

    private onExpand = (newExpandedKeys: string[], e: AntTreeNodeExpandedEvent) => {
        const expandedKey = e.node.props.dataRef.key;
        if (e.expanded) {
            this.state.expandedNodeIds.push(expandedKey);
        } else {
            const index = this.state.expandedNodeIds.indexOf(expandedKey);
            if (index > -1) {
                this.state.expandedNodeIds.splice(index, 1);
            }
        }

        this.setState({
            expandedNodeIds: [...this.state.expandedNodeIds]
        });
        if (this.props.onExpand) {
            this.props.onExpand(this.state.expandedNodeIds);
        }
    }

    private onSelect = (selectedKeys: string[], info: AntTreeNodeSelectedEvent) => {
        this.setState({
            selectedNodeIds: selectedKeys
        });
        if (this.props.onSelect) {
            const nodes: ISlizaaNode[] = selectedKeys.map((v) => this.nodeCache.get(v)).filter((v) => v !== undefined) as ISlizaaNode[];
            this.props.onSelect(nodes);
        }
    }


    private fetchIcon(item: ISlizaaNode): React.ReactNode {
        if (this.props.fetchIcon) {
            return this.props.fetchIcon(item);
        }
        return <Icon type="question"/>;
    }

    private createUniqueId = (length: number): string => {
        let result = '';
        const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        const charactersLength = characters.length;
        for (let i = 0; i < length; i++) {
            result += characters.charAt(Math.floor(Math.random() * charactersLength));
        }
        return result;
    }
}

export default STree;