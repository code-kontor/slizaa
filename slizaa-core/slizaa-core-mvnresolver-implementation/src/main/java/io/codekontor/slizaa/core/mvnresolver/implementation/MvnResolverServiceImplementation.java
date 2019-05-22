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
import java.util.ArrayList;
import java.util.List;

import org.jboss.shrinkwrap.resolver.api.maven.ConfigurableMavenResolverSystem;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenCoordinates;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenDependencies;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenDependency;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenDependencyExclusion;
import io.codekontor.slizaa.core.mvnresolver.api.IMvnResolverService;
import io.codekontor.slizaa.core.mvnresolver.api.IMvnCoordinate;

/**
 * <p>
 * https://github.com/shrinkwrap/resolver
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class MvnResolverServiceImplementation implements IMvnResolverService {

	ConfigurableMavenResolverSystem _resolverSystem;

	/**
	 * <p>
	 * </p>
	 */
	public void initialize(ConfigurableMavenResolverSystem resolverSystem) {

		_resolverSystem = resolverSystem;
	}

	@Override
	public IMvnResolverJob newMvnResolverJob() {
		return new MvnResolverJobImplementation(this);
	}

	/**
	 *
	 * @param coordinate
	 * @return
	 */
	@Override
	public IMvnCoordinate parseCoordinate(String coordinate) {
		return new MvnCoordinateImplementation(MavenCoordinates.createCoordinate(coordinate));
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public File[] resolve(String... coords) {
		return _resolverSystem.resolve(checkNotNull(coords)).withTransitivity().asFile();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File resolveArtifact(String canonicalForm) {
		return _resolverSystem.resolve(checkNotNull(canonicalForm)).withoutTransitivity().asSingleFile();
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @param job
	 * @return
	 */
	File[] resolve(MvnResolverJobImplementation job) {

		//
		List<MavenDependency> dependencies = new ArrayList<>();
		List<MavenDependencyExclusion> exclusions = new ArrayList<>();
		
		for (String exclusionPattern : job.getExclusionPatterns()) {
			exclusions.add(MavenDependencies.createExclusion(exclusionPattern));
		}
		
		for (String coord : job.getCoords()) {
			MavenDependency mavenDependency = MavenDependencies.createDependency(coord, ScopeType.COMPILE, false,exclusions.toArray(new MavenDependencyExclusion[0]));
			dependencies.add(mavenDependency);
		}
		
		return _resolverSystem
		  .addDependencies(dependencies).resolve().withTransitivity().asFile();

	}
}
