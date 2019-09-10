/* tslint:disable */
/* eslint-disable */
// This file was automatically generated and should not be edited.

// ====================================================
// GraphQL query operation: HasInstalledExtensions
// ====================================================

export interface HasInstalledExtensions_serverConfiguration {
  __typename: "ServerConfiguration";
  /**
   * Returns true if the backend contains installed extensions.
   */
  hasInstalledExtensions: boolean;
}

export interface HasInstalledExtensions {
  /**
   * Returns the server configuration
   */
  serverConfiguration: HasInstalledExtensions_serverConfiguration;
}
