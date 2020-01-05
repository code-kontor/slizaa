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
package io.codekontor.slizaa.server.service.backend.extensions.mvn;

import io.codekontor.mvnresolver.MvnResolverServiceFactoryFactory;
import io.codekontor.mvnresolver.api.IMvnResolverService;
import io.codekontor.mvnresolver.api.IMvnResolverServiceFactory;
import io.codekontor.slizaa.server.service.backend.extensions.ExtensionIdentifier;
import io.codekontor.slizaa.server.service.backend.extensions.IExtension;
import io.codekontor.slizaa.server.service.backend.extensions.Version;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * see:https://github.com/FasterXML/jackson-docs/wiki/JacksonPolymorphicDeserialization
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class MvnBasedExtension extends ExtensionIdentifier implements IExtension {

  /* - */
  @JsonProperty("dependencies")
  private List<MvnDependency> _dependencies;

    /**
     * Creates a new instance of type {@link MvnBasedExtension}.
     */
    public MvnBasedExtension(String symbolicName, Version version) {
        super(symbolicName, version);

        _dependencies = new ArrayList<>();
    }

    protected MvnBasedExtension() {
        super();

        _dependencies = new ArrayList<>();
    }

    /**
     * @param mvnDependency
     * @return
     */
    public MvnBasedExtension withDependency(MvnDependency mvnDependency) {
        _dependencies.add(checkNotNull(mvnDependency));
        return this;
    }

    /**
     * @return
     */
    @Override
    public List<URL> resolvedArtifactsToInstall() {

        //
        IMvnResolverServiceFactory resolverServiceFactory = MvnResolverServiceFactoryFactory
                .createNewResolverServiceFactory();

        //
        IMvnResolverService mvnResolverService = resolverServiceFactory.newMvnResolverService().create();
        IMvnResolverService.IMvnResolverJob resolverJob = mvnResolverService.newMvnResolverJob();
        _dependencies.forEach(dep -> {
            IMvnResolverService.IMvnResolverJobDependency dependency = resolverJob.withDependency(dep.getDependency());
            if (dep.getExclusionPatterns() != null) {
                dependency.withExclusionPatterns(dep.getExclusionPatterns().toArray(new String[0]));
            }
        });

        return Arrays.asList(resolverJob.resolveToUrlArray());

    }
}
