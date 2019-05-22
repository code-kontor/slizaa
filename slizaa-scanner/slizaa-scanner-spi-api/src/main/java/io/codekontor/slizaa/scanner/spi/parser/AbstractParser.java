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

import static io.codekontor.slizaa.scanner.spi.internal.Preconditions.checkNotNull;

import java.util.LinkedList;
import java.util.List;

import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinition;
import io.codekontor.slizaa.scanner.spi.contentdefinition.filebased.IFile;
import io.codekontor.slizaa.scanner.spi.parser.model.INode;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public abstract class AbstractParser<P extends IParserFactory> implements IParser {

  /** - */
  private P              _parserFactory;

  /** - */
  private List<IProblem> _problems;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractParser}.
   * </p>
   * 
   * @param parserFactory
   */
  public AbstractParser(P parserFactory) {

    //
    checkNotNull(parserFactory);

    //
    _parserFactory = parserFactory;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public P getParserFactory() {
    return _parserFactory;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  protected List<IProblem> getProblems() {
    return _problems;
  }

  /**
   *
   * @param content
   * @param resource
   *          the resource to parse
   * @param resourceBean
   *          the resource bean that represents the resource that has to be parsed
   * @param context
   * @return
   */
  @Override
  public final List<IProblem> parseResource(IContentDefinition content, IFile resource,
      INode resourceBean, IParserContext context) {

    // Reset problem list
    _problems = new LinkedList<IProblem>();

    // do the parsing
    doParseResource(content, resource, resourceBean, context);

    //
    return _problems;
  }

  /**
   * Override in subclasses to implement parse logic
   * 
   * @param content
   * @param resource
   * @param context
   */
  protected abstract void doParseResource(IContentDefinition content, IFile resource, INode resourceBean,
      IParserContext context);

  /**
   * <p>
   * </p>
   * 
   * @param resource
   * @return
   */
  public abstract boolean canParse(IFile resource);

}
