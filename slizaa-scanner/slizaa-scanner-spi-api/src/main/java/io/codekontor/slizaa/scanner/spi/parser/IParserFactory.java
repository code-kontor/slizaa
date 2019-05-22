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
package io.codekontor.slizaa.scanner.spi.parser;

import io.codekontor.slizaa.core.progressmonitor.IProgressMonitor;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinition;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;

/**
 * <p>
 * A parser factory is responsible for creating project parser.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public interface IParserFactory {

  /**
   * <p>
   * This method is called immediately after a {@link IParserFactory} has been created.
   * </p>
   */
  void initialize();

  /**
   * <p>
   * This method is called before a {@link IParserFactory} will be destroyed.
   * </p>
   */
  void dispose();

  /**
   * <p>
   * Creates a new instance of type {@link IParser}.
   * </p>
   * 
   * @return the newly created {@link IParser}
   */
  IParser createParser(IContentDefinitionProvider systemDefinition);

  /**
   * <p>
   * </p>
   * 
   * @param systemDefinition
   * @param cypherStatementExecutor
   * @param subMonitor
   * @throws Exception
   */
  void batchParseStart(IContentDefinitionProvider systemDefinition, ICypherStatementExecutor cypherStatementExecutor, IProgressMonitor subMonitor)
      throws Exception;

  /**
   * <p>
   * </p>
   * 
   * @param systemDefinition
   * @param cypherStatementExecutor
   * @param subMonitor
   * @throws Exception
   */
  void batchParseStop(IContentDefinitionProvider systemDefinition, ICypherStatementExecutor cypherStatementExecutor, IProgressMonitor subMonitor)
      throws Exception;

  /**
   * <p>
   * </p>
   * 
   * @param contentDefinition
   * @throws Exception
   */
  void batchParseStartContentDefinition(IContentDefinition contentDefinition) throws Exception;

  /**
   * <p>
   * </p>
   * 
   * @param contentDefinition
   * @throws Exception
   */
  void batchParseStopContentDefinition(IContentDefinition contentDefinition) throws Exception;

  /**
   * <p>
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
   */
  static abstract class Adapter implements IParserFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
      // empty default implementation
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
      // empty default implementation
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void batchParseStart(IContentDefinitionProvider systemDefinition, ICypherStatementExecutor cypherStatementExecutor,
        IProgressMonitor subMonitor) throws Exception {
      // empty default implementation
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void batchParseStop(IContentDefinitionProvider systemDefinition, ICypherStatementExecutor cypherStatementExecutor,
        IProgressMonitor subMonitor) throws Exception {
      // empty default implementation
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void batchParseStartContentDefinition(IContentDefinition contentDefinition) throws Exception {
      // empty default implementation
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void batchParseStopContentDefinition(IContentDefinition contentDefinition) throws Exception {
      // empty default implementation
    }
  }
}
