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

import io.codekontor.slizaa.server.service.backend.IBackendService;
import io.codekontor.slizaa.server.service.backend.IBackendServiceInstanceProvider;
import io.codekontor.slizaa.server.service.backend.extensions.IExtension;
import io.codekontor.slizaa.server.service.backend.extensions.directory.DirectoryBasedExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * <p>
 * </p>
 */
@Component
@Profile("offline")
public class OfflineSlizaaServerBackendImpl extends AbstractSlizaaServerBackendImpl implements IBackendService, IBackendServiceInstanceProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(OfflineSlizaaServerBackendImpl.class);

    private List<IExtension> _directoryBasedExtensions;

    @PostConstruct
    public void initialize() {

        //
        _directoryBasedExtensions = new ArrayList<>();

        Path workingDirectory = Paths.get("backend-extensions").toAbsolutePath();
        if (workingDirectory.toFile().exists() && workingDirectory.toFile().isDirectory()) {

            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(workingDirectory)) {

                //
                for (Path path : directoryStream) {
                    DirectoryBasedExtension directoryBasedExtension = DirectoryBasedExtension.createDirectoryBasedExtension(path.toFile());
                    if (directoryBasedExtension != null) {
                        _directoryBasedExtensions.add(directoryBasedExtension);
                    }
                }

                // finally update the configuration
                this.updateBackendConfiguration(_directoryBasedExtensions);

            } catch (IOException ex) {
                LOGGER.warn("Could not load extensions form extension directory.", ex);
            }
        }

    }

    @Override
    public List<IExtension> getInstalledExtensions() {
        return Collections.unmodifiableList(_directoryBasedExtensions);
    }
}
