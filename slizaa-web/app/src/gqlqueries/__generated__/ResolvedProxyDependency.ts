/* tslint:disable */
/* eslint-disable */
// @generated
// This file was automatically generated and should not be edited.

// ====================================================
// GraphQL query operation: ResolvedProxyDependency
// ====================================================

export interface ResolvedProxyDependency_hierarchicalGraph_dependency_resolvedDependencies_sourceNode {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
  /**
   * The text label
   */
  text: string;
  /**
   * the icon identifier
   */
  iconIdentifier: string;
}

export interface ResolvedProxyDependency_hierarchicalGraph_dependency_resolvedDependencies_targetNode {
  __typename: "Node";
  /**
   * The symbolicName for this node
   */
  id: string;
  /**
   * The text label
   */
  text: string;
  /**
   * the icon identifier
   */
  iconIdentifier: string;
}

export interface ResolvedProxyDependency_hierarchicalGraph_dependency_resolvedDependencies {
  __typename: "Dependency";
  id: string;
  type: string;
  weight: number;
  sourceNode: ResolvedProxyDependency_hierarchicalGraph_dependency_resolvedDependencies_sourceNode;
  targetNode: ResolvedProxyDependency_hierarchicalGraph_dependency_resolvedDependencies_targetNode;
}

export interface ResolvedProxyDependency_hierarchicalGraph_dependency {
  __typename: "Dependency";
  id: string;
  resolvedDependencies: ResolvedProxyDependency_hierarchicalGraph_dependency_resolvedDependencies[];
}

export interface ResolvedProxyDependency_hierarchicalGraph {
  __typename: "HierarchicalGraph";
  /**
   * Returns the node with the given id
   */
  dependency: ResolvedProxyDependency_hierarchicalGraph_dependency | null;
}

export interface ResolvedProxyDependency {
  hierarchicalGraph: ResolvedProxyDependency_hierarchicalGraph | null;
}

export interface ResolvedProxyDependencyVariables {
  databaseId: string;
  hierarchicalGraphId: string;
  proxyDependencyId: string;
}
