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

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import io.codekontor.mvnresolver.MvnResolverServiceFactoryFactory;
import io.codekontor.mvnresolver.api.IMvnCoordinate;
import io.codekontor.mvnresolver.api.IMvnResolverService;
import io.codekontor.mvnresolver.api.IMvnResolverServiceFactory;
import io.codekontor.slizaa.scanner.spi.contentdefinition.AbstractContentDefinitionProvider;
import io.codekontor.slizaa.scanner.spi.contentdefinition.AnalyzeMode;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProviderFactory;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class MvnBasedContentDefinitionProvider
        extends AbstractContentDefinitionProvider<MvnBasedContentDefinitionProvider> {

    /** - */
    private final IContentDefinitionProviderFactory<MvnBasedContentDefinitionProvider> _contentDefinitionProviderFactory;

    /** - */
    private final IMvnResolverService _mvnResolverService;

    /** - */
    private List<IMvnCoordinate> _mavenCoordinates;

    /**
     *
     */
    @Override
    public IContentDefinitionProviderFactory<MvnBasedContentDefinitionProvider> getContentDefinitionProviderFactory() {
        return _contentDefinitionProviderFactory;
    }

    @Override
    public String toExternalRepresentation() {
        return _contentDefinitionProviderFactory.toExternalRepresentation(this);
    }

    /**
     * <p>
     * </p>
     *
     * @param groupId
     * @param artifactId
     * @param version
     */
    @Deprecated
    public void addArtifact(String groupId, String artifactId, String version) {
        addArtifact(String.format("%s:%s:%s", checkNotNull(groupId), checkNotNull(artifactId), checkNotNull(version)));
    }

    public void setMavenCoordinates(List<String> mavenCoordinates) {
        _mavenCoordinates = checkNotNull(mavenCoordinates.stream()
                .map(coordinate -> _mvnResolverService.parseCoordinate(coordinate)).collect(Collectors.toList()));
    }

    public IMvnCoordinate addArtifact(String coordinate) {
        IMvnCoordinate mvnCoordinate = _mvnResolverService.parseCoordinate(coordinate);
        _mavenCoordinates.add(mvnCoordinate);
        return mvnCoordinate;
    }

    public List<IMvnCoordinate> getMavenCoordinates() {
        return _mavenCoordinates;
    }

    /**
     */
    protected void onInitializeProjectContent() {

        //
        for (IMvnCoordinate mvnCoordinate : _mavenCoordinates) {

            // TODO
            File resolvedFile = _mvnResolverService.resolveArtifact(String.format("%s:%s:%s", mvnCoordinate.getGroupId(),
                    mvnCoordinate.getArtifactId(), mvnCoordinate.getVersion()));

            this.createFileBasedContentDefinition(mvnCoordinate.getArtifactId(), mvnCoordinate.getVersion(),
                    new File[]{resolvedFile}, null, AnalyzeMode.BINARIES_ONLY);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDisposeProjectContent() {
        //
    }

    /**
     * <p>
     * Creates a new instance of type {@link MvnBasedContentDefinitionProvider}.
     * </p>
     */
    MvnBasedContentDefinitionProvider(IContentDefinitionProviderFactory<MvnBasedContentDefinitionProvider> contentDefinitionProviderFactory,
                                      IMvnResolverService mvnResolverService) {

        _contentDefinitionProviderFactory = checkNotNull(contentDefinitionProviderFactory);
        _mvnResolverService = checkNotNull(mvnResolverService);
        _mavenCoordinates = new LinkedList<>();
    }
}
