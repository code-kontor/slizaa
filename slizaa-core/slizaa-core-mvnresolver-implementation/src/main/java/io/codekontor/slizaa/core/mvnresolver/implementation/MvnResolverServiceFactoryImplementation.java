/**
 * slizaa-core-mvnresolver-implementation - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.core.mvnresolver.implementation;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;

import org.jboss.shrinkwrap.resolver.api.Resolvers;
import org.jboss.shrinkwrap.resolver.api.maven.ConfigurableMavenResolverSystem;
import org.jboss.shrinkwrap.resolver.api.maven.repository.MavenRemoteRepositories;
import org.jboss.shrinkwrap.resolver.api.maven.repository.MavenRemoteRepository;
import org.jboss.shrinkwrap.resolver.api.maven.repository.MavenUpdatePolicy;
import io.codekontor.slizaa.core.mvnresolver.api.IMvnResolverService;
import io.codekontor.slizaa.core.mvnresolver.api.IMvnResolverServiceFactory;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class MvnResolverServiceFactoryImplementation implements IMvnResolverServiceFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public MvnResolverServiceFactoryBuilder newMvnResolverService() {

        ClassLoader current = Thread.currentThread().getContextClassLoader();

        try {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            return new MvnResolverServiceFactoryBuilderImplementation();
        } finally {
            Thread.currentThread().setContextClassLoader(current);
        }
    }

    /**
     * <p>
     * </p>
     *
     * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
     */
    public static class MvnResolverServiceFactoryBuilderImplementation implements MvnResolverServiceFactoryBuilder {

        /** - */
        private ConfigurableMavenResolverSystem _resolverSystem;

        /**
         * <p>
         * Creates a new instance of type
         * {@link MvnResolverServiceFactoryBuilderImplementation}.
         * </p>
         */
        public MvnResolverServiceFactoryBuilderImplementation() {
            _resolverSystem = Resolvers.use(ConfigurableMavenResolverSystem.class);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public MvnResolverServiceFactoryBuilder withRemoteRepository(String id, String url) {
            MavenRemoteRepository mavenRemoteRepository = MavenRemoteRepositories.createRemoteRepository(id, url, "default");
            mavenRemoteRepository.setUpdatePolicy(MavenUpdatePolicy.UPDATE_POLICY_NEVER);
            _resolverSystem.withRemoteRepo(mavenRemoteRepository);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public MvnResolverServiceFactoryBuilder withDefaultRemoteRepository() {
            return withRemoteRepository("central", "http://repo1.maven.org/maven2");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public IMvnResolverService create() {
            MvnResolverServiceImplementation serviceImplementation = new MvnResolverServiceImplementation();
            serviceImplementation.initialize(_resolverSystem);
            return serviceImplementation;
        }
    }
}
