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

import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProviderFactory;
import io.codekontor.slizaa.scanner.spi.contentdefinition.InvalidContentDefinitionException;

import java.io.File;
import java.util.stream.Collectors;

public class DirectoryBasedContentDefinitionProviderFactory implements IContentDefinitionProviderFactory<DirectoryBasedContentDefinitionProvider> {

  private final static String PATH_SEPARATOR = File.pathSeparator; 
  
  @Override
  public String getFactoryId() {
    return DirectoryBasedContentDefinitionProviderFactory.class.getName();
  }

  @Override
  public String getShortForm() {
    return "directory";
  }

  @Override
  public String getName() {
    return "Directory based";
  }

  @Override
  public String getDescription() {
    return "";
  }

  @Override
  public DirectoryBasedContentDefinitionProvider emptyContentDefinitionProvider() {
    return new DirectoryBasedContentDefinitionProvider(this);
  }

  @Override
  public String toExternalRepresentation(DirectoryBasedContentDefinitionProvider contentDefinitionProvider) {
    return contentDefinitionProvider.getDirectoriesWithBinaryArtifacts().stream().map(file -> file.getAbsolutePath()).collect(Collectors.joining(PATH_SEPARATOR));
  }

  @Override
  public DirectoryBasedContentDefinitionProvider fromExternalRepresentation(String externalRepresentation) {
    
    //
    if (externalRepresentation == null) {
      throw new InvalidContentDefinitionException("Invalid content definition 'null'.");
    }
    
    //
    String[] direcoryPaths = externalRepresentation.split(PATH_SEPARATOR);
    
    //
    DirectoryBasedContentDefinitionProvider contentDefinitionProvider = new DirectoryBasedContentDefinitionProvider(this);
    for (String directoryPath : direcoryPaths) {
      directoryPath = normalizePath(directoryPath);
      File directory = new File(directoryPath);
      contentDefinitionProvider.add(directory);
    }
    
    //
    return contentDefinitionProvider;
  }


  private static String normalizePath(String path) {

    if (path == null) {
      return null;
    }
    path = path.replace("\\", File.separator);
    path = path.replace("/", File.separator);

    return path;
  }
}
