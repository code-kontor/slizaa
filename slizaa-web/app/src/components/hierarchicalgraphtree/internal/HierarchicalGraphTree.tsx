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
import { WithApolloClient } from 'react-apollo';
import { SlizaaIcon } from 'src/components/slizaaicon';
import STree from 'src/components/stree/STree';
import { ISlizaaNode } from 'src/model/ISlizaaNode';
import { SlizaaNode } from 'src/model/SlizaaNode';
import { fetchChildren } from 'src/model/SlizaaNodeChildrenResolver';
import { IHierarchicalGraphTreeProps } from './IHierarchicalGraphTreeProps';
import { IHierarchicalGraphTreeState } from './IHierarchicalGraphTreeState';

export class HierarchicalGraphTree extends React.Component<WithApolloClient<IHierarchicalGraphTreeProps>, IHierarchicalGraphTreeState> {

  constructor(props: WithApolloClient<IHierarchicalGraphTreeProps>) {
    super(props);
  }

  public onExpand = (expandedKeys: string[]) => {
    this.props.onExpand(expandedKeys);
  }

  public onSelect = (selectedKeys: string[]) => {
    this.props.onSelect(selectedKeys);
  }

  public fetchIcon = (item: ISlizaaNode): React.ReactNode => {
    return <SlizaaIcon iconId={item.iconId} />
  }

  public loadData = (parent: SlizaaNode, callback: () => void): Promise<{}> => {
    return fetchChildren(this.props.client, parent, this.props.databaseId, this.props.hierarchicalGraphId, callback);
  }

  public render() {
    const rootNode = SlizaaNode.createRoot("Root", "default");
    return (
      <STree
        rootNode={rootNode}
        selectedKeys={this.props.checkedKeys}
        expandedKeys={this.props.expandedKeys}
        onExpand={this.onExpand}
        onSelect={this.onSelect}
        loadData={this.loadData}
        fetchIcon={this.fetchIcon}
      />
    );
  }
}

export default HierarchicalGraphTree;