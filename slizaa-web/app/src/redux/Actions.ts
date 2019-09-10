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
import {Action} from 'redux';

export const ACTION_SELECT_DATABASE = 'ACTION_SELECT_DATABASE';
export const ACTION_SELECT_HIERARCHICAL_GRAPH = 'ACTION_SELECT_HIERARCHICAL_GRAPH';

export interface IActionSelectDatabase extends Action {
    type: 'ACTION_SELECT_DATABASE';
    selectedDatabaseId: string;
}

export interface IActionSelectHierarchicalGraph extends Action {
    type: 'ACTION_SELECT_HIERARCHICAL_GRAPH';
    selectedHierarchicalGraphId: string;
}

export type AppActions =
    IActionSelectDatabase |
    IActionSelectHierarchicalGraph;

export function actionSelectDatabase(selectedDatabaseId: string): IActionSelectDatabase {
    return {
        selectedDatabaseId,
        type: ACTION_SELECT_DATABASE
    };
}

export function actionSelectHierarchicalGraph(selectedHierarchicalGraphId: string): IActionSelectHierarchicalGraph {
    return {
        selectedHierarchicalGraphId,
        type: ACTION_SELECT_HIERARCHICAL_GRAPH
    };
}