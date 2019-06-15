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

import io.codekontor.slizaa.server.service.backend.IModifiableBackendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
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
@Profile("!offline")
public class SlizaaServerBackendImpl extends AbstractSlizaaServerBackendImpl implements IModifiableBackendService, IBackendServiceInstanceProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlizaaServerBackendImpl.class);

    @Autowired
    private ISlizaaServerBackendDao _slizaaServerBackendDao;

    @Autowired(required = false)
    private IBackendServiceCallback _backendServiceCallback;

    @PostConstruct
    public void initialize() {

        //
        List<IExtension> installedExtensions = _slizaaServerBackendDao.getInstalledExtensions();
        if (!installedExtensions.isEmpty()) {
            updateBackendConfiguration(installedExtensions);
        }
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

//    protected boolean configureBackendFromDao() {
//
//        try {
//
//            DynamicallyLoadedExtensions newDynamicallyLoadedExtensions = new DynamicallyLoadedExtensions(
//                    dynamicallyLoadExtensions(_slizaaServerBackendDao.getInstalledExtensions()));
//
//            this._dynamicallyLoadedExtensions = newDynamicallyLoadedExtensions;
//            this._dynamicallyLoadedExtensions.initialize();
//
//            return true;
//
//        } catch (Exception ex) {
//            LOGGER.error("Could not load extensions from backend data store.", ex);
//            return false;
//        }
//    }

    @Override
    protected void updateBackendConfiguration(List<IExtension> extensionsToInstall) {
        super.updateBackendConfiguration(extensionsToInstall);
        _slizaaServerBackendDao.saveInstalledExtensions(extensionsToInstall);
    }
}
