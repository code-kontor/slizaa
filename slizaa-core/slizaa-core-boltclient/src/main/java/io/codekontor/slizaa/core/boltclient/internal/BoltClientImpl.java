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
package io.codekontor.slizaa.core.boltclient.internal;

import io.codekontor.slizaa.core.boltclient.IBoltClient;
import io.codekontor.slizaa.core.boltclient.internal.asynch.StatementCallable;
import org.neo4j.driver.*;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class BoltClientImpl implements IBoltClient {

    /**
     * -
     */
    private final PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * -
     */
    private final ExecutorService _executorService;

    /**
     * -
     */
    private String _name;

    /**
     * -
     */
    private String _description;

    /**
     * -
     */
    private String _uri;

    /**
     * -
     */
    private Driver _driver;

    /**
     * -
     */
    private boolean _connected;

    /**
     * <p>
     * Creates a new instance of type {@link BoltClientImpl}.
     * </p>
     *
     * @param uri
     * @param name
     * @param description
     */
    public BoltClientImpl(ExecutorService executorService, String uri, String name, String description) {
        this._executorService = checkNotNull(executorService);
        this._uri = checkNotNull(uri);
        this._name = name;
        this._description = description;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this._propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this._propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public String getName() {
        return this._name;
    }

    @Override
    public String getDescription() {
        return this._description;
    }

    @Override
    public boolean isConnected() {
        return this._connected;
    }

    @Override
    public String getUri() {
        return this._uri;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect() {
        Config config = Config.defaultConfig().builder().withoutEncryption().build();
        this._driver = GraphDatabase.driver(getUri(), config);
        setConnected(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() {
        this._driver.close();
        setConnected(false);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getRelationshipTypes() {
        return syncExecCypherQuery("CALL db.relationshipTypes", result -> result.stream().map(record -> record.get("relationshipType").asString()).collect(Collectors.toList()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getNodeLabels() {
        return syncExecCypherQuery("CALL db.labels", result -> result.stream().map(record -> record.get("label").asString()).collect(Collectors.toList()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getPropertyKeys() {
        return syncExecCypherQuery("CALL db.propertyKeys", result -> result.stream().map(record -> record.get("propertyKey").asString()).collect(Collectors.toList()));
    }

    @Override
    public String getNode(long nodeId) {
        return getNode(nodeId, node -> StatementResultToJsonConverter.convertElementToJson(node));
    }

    @Override
    public <T> T getNode(long nodeId, Function<Node, T> mappingFunction) {
        return syncExecCypherQuery(String.format("MATCH (n) WHERE id(n) = %s RETURN n ", nodeId), r -> r.hasNext() ? mappingFunction.apply(r.single().get("n").asNode()) : null);
    }

    @Override
    public void getNodeAndConsume(long nodeId, Consumer<Node> consumer) {
        syncExecAndConsume(String.format("MATCH (n) WHERE id(n) = %s RETURN n ", nodeId), result -> {
            if (result.hasNext()) {
                consumer.accept(result.single().get("n").asNode());
            }
        });
    }

    @Override
    public String getRelationship(long relationshipId) {
        return getRelationship(relationshipId, node -> StatementResultToJsonConverter.convertElementToJson(node));
    }

    @Override
    public <T> T getRelationship(long relationshipId, Function<Relationship, T> mappingFunction) {
        return syncExecCypherQuery(String.format("MATCH ()-[r]->() WHERE id(r) = %s RETURN r ", relationshipId), r -> mappingFunction.apply(r.single().get("r").asRelationship()));
    }

    @Override
    public void getRelationshipAndConsume(long relationshipId, Consumer<Relationship> consumer) {
        syncExecAndConsume(String.format("MATCH ()-[r]->() WHERE id(r) = %s RETURN r ", relationshipId), result -> {
            if (result.hasNext()) {
                consumer.accept(result.single().get("r").asRelationship());
            }
        });
    }

    @Override
    public String syncExecCypherQuery(String cypherQuery) {
        return syncExecCypherQuery(cypherQuery, null, IBoltClient.jsonMappingFunction());
    }

    @Override
    public <T> T syncExecCypherQuery(String cypherQuery, Function<Result, T> mappingFunction) {
        return syncExecCypherQuery(cypherQuery, null, mappingFunction);
    }

    @Override
    public String syncExecCypherQuery(String cypherQuery, Map<String, Object> params) {
        return syncExecCypherQuery(cypherQuery, params, IBoltClient.jsonMappingFunction());
    }

    @Override
    public <T> T syncExecCypherQuery(String cypherQuery, Map<String, Object> params, Function<Result, T> mappingFunction) {
        checkNotNull(cypherQuery);
        checkNotNull(mappingFunction);
        assertConnected();

        try (Session session = this._driver.session()) {
            Result result = session.run(cypherQuery, params);
            return mappingFunction.apply(result);
        }
    }

    @Override
    public void syncExecAndConsume(String cypherQuery, Consumer<Result> consumer) {
        syncExecAndConsume(cypherQuery, null, consumer);
    }

    @Override
    public void syncExecAndConsume(String cypherQuery, Map<String, Object> params, Consumer<Result> consumer) {
        checkNotNull(cypherQuery);
        assertConnected();

        try (Session session = this._driver.session()) {
            Result result = session.run(cypherQuery, params);
            if (consumer != null) {
                consumer.accept(result);
            }
        }
    }

    @Override
    public Future<String> asyncExecCypherQuery(String cypherQuery) {
        return asyncExecCypherQuery(cypherQuery, null, IBoltClient.jsonMappingFunction());
    }

    @Override
    public Future<String> asyncExecCypherQuery(String cypherQuery, Map<String, Object> params) {
        return asyncExecCypherQuery(cypherQuery, params, IBoltClient.jsonMappingFunction());
    }

    @Override
    public <T> Future<T> asyncExecCypherQuery(String cypherQuery, Function<Result, T> mappingFunction) {
        return asyncExecCypherQuery(cypherQuery, null, mappingFunction);
    }

    @Override
    public <T> Future<T> asyncExecCypherQuery(String cypherQuery, Map<String, Object> params, Function<Result, T> mappingFunction) {
        assertConnected();
        checkNotNull(cypherQuery);
        checkNotNull(mappingFunction);

        try (Session session = this._driver.session()) {

            // create future task
            FutureTask<T> futureTask = new FutureTask<T>(
                    new StatementCallable<T>(this._driver, checkNotNull(cypherQuery), params, result -> mappingFunction.apply(result)));

            // execute
            this._executorService.execute(futureTask);

            // return the running task
            return futureTask;
        }
    }

    @Override
    public Future<Void> asyncExecAndConsume(String cypherQuery, Consumer<Result> consumer) {
        return asyncExecAndConsume(cypherQuery, null, consumer);
    }

    @Override
    public Future<Void> asyncExecAndConsume(String cypherQuery, Map<String, Object> params, Consumer<Result> consumer) {
        assertConnected();
        checkNotNull(cypherQuery);
        checkNotNull(consumer);

        try (Session session = this._driver.session()) {

            // create future task
            FutureTask<Void> futureTask = new FutureTask<Void>(
                    new StatementCallable<Void>(this._driver, checkNotNull(cypherQuery), params, result -> {
                        consumer.accept(result);
                        return null;
                    }));

            // execute
            this._executorService.execute(futureTask);

            // return the running task
            return futureTask;
        }
    }

    /**
     * <p>
     * </p>
     *
     * @param connected
     */
    protected void setConnected(boolean connected) {
        boolean oldValue = this._connected;
        this._connected = connected;
        this._propertyChangeSupport.firePropertyChange("connected", oldValue, connected);
    }

    /**
     * <p>
     * </p>
     */
    private void assertConnected() {
        if (!isConnected()) {
            throw new RuntimeException("BoltClient is not connected.");
        }
    }
}
