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
import {NodeType} from "../../../model/NodeType";

export interface IDependenciesViewState {
    databaseId: string
    hierarchicalGraphId: string
    layout: IIDependenciesViewLayout
    mainTreeNodeSelection: ITreeNodeSelection
    mainDependencySelection?: IDependencySelection
    dependenciesTree?: IDependenciesTreeSelection
}

export interface IDependenciesTreeSelection {
    selectionNodeType: NodeType,
    sourceTreeNodeSelection: ITreeNodeSelection,
    targetTreeNodeSelection: ITreeNodeSelection,
}

export interface ITreeNodeSelection {
    selectedNodeIds: string[]
    expandedNodeIds: string[]
}

export interface IDependencySelection {
    sourceNodeId: string;
    sourceNodeText: string;
    targetNodeId: string;
    targetNodeText: string;
    weight: number;
}

export interface IIDependenciesViewLayout {
    treeWidth: number
    upperHeight: number
    lowerHeight: number
    dsmSetting: IDsmSettings
}

export interface IDsmSettings {
    horizontalSideMarkerHeight: number;
    verticalSideMarkerWidth: number;
}

