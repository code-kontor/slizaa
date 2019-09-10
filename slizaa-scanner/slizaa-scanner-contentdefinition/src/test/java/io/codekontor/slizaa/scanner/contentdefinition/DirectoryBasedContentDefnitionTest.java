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

import io.codekontor.mvnresolver.MvnResolverServiceFactoryFactory;
import io.codekontor.mvnresolver.api.IMvnResolverService;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinition;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProviderFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class DirectoryBasedContentDefnitionTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private IContentDefinitionProviderFactory<DirectoryBasedContentDefinitionProvider> _providerFactory  = new DirectoryBasedContentDefinitionProviderFactory();

    @Test
    public void testDirectoryBasedContentDefinition() throws IOException {

        //
        File testFolder =
                setupTestFolder("org.springframework:spring-core:5.1.9.RELEASE", "org.springframework:spring-beans:5.1.9.RELEASE");

        //
        DirectoryBasedContentDefinitionProvider contentDefinitionProvider = _providerFactory.emptyContentDefinitionProvider();
        contentDefinitionProvider.add(testFolder);
        List<IContentDefinition> contentDefinitions = contentDefinitionProvider.getContentDefinitions();

        // assert the result
        assertThat(contentDefinitions).hasSize(2);
        assertThat(contentDefinitions.stream().map(cd -> cd.getName()).collect(Collectors.toList()))
                .containsExactlyInAnyOrder("spring-core", "spring-beans");
        assertThat(contentDefinitions.stream().map(cd -> cd.getVersion()).collect(Collectors.toList()))
                .containsExactlyInAnyOrder("5.1.9.RELEASE", "5.1.9.RELEASE");
    }

    /**
     *
     * @return
     * @throws IOException
     */
    private File setupTestFolder(String... mavenCoordinates) throws IOException {

        // create the IMvnResolverService
        IMvnResolverService mvnResolverService = MvnResolverServiceFactoryFactory.createNewResolverServiceFactory().
                newMvnResolverService().withMavenCentralRepo(true).create();

        // create the test folder
        File tempFolder = testFolder.newFolder("folder");

        // populate it
        File[] files = mvnResolverService.resolve(false, mavenCoordinates);
        for (File sourceFile : files) {
            File targetFile = new File(tempFolder, sourceFile.getName());
            copyFile(sourceFile, targetFile);
        }

        // return the result
        return tempFolder;
    }

    /**
     *
     * @param source
     * @param dest
     * @throws IOException
     */
    private static void copyFile(File source, File dest) throws IOException {

        FileChannel sourceChannel = null;
        FileChannel destChannel = null;

        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } finally{
            sourceChannel.close();
            destChannel.close();
        }
    }
}
