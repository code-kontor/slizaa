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
import '../src/SlizaaApp.css'

import { storiesOf } from '@storybook/react';
import * as React from 'react';
import STree from '../src/components/stree/STree';
import { SlizaaNode } from '../src/model/SlizaaNode';

storiesOf('STree', module)
  .add('Simple STree', () => (
    <STree rootNode={SlizaaNode.createRoot("Root", "default")} checkedKeys={["2-3","2-4"]} expandedKeys={["-1", "2", "2-3"]} loadData={fetchChildrenFunc()} />
  ));

function fetchChildrenFunc(): (parent: SlizaaNode) => Promise<{}> {
  return (parent) => {
    return new Promise(res => parent.internalChildren = [node(parent, "1"), node(parent, "2"), node(parent, "3"), node(parent, "4")]);
  }
}

function node(parent: SlizaaNode, postfix: string): SlizaaNode {
  const prefix = parent.key !== "-1" ? parent.key + "-" : "";
  return SlizaaNode.createNode(prefix + postfix, prefix + postfix, "default", true);
} 