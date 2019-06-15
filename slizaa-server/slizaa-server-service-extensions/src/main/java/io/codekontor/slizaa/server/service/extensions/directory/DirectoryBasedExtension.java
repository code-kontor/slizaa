/**
 * slizaa-server-service-extensions - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.extensions.directory;

import com.google.common.base.Preconditions;
import io.codekontor.slizaa.server.service.extensions.ExtensionIdentifier;
import io.codekontor.slizaa.server.service.extensions.IExtension;
import io.codekontor.slizaa.server.service.extensions.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DirectoryBasedExtension extends ExtensionIdentifier implements IExtension {

    private static final Logger LOGGER = LoggerFactory.getLogger(DirectoryBasedExtension.class);

    public static DirectoryBasedExtension createDirectoryBasedExtension(File directory) {

        Preconditions.checkNotNull(directory);

        try {
            String[] splittedString = directory.getName().split("_");
            if (splittedString.length == 2) {
                String name = splittedString[0];
                Version version = new Version(splittedString[1].replace('-', '.'));
                return new DirectoryBasedExtension(name, version, directory);
            }
        } catch (Exception ex) {
            LOGGER.warn("Could not load DirectoryBasedExtension for directory '%s'.", ex);
        }
        return null;
    }

    private File _directory;

    public DirectoryBasedExtension(String symbolicName, Version version, File directory) {
        super(symbolicName, version);
        _directory = Preconditions.checkNotNull(directory);
    }

    @Override
    public List<URL> resolvedArtifactsToInstall() {

        // fetch all files
        File[] children = _directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File child) {
                return child.getName().endsWith("jar");
            }
        });

        // create result
        List<URL> result = new ArrayList<>();
        for (File child : children) {
            try {
                result.add(child.toURI().toURL());
            } catch (MalformedURLException e) {
                LOGGER.error("Could not resolve artifact '%s'.", child);
            }
        }

        // return the result
        return result;
    }
}
