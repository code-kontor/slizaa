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
export interface IDsmProps {
    labels: IDsmLabel[];
    cells: IDsmCell[];
    stronglyConnectedComponents: IDsmStronglyConnectedComponent[];
    horizontalBoxSize?: number;
    verticalBoxSize?: number;
    horizontalSideMarkerHeight: number;
    verticalSideMarkerWidth: number;
    onSideMarkerResize?: (horizontalSideMarkerHeight: number | undefined, verticalSideMarkerWidth: number | undefined) => void;
    // TODO: Callback-Signature - Generics in TypeScript?
    onHover?:  (columnElementId: string | undefined, rowElementId: string | undefined) => void;
    onSelect?: (columnElementId: string | undefined, rowElementId: string | undefined) => void;
}

export interface IDsmLabel {
    id: string;
    text: string;
    iconUrl?: string;
}

export interface IDsmCell {
    row: number;
    column: number;
    value: number;
}

export interface IDsmStronglyConnectedComponent {
    nodePositions: number[];
}