/**
 * slizaa-server-specs-api - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.specs.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import io.codekontor.slizaa.server.specs.internal.api.ServerExtensionSpec;
import io.codekontor.slizaa.server.specs.internal.model.graphdatabase.GraphDatabases;
import io.codekontor.slizaa.server.specs.internal.model.serverconfig.AvailableServerExtensions;
import io.codekontor.slizaa.server.specs.internal.model.serverconfig.ServerConfiguration;

public class QueryExecutor {

  private static final Logger logger                                         = LoggerFactory
      .getLogger(QueryExecutor.class);

  public static final String  QUERY_BODY                                     = "{  \"query\": \"%s\" }";

  public static final String  QUERY_SERVER_CONFIGURATION                     = "query serverConfiguration { serverConfiguration { hasInstalledExtensions installedExtensions { symbolicName version } } }";

  public static final String  QUERY_GRAPH_DATABASES                          = "query graphDatabases { graphDatabases { identifier state port availableActions contentDefinition { type { identifier name description } definition } hierarchicalGraphs {  identifier  } } }";

  public static final String  QUERY_AVAILABLE_SERVER_EXTENSIONS              = "query availableServerExtensions { availableServerExtensions { symbolicName version } }";

  public static final String  MUTATION_INSTALL_SERVER_EXTENSION              = "mutation installServerExtensions { installServerExtensions(extensions: %s) { symbolicName version } }";

  public static final String  MUTATION_CREATE_GRAPH_DATABASE                 = "mutation createGraphDatabase { createGraphDatabase(databaseId: \\\"%s\\\") { identifier state } }";

  public static final String  MUTATION_DELETE_GRAPH_DATABASE                 = "mutation deleteGraphDatabase { deleteGraphDatabase(databaseId: \\\"%s\\\") { identifier state } }";

  public static final String  MUTATION_SET_GRAPH_DATABASE_CONTENT_DEFINITION = "mutation setGraphDatabaseContentDefinition { setGraphDatabaseContentDefinition(databaseId: \\\"%s\\\", contentDefinitionFactoryId: \\\"%s\\\", contentDefinition: \\\"%s\\\") { identifier state } }";

  public static final String  MUTATION_PARSE_GRAPH_DATABASE                  = "mutation parseGraphDatabase { parseGraphDatabase(databaseId: \\\"%s\\\") { identifier state } }";

  public static final String  MUTATION_START_GRAPH_DATABASE                  = "mutation startGraphDatabase { startGraphDatabase(databaseId: \\\"%s\\\") { identifier state } }";

  public static final String  MUTATION_STOP_GRAPH_DATABASE                   = "mutation stopGraphDatabase { stopGraphDatabase(databaseId: \\\"%s\\\") { identifier state } }";

  public static final String  MUTATION_CREATE_HIERARCHICAL_GRAPH             = "mutation createHierarchicalGraph { createHierarchicalGraph(databaseId: \\\"%s\\\", hierarchicalGraphId: \\\"%s\\\") { availableActions state hierarchicalGraphs { identifier } } }";

  /** - */
  private String              _url;

  /** - */
  private ObjectMapper        _objectMapper;

  /**
   * 
   * @param url
   */
  public QueryExecutor(String url) {
    _url = checkNotNull(url);
    _objectMapper = new ObjectMapper();
  }

  public GraphDatabases fetchGraphDatabases() {

    JsonNode jsonNode = executeJsonQuery(QueryExecutor.QUERY_GRAPH_DATABASES);

    JSONObject jsonObject = jsonNode.getObject().getJSONObject("data");

    return convert(jsonObject, GraphDatabases.class);
  }

  public ServerConfiguration fetchServerConfiguration() {

    JsonNode jsonNode = executeJsonQuery(QueryExecutor.QUERY_SERVER_CONFIGURATION);

    JSONObject jsonObject = jsonNode.getObject().getJSONObject("data").getJSONObject("serverConfiguration");

    return convert(jsonObject, ServerConfiguration.class);
  }

  public AvailableServerExtensions fetchAvailableServerExtensions() {

    JsonNode jsonNode = executeJsonQuery(QueryExecutor.QUERY_AVAILABLE_SERVER_EXTENSIONS);

    JSONObject jsonObject = jsonNode.getObject().getJSONObject("data");

    return convert(jsonObject, AvailableServerExtensions.class);
  }

  /**
   * 
   * @param serverExtensions
   * @
   */
  public void installServerExtensions(List<ServerExtensionSpec> serverExtensions) {

    com.fasterxml.jackson.databind.JsonNode node = _objectMapper.convertValue(serverExtensions,
        com.fasterxml.jackson.databind.JsonNode.class);

    String mutation = String.format(MUTATION_INSTALL_SERVER_EXTENSION, node.toString());
    mutation = mutation.replace("\"symbolicName\"", "symbolicName");
    mutation = mutation.replace("\"version\"", "version");
    mutation = mutation.replace("\"", "\\\"");
    executeJsonQuery(mutation);
  }

  public void deleteGraphDatabase(String databaseId) {
    executeJsonQuery(String.format(MUTATION_DELETE_GRAPH_DATABASE, databaseId));
  }

  public void createGraphDatabase(String databaseId) {
    executeJsonQuery(String.format(MUTATION_CREATE_GRAPH_DATABASE, databaseId));
  }

  public void setGraphDatabaseContentDefinition(String databaseId, String contentDefinitionFactoryId,
      String contentDefinition) {
    executeJsonQuery(String.format(MUTATION_SET_GRAPH_DATABASE_CONTENT_DEFINITION, databaseId,
        contentDefinitionFactoryId, contentDefinition));
  }

  public void startGraphDatabase(String databaseId) {
    executeJsonQuery(String.format(MUTATION_START_GRAPH_DATABASE, databaseId));
  }

  public void stopGraphDatabase(String databaseId) {
    executeJsonQuery(String.format(MUTATION_STOP_GRAPH_DATABASE, databaseId));
  }

  public void parseGraphDatabase(String databaseId) {
    executeJsonQuery(String.format(MUTATION_PARSE_GRAPH_DATABASE, databaseId));
  }

  public void createHierarchicalGraph(String databaseId, String hierrachicalGraphId) {
    executeJsonQuery(String.format(MUTATION_CREATE_HIERARCHICAL_GRAPH, databaseId, hierrachicalGraphId));
  }

  public Object getUrl() {
    return _url;
  }

  private JsonNode executeJsonQuery(String jsonQuery) {

    try {
      String query = String.format(QueryExecutor.QUERY_BODY, jsonQuery);

      if (logger.isDebugEnabled()) {
        logger.debug("=> '{}' - '{}'.", query, _url);
      }

      HttpResponse<JsonNode> response = Unirest.post(_url).body(query).asJson();
      JsonNode result = response.getBody();

      if (logger.isDebugEnabled()) {
        logger.debug("<= '{}' - '{}'.", result.toString(), _url);
      }

      return result;
    } catch (UnirestException e) {
      throw new RuntimeException(e);
    }
  }

  private <T> T convert(JSONObject jsonObject, Class<T> type) {
    
    try {
      return _objectMapper.readValue(jsonObject.toString(), type);
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
