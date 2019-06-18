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

import com.google.common.io.ByteStreams;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import io.codekontor.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import io.codekontor.slizaa.scanner.api.graphdb.IGraphDbFactory;
import io.codekontor.slizaa.scanner.api.importer.IModelImporterFactory;
import io.codekontor.slizaa.scanner.spi.parser.IParserFactory;
import io.codekontor.slizaa.server.service.backend.IBackendService;
import io.codekontor.slizaa.server.service.backend.IBackendServiceCallback;
import io.codekontor.slizaa.server.service.backend.IBackendServiceInstanceProvider;
import io.codekontor.slizaa.server.service.backend.IModifiableBackendService;
import io.codekontor.slizaa.server.service.backend.impl.dao.ISlizaaServerBackendDao;
import io.codekontor.slizaa.server.service.extensions.IExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * <p>
 * </p>
 */
public abstract class AbstractSlizaaServerBackendImpl implements IBackendService, IBackendServiceInstanceProvider {

    /* LOGGER */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSlizaaServerBackendImpl.class);

    /* the dynamically loaded extensions */
    private DynamicallyLoadedExtensions _dynamicallyLoadedExtensions;


    @Override
    public final ClassLoader getCurrentExtensionClassLoader() {
        return _dynamicallyLoadedExtensions != null ? _dynamicallyLoadedExtensions.getClassLoader() : null;
    }

    @Override
    public boolean hasInstalledExtensions() {
        return _dynamicallyLoadedExtensions != null;
    }

    @Override
    public final boolean hasModelImporterFactory() {
        checkState(hasInstalledExtensions(), "SlizaaServerBackend has no installed extensions.");
        return _dynamicallyLoadedExtensions.hasModelImporterFactory();
    }

    @Override
    public final IModelImporterFactory getModelImporterFactory() {
        checkState(hasInstalledExtensions(), "SlizaaServerBackend has no installed extensions.");
        return _dynamicallyLoadedExtensions.getModelImporterFactory();
    }

    @Override
    public final boolean hasGraphDbFactory() {
        checkState(hasInstalledExtensions(), "SlizaaServerBackend has no installed extensions.");
        return _dynamicallyLoadedExtensions.hasGraphDbFactory();
    }

    @Override
    public final IGraphDbFactory getGraphDbFactory() {
        checkState(hasInstalledExtensions(), "SlizaaServerBackend has no installed extensions.");
        return _dynamicallyLoadedExtensions.getGraphDbFactory();
    }

    @Override
    public final ICypherStatementRegistry getCypherStatementRegistry() {
        checkState(hasInstalledExtensions(), "SlizaaServerBackend has no installed extensions.");
        return _dynamicallyLoadedExtensions.getCypherStatementRegistry();
    }

    @Override
    public final List<IParserFactory> getParserFactories() {
        checkState(hasInstalledExtensions(), "SlizaaServerBackend has no installed extensions.");
        return _dynamicallyLoadedExtensions.getParserFactories();
    }

    @Override
    public final List<IMappingProvider> getMappingProviders() {
        checkState(hasInstalledExtensions(), "SlizaaServerBackend has no installed extensions.");
        return _dynamicallyLoadedExtensions.getMappingProviders();
    }

    @Override
    public final byte[] loadResourceFromExtensions(String path) {

        if (hasInstalledExtensions()) {

            ClassPathResource imgFile = new ClassPathResource(path, getCurrentExtensionClassLoader());

            if (imgFile.exists()) {
                try {
                    return ByteStreams.toByteArray(imgFile.getInputStream());
                } catch (IOException e) {
                    LOGGER.warn(String.format("Could not load resource '%s' from classpath.", e));
                }
            }
        }
        return null;
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    protected final ClassLoader dynamicallyLoadExtensions(List<IExtension> extensionToDynamicallyLoad) {

        List<URL> urlList = checkNotNull(extensionToDynamicallyLoad).stream()
                .flatMap(ext -> ext.resolvedArtifactsToInstall().stream()).distinct().collect(Collectors.toList());

        return new URLClassLoader(urlList.toArray(new URL[0]), AbstractSlizaaServerBackendImpl.class.getClassLoader());
    }

    /**
     *
     * @param extensionsToInstall
     */
    protected boolean updateBackendConfiguration(List<IExtension> extensionsToInstall) {

        checkNotNull(extensionsToInstall);

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
            return true;
        } catch (Exception exception) {
            LOGGER.error("Could not load extensions.", exception);
            return false;
        }
    }
}
