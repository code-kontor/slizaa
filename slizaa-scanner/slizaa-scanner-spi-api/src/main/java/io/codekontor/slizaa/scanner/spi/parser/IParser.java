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

import java.util.List;

import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinition;
import io.codekontor.slizaa.scanner.spi.contentdefinition.filebased.IFile;
import io.codekontor.slizaa.scanner.spi.parser.model.INode;

/**
 * <p>
 * Defines the common interface to parse a {@link IParsableResource}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public interface IParser {

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public ParserType getParserType();

  /**
   * <p>
   * </p>
   * 
   * @param resource
   * @return
   */
  boolean canParse(IFile resource);

  /**
   * <p>
   * </p>
   * 
   * @param contentDefinition
   *          the content definition that specifies this resource.
   * @param resource
   *          the resource to parse
   * @param resourceBean
   *          the resource bean that represents the resource that has to be parsed
   * @param parserContext
   *          the parser context
   * @return
   */
  List<IProblem> parseResource(IContentDefinition contentDefinition, IFile resource, INode resourceBean,
      IParserContext parserContext);
}
