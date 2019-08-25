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
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.codekontor.slizaa.scanner.spi.contentdefinition.AbstractContentDefinitionProvider;
import io.codekontor.slizaa.scanner.spi.contentdefinition.AnalyzeMode;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProviderFactory;

public class DirectoryBasedContentDefinitionProvider extends AbstractContentDefinitionProvider<DirectoryBasedContentDefinitionProvider> {

    /** - */
    private List<File> _directoriesWithBinaryArtifacts;

    /** - */
    private final IContentDefinitionProviderFactory<DirectoryBasedContentDefinitionProvider> _contentDefinitionProviderFactory;

    public boolean add(File e) {
        return _directoriesWithBinaryArtifacts.add(e);
    }

    public boolean addAll(Collection<? extends File> c) {
        return _directoriesWithBinaryArtifacts.addAll(c);
    }


    @Override
    public IContentDefinitionProviderFactory<DirectoryBasedContentDefinitionProvider> getContentDefinitionProviderFactory() {
        return _contentDefinitionProviderFactory;
    }

    @Override
    public String toExternalRepresentation() {
        return this._contentDefinitionProviderFactory.toExternalRepresentation(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitializeProjectContent() {

        // collect dirs
        for (File directories : _directoriesWithBinaryArtifacts) {

            //
            for (File artifact : collectJars(directories)) {

                NameAndVersionInfo info = NameAndVersionInfo.resolveNameAndVersion(artifact);

                if (!info.isSource()) {

                    this.createFileBasedContentDefinition(info.getName(), info.getVersion(), new File[]{artifact}, null,
                            AnalyzeMode.BINARIES_ONLY);
                }
            }
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
     *
     * @return
     */
    List<File> getDirectoriesWithBinaryArtifacts() {
        return _directoriesWithBinaryArtifacts;
    }

    /**
     * <p>
     * </p>
     *
     * @param directory
     * @return
     */
    private List<File> collectJars(File directory) {

        // path
        Path path = directory.toPath();

        // create result
        final List<File> result = new ArrayList<>();

        //
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (!attrs.isDirectory() &&
                            (file.getFileName().toString().endsWith(".zip") || file.getFileName().toString().endsWith(".jar"))) {
                        result.add(file.toFile());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        //
        return result;
    }

    /**
     * <p>
     * Creates a new instance of type {@link DirectoryBasedContentDefinitionProvider}.
     * </p>
     */
    DirectoryBasedContentDefinitionProvider(IContentDefinitionProviderFactory<DirectoryBasedContentDefinitionProvider> contentDefinitionProviderFactory) {
        _directoriesWithBinaryArtifacts = new ArrayList<>();
        _contentDefinitionProviderFactory = checkNotNull(contentDefinitionProviderFactory);
    }

}
