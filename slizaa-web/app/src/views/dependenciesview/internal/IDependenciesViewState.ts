export interface IDependenciesViewState {
    layout: IIDependenciesViewLayout
    mainTreeNodeSelection: ITreeNodeSelection
    mainDependencySelection?: IDependencySelection
    dependenciesTree?: IDependenciesTreeSelection
}

export enum NodeType { SOURCE, TARGET }

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
    targetNodeId: string;
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

