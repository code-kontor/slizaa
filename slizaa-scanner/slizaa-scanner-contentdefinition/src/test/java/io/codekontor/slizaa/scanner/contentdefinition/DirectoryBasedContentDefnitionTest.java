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

import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinition;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProviderFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DirectoryBasedContentDefnitionTest {

    private IContentDefinitionProviderFactory<DirectoryBasedContentDefinitionProvider> _providerFactory;

    @Before
    public void setup() {
        _providerFactory = new DirectoryBasedContentDefinitionProviderFactory();
    }

    @Test
    public void testDirectoryBasedContentDefnition_1() {

        // TODO TEST-SETUP

        DirectoryBasedContentDefinitionProvider contentDefinitionProvider = _providerFactory.emptyContentDefinitionProvider();
        contentDefinitionProvider.add(new File("c:\\tmp\\sl"));

        List<IContentDefinition> contentDefinitions = contentDefinitionProvider.getContentDefinitions();

        assertThat(contentDefinitions).hasSize(2);
        assertThat(contentDefinitions.get(0).getName()).isEqualTo("spring-core");
        assertThat(contentDefinitions.get(1).getName()).isEqualTo("spring-beans");
    }

/*    @Test
    public void testMvnBasedContentDefnition_2() {

        String externalRepresentation = "org.springframework:spring-core:5.1.6.RELEASE, org.springframework:spring-beans:5.1.6.RELEASE";

        MvnBasedContentDefinitionProvider contentDefinitionProvider = _providerFactory.fromExternalRepresentation(externalRepresentation);

        List<IContentDefinition> contentDefinitions = contentDefinitionProvider.getContentDefinitions();

        assertThat(contentDefinitions).hasSize(2);
        assertThat(contentDefinitions.get(0).getName()).isEqualTo("spring-core");
        assertThat(contentDefinitions.get(1).getName()).isEqualTo("spring-beans");
    }

    @Test
    public void testMvnBasedContentDefnition_3() {

        MvnBasedContentDefinitionProvider contentDefinitionProvider = _providerFactory.emptyContentDefinitionProvider();
        contentDefinitionProvider.addArtifact("org.springframework:spring-core:5.1.6.RELEASE");
        contentDefinitionProvider.addArtifact("org.springframework:spring-beans:5.1.6.RELEASE");

        String externalRepresentation = _providerFactory.toExternalRepresentation(contentDefinitionProvider);

        MvnBasedContentDefinitionProvider contentDefinitionProvider2 = _providerFactory.fromExternalRepresentation(externalRepresentation);
        List<IContentDefinition> contentDefinitions = contentDefinitionProvider2.getContentDefinitions();

        assertThat(contentDefinitions).hasSize(2);
        assertThat(contentDefinitions.get(0).getName()).isEqualTo("spring-core");
        assertThat(contentDefinitions.get(1).getName()).isEqualTo("spring-beans");
    }*/
}
