/**
 * slizaa-server-service-backend - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.backend.impl;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import io.codekontor.slizaa.server.service.backend.IModifiableBackendService;
import org.springframework.core.io.FileSystemResource;

import com.google.common.io.ByteStreams;

import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import io.codekontor.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import io.codekontor.slizaa.scanner.api.graphdb.IGraphDbFactory;
import io.codekontor.slizaa.scanner.api.importer.IModelImporterFactory;
import io.codekontor.slizaa.scanner.spi.parser.IParserFactory;
import io.codekontor.slizaa.server.service.backend.IBackendService;
import io.codekontor.slizaa.server.service.backend.IBackendServiceInstanceProvider;
import io.codekontor.slizaa.server.service.extensions.IExtension;

public class DummyBackendService implements IModifiableBackendService, IBackendServiceInstanceProvider {

  @Override
  public boolean hasInstalledExtensions() {
    return true;
  }

  @Override
  public List<IExtension> getInstalledExtensions() {
    return Collections.emptyList();
  }

  @Override
  public boolean installExtensions(List<IExtension> extensions) {
    // ignore
    return false;
  }

  @Override
  public ClassLoader getCurrentExtensionClassLoader() {
    return this.getClass().getClassLoader();
  }

  @Override
  public byte[] loadResourceFromExtensions(String path) {

    File file = new File(System.getProperty("user.dir") + File.separatorChar + "src" + File.separatorChar + "test"
        + File.separatorChar + "resources" + File.separatorChar + path);
    System.out.println(file.getAbsolutePath());
    if (file.exists()) {
      FileSystemResource fileSystemResource = new FileSystemResource(file.getAbsolutePath());
      if (fileSystemResource.exists()) {
        try {
          return ByteStreams.toByteArray(fileSystemResource.getInputStream());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return null;
  }

  @Override
  public boolean hasModelImporterFactory() {
    return false;
  }

  @Override
  public IModelImporterFactory getModelImporterFactory() {
    return null;
  }

  @Override
  public boolean hasGraphDbFactory() {
    return false;
  }

  @Override
  public IGraphDbFactory getGraphDbFactory() {
    return null;
  }

  @Override
  public ICypherStatementRegistry getCypherStatementRegistry() {
    return null;
  }

  @Override
  public List<IParserFactory> getParserFactories() {
    return null;
  }

  @Override
  public List<IMappingProvider> getMappingProviders() {
    return null;
  }

}
