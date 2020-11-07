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
import {SlizaaRootNode} from "./internal/SlizaaRootNode";
import {ISlizaaRootNode} from "./ISlizaaRootNode";

export class SlizaaNodeFactory {

    public static DEFAULT_ICON_ID = "default";

    public static DEFAULT_ROOT_ICON_ID = "default";

    public static createRoot(title: string): ISlizaaRootNode {
        return new SlizaaRootNode("-1", title, SlizaaNodeFactory.DEFAULT_ROOT_ICON_ID, true);
    }
}