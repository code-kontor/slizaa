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
import {ISlizaaRootNode} from "../ISlizaaRootNode";
import {SlizaaNode} from "./SlizaaNode";

export class SlizaaRootNode extends SlizaaNode implements ISlizaaRootNode {

    public static ROOT_ID = "-1";

    private nodeCache: Map<string, SlizaaNode>;

    constructor(public key: string, public title: string, public iconId: string, public hasChildren: boolean) {
        super(key, title,  iconId, hasChildren)

        this.nodeCache = new Map();
    }

    public root() : ISlizaaRootNode {
        return this;
    }

    public lookupNode(id: string): SlizaaNode | undefined {
        if (SlizaaRootNode.ROOT_ID === id) {
            return this;
        }
        return this.nodeCache.get(id);
    }

    public register(node: SlizaaNode) {
        this.nodeCache.set(node.key, node);
    }
}