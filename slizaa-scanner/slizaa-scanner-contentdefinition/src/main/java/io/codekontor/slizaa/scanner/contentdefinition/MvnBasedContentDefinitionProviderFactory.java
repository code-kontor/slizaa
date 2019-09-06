/**
 * slizaa-scanner-contentdefinition - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.scanner.contentdefinition;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Scanner;
import java.util.stream.Collectors;

import io.codekontor.mvnresolver.MvnResolverServiceFactoryFactory;
import io.codekontor.mvnresolver.api.IMvnResolverService;
import io.codekontor.mvnresolver.api.IMvnResolverServiceFactory;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IConfigurableContentDefinitionProviderFactory;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProviderFactory;

public class MvnBasedContentDefinitionProviderFactory
    implements IConfigurableContentDefinitionProviderFactory<MvnBasedContentDefinitionProvider> {

  private static final String DELIMITER = ",";

  private IMvnResolverService _mvnResolverService;

  @Override
  public String getFactoryId() {
    return MvnBasedContentDefinitionProviderFactory.class.getName();
  }

  @Override
  public String getShortForm() {
    return "mvn";
  }

  @Override
  public String getName() {
    return "Maven Based";
  }

  @Override
  public String getDescription() {
    return "";
  }

  @Override
  public MvnBasedContentDefinitionProvider emptyContentDefinitionProvider() {

    IMvnResolverService mvnResolverService = _mvnResolverService != null ?
            _mvnResolverService :
     MvnResolverServiceFactoryFactory.createNewResolverServiceFactory().newMvnResolverService()
            .withMavenCentralRepo(true).create();

    return new MvnBasedContentDefinitionProvider(this, mvnResolverService);
  }

  @Override
  public void configure(IConfigurationContext context) {
    _mvnResolverService = context.getService(IMvnResolverService.class);
  }

  @Override
  public String toExternalRepresentation(MvnBasedContentDefinitionProvider contentDefinitionProvider) {
    return contentDefinitionProvider.getMavenCoordinates().stream()
        .map(mvnCoordinate -> mvnCoordinate.toCanonicalForm()).collect(Collectors.joining(DELIMITER));
  }

  @Override
  public MvnBasedContentDefinitionProvider fromExternalRepresentation(String externalRepresentation) {

    checkNotNull(externalRepresentation);

    MvnBasedContentDefinitionProvider contentDefinitionProvider = emptyContentDefinitionProvider();

    String[] parts = externalRepresentation.split(DELIMITER);

    for (String part : parts) {
      part = part.trim();
      if (!part.isEmpty()) {
        contentDefinitionProvider.addArtifact(part);
      }
    }

    return contentDefinitionProvider;
  }
}
