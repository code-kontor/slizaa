/**
 * slizaa-server-graphql - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.graphql.serverconfig;

import java.util.List;
import java.util.stream.Collectors;

import io.codekontor.slizaa.server.service.backend.IBackendService;
import io.codekontor.slizaa.server.service.backend.IModifiableBackendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;

import io.codekontor.slizaa.server.service.extensions.ExtensionIdentifier;
import io.codekontor.slizaa.server.service.extensions.IExtension;
import io.codekontor.slizaa.server.service.extensions.IExtensionIdentifier;
import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;

@Component
public class ServerConfigMutation implements GraphQLMutationResolver {

  @Autowired
  private ISlizaaService slizaaService;

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public List<ServerExtension> installServerExtensions(List<ServerExtension> serverExtensions) {

    // retrieve the extensions...
    List<IExtensionIdentifier> extensionIds = serverExtensions.stream()
        .map(ext -> new ExtensionIdentifier(ext.getSymbolicName(), ext.getVersion())).collect(Collectors.toList());

    List<IExtension> extensions = slizaaService.getExtensionService().getExtensions(extensionIds);

    // ... and install it
    IBackendService backendService = slizaaService.getBackendService();

    if (backendService  instanceof IModifiableBackendService) {
      ((IModifiableBackendService) backendService).installExtensions(extensions);
    }

    //
    return extensions.stream().map(ext -> new ServerExtension(ext.getSymbolicName(), ext.getVersion().toString()))
        .collect(Collectors.toList());
  }
}
