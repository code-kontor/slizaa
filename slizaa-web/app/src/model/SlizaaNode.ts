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

import { ISlizaaNode } from './ISlizaaNode';

/**
 * Represents a tree node.
 */
export class SlizaaNode implements ISlizaaNode {

    public static createRoot(title: string, iconId: string): SlizaaNode {
        return new SlizaaNode("-1", title, iconId, true);
    }

    public static createNode(id: string, title: string, iconId: string, hasChildren: boolean): SlizaaNode {
        return new SlizaaNode(id, title, iconId, hasChildren);
    }

    public expanded?: boolean;

    public internalChildren: SlizaaNode[] | undefined;

    private constructor(public key: string, public title: string, public iconId: string, public hasChildren: boolean) { }

    public children(): SlizaaNode[] {
        return this.internalChildren ? this.internalChildren : [];
    }
}