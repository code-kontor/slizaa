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
package io.codekontor.slizaa.core.boltclient.internal.gson;

import java.lang.reflect.Type;

import org.neo4j.driver.v1.types.Path;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 *
 */
public class InternalPathAdapter implements JsonSerializer<Path> {

  @Override
  public JsonElement serialize(Path path, Type typeOfSrc, JsonSerializationContext context) {

    // create result
    JsonObject result = new JsonObject();

    // add the nodes
    result.add("nodes", context.serialize(path.nodes()));

    // add the relationships
    result.add("relationships", context.serialize(path.relationships()));

    // add the segments
    JsonArray jsonArray = new JsonArray();
    path.forEach(seg -> jsonArray.add(context.serialize(seg)));
    result.add("segments", jsonArray);

    //
    result.addProperty("__type", "PATH");

    //
    return result;
  }
}
