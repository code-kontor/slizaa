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

import { Icon, Tree } from 'antd';
import { AntTreeNode, AntTreeNodeCheckedEvent, AntTreeNodeExpandedEvent, AntTreeNodeSelectedEvent } from "antd/lib/tree";
import * as React from 'react';
import { ISlizaaNode } from 'src/model/ISlizaaNode';
import { ISTreeProps } from './ISTreeProps';
import { ISTreeState } from './ISTreeState';
import './STree.css';

export class STree extends React.Component<ISTreeProps, ISTreeState> {

  constructor(props: ISTreeProps) {
    super(props);

    this.state = {
      checkedKeys: props.checkedKeys ? props.checkedKeys : [],
      expandedKeys: props.expandedKeys ? props.expandedKeys : [],
      focusedNodes: [],
      rootNodes: [props.rootNode]
    };
  }

  public onExpand = (newExpandedKeys: string[], e: AntTreeNodeExpandedEvent) => {

    const expandedKey = e.node.props.dataRef.key;

    if (e.expanded) {
      this.state.expandedKeys.push(expandedKey);
    } else {
      const index = this.state.expandedKeys.indexOf(expandedKey);
      if (index > -1) {
        this.state.expandedKeys.splice(index, 1);
      }
    }

    this.setState({
      expandedKeys: [...this.state.expandedKeys]
    });
    if (this.props.onExpand) {
      this.props.onExpand(this.state.expandedKeys);
    }
  }

  public onCheck = (checkedItems: ICheckedItems, e: AntTreeNodeCheckedEvent): void => {
    // TODO: multiple check
    const newCheckedKeys = !e.node.props.checked ? [e.node.props.dataRef.key] : [];
    this.setState({
      checkedKeys: newCheckedKeys
    });
    if (this.props.onSelect) {
      this.props.onSelect(newCheckedKeys);
    }
  }

  public onSelect = (selectedKeys: string[], info: AntTreeNodeSelectedEvent) => {
    this.setState({
      focusedNodes: selectedKeys
    });
    if (this.props.onSelect) {
      this.props.onSelect(selectedKeys);
    }
  }

  public loadData = (treeNode: AntTreeNode) => {



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

  public renderTreeNodes = (treeNodes: ISlizaaNode[]) => {

    return treeNodes.map((item: ISlizaaNode) => {

      const isExpanded = this.state.expandedKeys.indexOf(item.key) > -1;
      const childNodes = isExpanded ? this.renderTreeNodes(item.children()) : null;

      return (
        <Tree.TreeNode
          dataRef={item}
          key={item.key}
          title={item.title}
          icon={this.fetchIcon(item)}
          isLeaf={!item.hasChildren}
          className={'slizaa-tree'}
        >
          {childNodes}
        </Tree.TreeNode>
      );
      ;
    });
  }

  public render() {

    return (
      <Tree
        checkable={false}
        checkStrictly={false}
        defaultExpandedKeys={this.state.expandedKeys}
        expandedKeys={this.state.expandedKeys}
        defaultCheckedKeys={this.state.checkedKeys}
        checkedKeys={this.state.checkedKeys}
        autoExpandParent={true}
        multiple={false}
        selectable={true}
        onSelect={this.onSelect}
        onExpand={this.onExpand}
        onCheck={this.onCheck}
        loadData={this.loadData}
        showIcon={true}
        showLine={false}
        style={{ overflow: "auto" }}
      >
        {this.renderTreeNodes(this.state.rootNodes)}
      </Tree>
    );
  }

  private fetchIcon(item: ISlizaaNode): React.ReactNode {
    if (this.props.fetchIcon) {
      return this.props.fetchIcon(item);
    }
    return <Icon type="question" />;
  }
}
interface ICheckedItems {
  checked: string[];
  halfChecked: string[];
}

export default STree;