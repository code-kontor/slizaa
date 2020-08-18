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

import {Slizaa21SplitViewPositions} from "./Slizaa21SplitViewPositions";

export interface ISlizaa21SplitViewProps {
    id: string,
    topLeft: ISlizaa21SplitViewComponent,
    topRight: ISlizaa21SplitViewComponent,
    bottom: ISlizaa21SplitViewComponent,
    maximizedElement?: Slizaa21SplitViewPositions
}

export interface ISlizaa21SplitViewComponent {
    title: string,
    allowOverflow?: boolean,
    element: React.ReactNode,
    overlayMenuFunc?: () => React.ReactNode,
}
