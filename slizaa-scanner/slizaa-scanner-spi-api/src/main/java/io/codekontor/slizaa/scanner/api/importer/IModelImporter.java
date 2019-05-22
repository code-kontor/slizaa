/**
 * slizaa-scanner-spi-api - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.scanner.api.importer;

import io.codekontor.slizaa.core.progressmonitor.IProgressMonitor;
import io.codekontor.slizaa.scanner.api.graphdb.IGraphDb;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import io.codekontor.slizaa.scanner.spi.parser.IProblem;

import java.util.List;
import java.util.function.Supplier;

/**
 * <p>
 * {@link IModelImporter IModelImporters} can be used to parse systems (defined by {@link IContentDefinitionProvider
 * IContentDefinitionProviders}).
 * </p>
 * <p>
 * To create {@link IModelImporter} instances you have to use the {@link IModelImporterFactory}: <code><pre>
 * // get the factory
 * IModelImporterFactory modelImporterFactory = ...;
 *
 * // create a new IModelImporter instance
 * modelImporterFactory.createModelImporter(systemDefinition, databaseDirectory, parserFactories);
 * </pre></code>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public interface IModelImporter {

  /**
   * <p>
   * Parses the underlying {@link IContentDefinitionProvider}.
   * </p>
   *
   * @param monitor
   *          the progress monitor
   * @return the list with all problems that may occurred while parsing the system
   */
  List<IProblem> parse(IProgressMonitor monitor);

  /**
   * <p>
   * </p>
   *
   * @param monitor
   * @param graphDbSupplier
   * @return
   */
  List<IProblem> parse(IProgressMonitor monitor, Supplier<IGraphDb> graphDbSupplier);

  /**
   * <p>
   * </p>
   *
   * @return
   */
  IGraphDb getGraphDb();
}
