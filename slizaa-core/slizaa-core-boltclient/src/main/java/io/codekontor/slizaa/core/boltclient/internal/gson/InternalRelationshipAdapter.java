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

import org.neo4j.driver.types.Relationship;

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
public class InternalRelationshipAdapter implements JsonSerializer<Relationship> {

  @Override
  public JsonElement serialize(Relationship relationship, Type typeOfSrc, JsonSerializationContext context) {

    // create result
    JsonObject result = new JsonObject();

    // add the id
    result.addProperty("id", relationship.id());

    // add the start id
    result.addProperty("start", relationship.startNodeId());

    // add the stop id
    result.addProperty("end", relationship.endNodeId());

    // add the type
    result.addProperty("type", relationship.type());

    // add the properties
    result.add("properties", context.serialize(relationship.asMap()));

    //
    result.addProperty("__type", "RELATIONSHIP");

    //
    return result;
  }
}
