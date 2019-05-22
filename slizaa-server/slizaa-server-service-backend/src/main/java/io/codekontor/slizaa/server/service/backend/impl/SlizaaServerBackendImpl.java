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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.google.common.io.ByteStreams;

import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import io.codekontor.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import io.codekontor.slizaa.scanner.api.graphdb.IGraphDbFactory;
import io.codekontor.slizaa.scanner.api.importer.IModelImporterFactory;
import io.codekontor.slizaa.scanner.spi.parser.IParserFactory;
import io.codekontor.slizaa.server.service.backend.IBackendService;
import io.codekontor.slizaa.server.service.backend.IBackendServiceCallback;
import io.codekontor.slizaa.server.service.backend.IBackendServiceInstanceProvider;
import io.codekontor.slizaa.server.service.backend.impl.dao.ISlizaaServerBackendDao;
import io.codekontor.slizaa.server.service.extensions.IExtension;

/**
 * <p>
 * </p>
 */
@Component
public class SlizaaServerBackendImpl implements IBackendService, IBackendServiceInstanceProvider {

  @Autowired
  private ISlizaaServerBackendDao     _slizaaServerBackendDao;

  @Autowired(required = false)
  private IBackendServiceCallback     _backendServiceCallback;

  /* the dynamically loaded extensions */
  private DynamicallyLoadedExtensions _dynamicallyLoadedExtensions;

  @PostConstruct
  public void initialize() {

    //
    List<IExtension> installedExtensions = _slizaaServerBackendDao.getInstalledExtensions();

    //
    if (!installedExtensions.isEmpty()) {
      configureBackendFromDao();
    }
  }

  @Override
  public ClassLoader getCurrentExtensionClassLoader() {
    return _dynamicallyLoadedExtensions != null ? _dynamicallyLoadedExtensions.getClassLoader() : null;
  }

  @Override
  public List<IExtension> getInstalledExtensions() {
    return _slizaaServerBackendDao.getInstalledExtensions();
  }

  @Override
  public void installExtensions(List<IExtension> extensionsToInstall) {

    checkNotNull(extensionsToInstall);

    if (_backendServiceCallback != null) {
      _backendServiceCallback.beforeInstallExtensions(extensionsToInstall);
    }

    updateBackendConfiguration(extensionsToInstall);
  }

  @Override
  public void installAllExtensions() {
    installExtensions(Collections.emptyList());
  }

  @Override
  public boolean hasInstalledExtensions() {
    return _dynamicallyLoadedExtensions != null;
  }

  @Override
  public boolean hasModelImporterFactory() {
    checkState(hasInstalledExtensions(), "SlizaaServerBackend has no installed extensions.");
    return _dynamicallyLoadedExtensions.hasModelImporterFactory();
  }

  @Override
  public IModelImporterFactory getModelImporterFactory() {
    checkState(hasInstalledExtensions(), "SlizaaServerBackend has no installed extensions.");
    return _dynamicallyLoadedExtensions.getModelImporterFactory();
  }

  @Override
  public boolean hasGraphDbFactory() {
    checkState(hasInstalledExtensions(), "SlizaaServerBackend has no installed extensions.");
    return _dynamicallyLoadedExtensions.hasGraphDbFactory();
  }

  @Override
  public IGraphDbFactory getGraphDbFactory() {
    checkState(hasInstalledExtensions(), "SlizaaServerBackend has no installed extensions.");
    return _dynamicallyLoadedExtensions.getGraphDbFactory();
  }

  @Override
  public ICypherStatementRegistry getCypherStatementRegistry() {
    checkState(hasInstalledExtensions(), "SlizaaServerBackend has no installed extensions.");
    return _dynamicallyLoadedExtensions.getCypherStatementRegistry();
  }

  @Override
  public List<IParserFactory> getParserFactories() {
    checkState(hasInstalledExtensions(), "SlizaaServerBackend has no installed extensions.");
    return _dynamicallyLoadedExtensions.getParserFactories();
  }

  @Override
  public List<IMappingProvider> getMappingProviders() {
    checkState(hasInstalledExtensions(), "SlizaaServerBackend has no installed extensions.");
    return _dynamicallyLoadedExtensions.getMappingProviders();
  }

  @Override
  public byte[] loadResourceFromExtensions(String path) {

    if (hasInstalledExtensions()) {

      ClassPathResource imgFile = new ClassPathResource(path, getCurrentExtensionClassLoader());

      if (imgFile.exists()) {
        try {
          return ByteStreams.toByteArray(imgFile.getInputStream());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }

  protected boolean configureBackendFromDao() {

    try {

      DynamicallyLoadedExtensions newDynamicallyLoadedExtensions = new DynamicallyLoadedExtensions(
          dynamicallyLoadExtensions(_slizaaServerBackendDao.getInstalledExtensions()));

      this._dynamicallyLoadedExtensions = newDynamicallyLoadedExtensions;
      this._dynamicallyLoadedExtensions.initialize();

      return true;

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  protected void updateBackendConfiguration(List<IExtension> extensionsToInstall) {

    try {

      DynamicallyLoadedExtensions newDynamicallyLoadedExtensions = null;

      if (!extensionsToInstall.isEmpty()) {
        newDynamicallyLoadedExtensions = new DynamicallyLoadedExtensions(
            dynamicallyLoadExtensions(extensionsToInstall));
      }

      if (this._dynamicallyLoadedExtensions != null) {
        this._dynamicallyLoadedExtensions.dispose();
        this._dynamicallyLoadedExtensions = null;
      }

      if (!extensionsToInstall.isEmpty()) {
        this._dynamicallyLoadedExtensions = newDynamicallyLoadedExtensions;
        this._dynamicallyLoadedExtensions.initialize();
      }

      _slizaaServerBackendDao.saveInstalledExtensions(extensionsToInstall);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  private static ClassLoader dynamicallyLoadExtensions(List<IExtension> extensionToDynamicallyLoad) {

    List<URL> urlList = checkNotNull(extensionToDynamicallyLoad).stream()
        .flatMap(ext -> ext.resolvedArtifactsToInstall().stream()).distinct().collect(Collectors.toList());

    return new URLClassLoader(urlList.toArray(new URL[0]), SlizaaServerBackendImpl.class.getClassLoader());
  }
}
