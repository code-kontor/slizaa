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
import { AppActions } from './Actions';
import { defaultState, IAppState } from './IAppState';

export function appReducer(state: IAppState = defaultState(), action: AppActions): IAppState {

    //
    if (action.type === "ACTION_SELECT_DATABASE") {
        return {
            ...state,
            currentDatabase: action.selectedDatabaseId
        }
    }
    else if (action.type === "ACTION_SELECT_HIERARCHICAL_GRAPH") {
        return {
            ...state,
            currentHierarchicalGraph: action.selectedHierarchicalGraphId
        }
    }
    else if (action.type === "ACTION__DEPENDENCIES_VIEW__SET_TREE_NODE_SELECTION") {
        return {
            ...state,
            dependenciesViewState: {
                ...state.dependenciesViewState,
                treeNodeSelection: 
                {   
                    expandedNodeIds: action.expandedNodeIds,
                    selectedNodeIds: action.selectedNodeIds,
                }
            }
        }
    }
    else if (action.type === "ACTION__DEPENDENCIES_VIEW__SET_DEPENDENCY_SELECTION") {
        return {
            ...state,
            dependenciesViewState: {
                ...state.dependenciesViewState,
                selectedDependency: 
                {   
                    sourceNode: action.sourceNode,
                    targetNode: action.targetNode,
                    weight: action.weight,
                }
            }
        }
    }
    else if (action.type === "ACTION__DEPENDENCIES_VIEW__SET_DSM_SIDEMARKER_SIZE") {
        return {
            ...state,
            dependenciesViewState: {
                ...state.dependenciesViewState,
                dsmSettings: {
                    horizontalSideMarkerHeight: action.horizontalSideMarkerHeight,
                    verticalSideMarkerWidth: action.verticalSideMarkerWidth
                }
            }
        }
    }

    return state;
}