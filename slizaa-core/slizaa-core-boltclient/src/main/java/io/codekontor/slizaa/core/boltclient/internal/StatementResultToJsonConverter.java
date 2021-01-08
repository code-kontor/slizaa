/**
 * slizaa-core-boltclient - Slizaa Static Software Analysis Tools
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
/**
 *
 */
package io.codekontor.slizaa.core.boltclient.internal;

import io.codekontor.slizaa.core.boltclient.internal.gson.BoltAwareGsonFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.neo4j.driver.Result;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class StatementResultToJsonConverter {

  /** - */
  private static Gson _gson = BoltAwareGsonFactory.createGson();

  /**
   * <p>
   * </p>
   *
   * @param statementResult
   * @return
   */
  public static JsonArray convertToJsonArray(Result statementResult) {

    //
    JsonArray result = new JsonArray();

    //
    while (statementResult.hasNext()) {
      JsonElement element = _gson.toJsonTree(statementResult.next().asMap());
      result.add(element);
    }

    //
    return result;
  }

  /**
   * <p>
   * </p>
   *
   * @param statementResult
   * @return
   */
  public static String convertToJson(Result statementResult) {

    //
    JsonArray result = new JsonArray();

    //
    while (statementResult.hasNext()) {
      JsonElement element = _gson.toJsonTree(statementResult.next().asMap());
      result.add(element);
    }

    //
    return _gson.toJson(result);
  }

  public static String convertElementToJson(Object element) {
    return _gson.toJson(element);
  }

  /**
   * <p>
   * Creates a new instance of type {@link StatementResultToJsonConverter}.
   * </p>
   *
   */
  private StatementResultToJsonConverter() {
    //
  }
}
