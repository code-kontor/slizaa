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
import { Action } from 'redux';

export const ACTION_SELECT_DATABASE = 'ACTION_SELECT_DATABASE';
export const ACTION_SELECT_HIERARCHICAL_GRAPH = 'ACTION_SELECT_HIERARCHICAL_GRAPH';

// dependencies view
export const ACTION__DEPENDENCIES_VIEW__SET_TREE_NODE_SELECTION = 'ACTION__DEPENDENCIES_VIEW__SET_TREE_NODE_SELECTION';
export const ACTION__DEPENDENCIES_VIEW__SET_DSM_SIDEMARKER_SIZE = 'ACTION__DEPENDENCIES_VIEW__SET_DSM_SIDEMARKER_SIZE';
export const ACTION__DEPENDENCIES_VIEW__SET_DEPENDENCY_SELECTION = 'ACTION__DEPENDENCIES_VIEW__SET_DEPENDENCY_SELECTION';

export interface IActionSelectDatabase extends Action {
  type: 'ACTION_SELECT_DATABASE';
  selectedDatabaseId: string;
}

export interface IActionSetDsmSidemarkerSize extends Action {
  type: 'ACTION__DEPENDENCIES_VIEW__SET_DSM_SIDEMARKER_SIZE';
  horizontalSideMarkerHeight: number;
  verticalSideMarkerWidth: number;
}

export interface IActionSelectHierarchicalGraph extends Action {
  type: 'ACTION_SELECT_HIERARCHICAL_GRAPH';
  selectedHierarchicalGraphId: string;
}

export interface IActionSetTreeNodeSelection extends Action {
  type: 'ACTION__DEPENDENCIES_VIEW__SET_TREE_NODE_SELECTION';
  selectedNodeIds: string[];
  expandedNodeIds: string[];
}

export interface IActionSetDependencySelection extends Action {
  type: 'ACTION__DEPENDENCIES_VIEW__SET_DEPENDENCY_SELECTION';
  sourceNode: string;
  targetNode: string;
  weight: number;
}

export type AppActions =
  IActionSelectDatabase |
  IActionSelectHierarchicalGraph |
  IActionSetTreeNodeSelection |
  IActionSetDsmSidemarkerSize |
  IActionSetDependencySelection;

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

export function actionSet_DependenciesView_TreeNodeSelection(expNodeIds: string[], selNodeIds: string[]): IActionSetTreeNodeSelection {
  return {
    expandedNodeIds: expNodeIds,
    selectedNodeIds: selNodeIds,
    type: ACTION__DEPENDENCIES_VIEW__SET_TREE_NODE_SELECTION
  };
}

export function actionSet_DependenciesView_DependencySelection(aSourceNode: string, aTargetNode: string, aWeight: number): IActionSetDependencySelection {
  return {
    sourceNode: aSourceNode,
    targetNode: aTargetNode,
    type: ACTION__DEPENDENCIES_VIEW__SET_DEPENDENCY_SELECTION,
    weight: aWeight,
  };
}

export function action_DependenciesView_SetDsmSidemarkerSize(horizontalHeight: number, verticalWidth: number): IActionSetDsmSidemarkerSize {
  return {
    horizontalSideMarkerHeight: horizontalHeight,
    type: ACTION__DEPENDENCIES_VIEW__SET_DSM_SIDEMARKER_SIZE,
    verticalSideMarkerWidth: verticalWidth,
  };
}
