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
package io.codekontor.slizaa.core.boltclient;


import io.codekontor.slizaa.core.boltclient.internal.StatementResultToJsonConverter;
import org.neo4j.driver.Result;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public interface IBoltClient {

    static Function<Result, String> jsonMappingFunction() {
        return result -> StatementResultToJsonConverter.convertToJson(result);
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    String getName();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    String getDescription();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    String getUri();

    /**
     * <p>
     * </p>
     */
    void connect();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    boolean isConnected();

    /**
     * <p>
     * </p>
     */
    void disconnect();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    List<String> getNodeLabels();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    List<String> getPropertyKeys();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    List<String> getRelationshipTypes();

    /**
     *
     * @param nodeId
     * @return
     */
    String getNode(long nodeId);

    /**
     *
     * @param nodeId
     * @param mappingFunction
     * @param <T>
     * @return
     */
    <T> T getNode(long nodeId, Function<Node, T> mappingFunction);

    void getNodeAndConsume(long nodeId, Consumer<Node> consumer);

    String getRelationship(long relationshipId);

    <T> T getRelationship(long relationshipId, Function<Relationship, T> consumer);

    void getRelationshipAndConsume(long relationshipId, Consumer<Relationship> consumer);


    String syncExecCypherQuery(String cypherQuery);

    /**
     *
     * @param cypherQuery
     * @param mappingFunction
     * @param <T>
     * @return
     */
    <T> T syncExecCypherQuery(String cypherQuery, Function<Result, T> mappingFunction);

    String syncExecCypherQuery(String cypherQuery, Map<String, Object> params);

    /**
     *
     * @param cypherQuery
     * @param params
     * @param mappingFunction
     * @param <T>
     * @return
     */
    <T> T syncExecCypherQuery(String cypherQuery, Map<String, Object> params, Function<Result, T> mappingFunction);


    /**
     * <p>
     * </p>
     *
     * @param cypherQuery
     * @param consumer
     * @return
     */
    void syncExecAndConsume(String cypherQuery, Consumer<Result> consumer);

    /**
     * <p>
     * </p>
     *
     * @param cypherQuery
     * @param params
     * @param consumer
     * @return
     */
    void syncExecAndConsume(String cypherQuery, Map<String, Object> params, Consumer<Result> consumer);

    /**
     * <p>
     * </p>
     *
     * @param cypherQuery
     * @return
     */
    Future<String> asyncExecCypherQuery(String cypherQuery);



    /**
     * <p>
     * </p>
     *
     * @param cypherQuery
     * @return
     */
    <T> Future<T> asyncExecCypherQuery(String cypherQuery, Function<Result, T> mappingFunction);

    /**
     * <p>
     * </p>
     *
     * @param cypherQuery
     * @param params
     * @return
     */
    Future<String> asyncExecCypherQuery(String cypherQuery, Map<String, Object> params);

    /**
     * <p>
     * </p>
     *
     * @param cypherQuery
     * @param params
     * @return
     */
    <T> Future<T> asyncExecCypherQuery(String cypherQuery, Map<String, Object> params, Function<Result, T> mappingFunction);

    /**
     * <p>
     * </p>
     *
     * @param cypherQuery
     * @param consumer
     * @return
     */
    Future<Void> asyncExecAndConsume(String cypherQuery, Consumer<Result> consumer);

    /**
     * <p>
     * </p>
     *
     * @param cypherQuery
     * @param params
     * @param consumer
     * @return
     */
    Future<Void> asyncExecAndConsume(String cypherQuery, Map<String, Object> params, Consumer<Result> consumer);
}
