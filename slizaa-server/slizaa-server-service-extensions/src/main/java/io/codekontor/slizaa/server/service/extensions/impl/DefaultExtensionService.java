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
package io.codekontor.slizaa.server.service.extensions.impl;

import io.codekontor.slizaa.server.service.extensions.IExtension;
import io.codekontor.slizaa.server.service.extensions.IExtensionIdentifier;
import io.codekontor.slizaa.server.service.extensions.IExtensionService;
import io.codekontor.slizaa.server.service.extensions.Version;
import io.codekontor.slizaa.server.service.extensions.mvn.MvnBasedExtension;
import io.codekontor.slizaa.server.service.extensions.mvn.MvnDependency;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Component
public class DefaultExtensionService implements IExtensionService {

    /* - */
    private MvnBasedExtension _jtypeExtension = new MvnBasedExtension("io.codekontor.slizaa.extensions.jtype", new Version(1, 0, 0))
            .withDependency(new MvnDependency("io.codekontor.slizaa.extensions:slizaa-extensions-jtype:1.0.0-SNAPSHOT"));

    /* - */
    private List<IExtension> _extensionList = Arrays.asList(_jtypeExtension);

    /**
     * @return
     */
    @Override
    public List<IExtension> getExtensions() {
        return Collections.unmodifiableList(_extensionList);
    }

    @Override
    public List<IExtension> getExtensions(List<? extends IExtensionIdentifier> extensionIdentifiers) {
        return _extensionList.stream().filter(ext -> extensionIdentifiers.contains(ext)).collect(Collectors.toList());
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        //
        new DefaultExtensionService()._jtypeExtension.resolvedArtifactsToInstall().forEach(url -> System.out.println(url.toString()));
    }
}
