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
import java.util.Map;
import java.util.function.Function;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public interface ICypherStatementExecutor {

  /**
   * <p>
   * </p>
   *
   * @param cypherStatement
   */
  IResult executeCypherStatement(String cypherStatement);

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
   */
  public static interface IResult {

    /**
     * <p>
     * </p>
     *
     * @return
     */
    List<String> keys();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    Map<String, Object> single();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    List<Map<String, Object> > list();

    /**
     * <p>
     * </p>
     *
     * @param mapFunction
     * @return
     */
    <T> List<T> list(Function<Map<String, Object> , T> mapFunction);
  }
}
