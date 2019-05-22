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

import java.io.File;

import io.codekontor.slizaa.scanner.spi.contentdefinition.AbstractContentDefinitionProvider;
import io.codekontor.slizaa.scanner.spi.contentdefinition.AnalyzeMode;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinition;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProviderFactory;

/**
 * <p>
 * Superclass for all implementations of {@link ITempDefinitionProvider}
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
@Deprecated
public class FileBasedContentDefinitionProvider extends AbstractContentDefinitionProvider<FileBasedContentDefinitionProvider> {
  
	@Override
  public IContentDefinitionProviderFactory<FileBasedContentDefinitionProvider> getContentDefinitionProviderFactory() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String toExternalRepresentation() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
	public IContentDefinition createFileBasedContentDefinition(String contentName, String contentVersion,
			File[] binaryPaths, File[] sourcePaths, AnalyzeMode analyzeMode) {

		//
		return super.createFileBasedContentDefinition(contentName, contentVersion, binaryPaths, sourcePaths,
				analyzeMode);
	}

	@Override
	protected void onInitializeProjectContent() {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onDisposeProjectContent() {
		//
	}
}
