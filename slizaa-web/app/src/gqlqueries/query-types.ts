/* tslint:disable */
/* eslint-disable */
// @generated
// This file was automatically generated and should not be edited.

//==============================================================
// START Enums and Input Objects
//==============================================================

export enum NodeType {
  SOURCE = "SOURCE",
  TARGET = "TARGET",
}

export enum NodesToConsider {
  SELF = "SELF",
  SELF_AND_CHILDREN = "SELF_AND_CHILDREN",
  SELF_AND_SUCCESSORS = "SELF_AND_SUCCESSORS",
}

export interface NodeSelection {
  selectedNodeIds: string[];
  selectedNodesType: NodeType;
}

//==============================================================
// END Enums and Input Objects
//==============================================================
