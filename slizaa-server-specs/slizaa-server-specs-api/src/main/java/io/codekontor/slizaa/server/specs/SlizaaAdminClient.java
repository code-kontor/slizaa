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
package io.codekontor.slizaa.server.specs;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import io.codekontor.slizaa.server.specs.api.ISlizaaServerSpec;
import io.codekontor.slizaa.server.specs.internal.QueryExecutor;
import io.codekontor.slizaa.server.specs.internal.ServerSpecValidator;
import io.codekontor.slizaa.server.specs.internal.api.GraphdatabaseSpec;
import io.codekontor.slizaa.server.specs.internal.api.ServerExtensionSpec;
import io.codekontor.slizaa.server.specs.internal.api.SlizaaServerSpec;
import io.codekontor.slizaa.server.specs.internal.model.graphdatabase.GraphDatabase;
import io.codekontor.slizaa.server.specs.internal.model.graphdatabase.GraphDatabases;
import io.codekontor.slizaa.server.specs.internal.model.serverconfig.ServerConfiguration;
import io.codekontor.slizaa.server.specs.internal.model.serverconfig.ServerExtension;

public class SlizaaAdminClient {

  private static final Logger logger = LoggerFactory.getLogger(SlizaaAdminClient.class);

  private QueryExecutor       _queryExecutor;

  /**
   * Create a new {@link SlizaaAdminClient} for the specified URL.
   * 
   * @param url
   */
  public SlizaaAdminClient(String url) {
    _queryExecutor = new QueryExecutor(checkNotNull(url));
  }
  
  /**
   * 
   * @param configuration
   * @throws Exception
   */
  public void sync(ISlizaaServerSpec configuration) {

    Preconditions.checkNotNull(configuration);
    Preconditions.checkState(configuration instanceof SlizaaServerSpec,
        "Parameter slizaaServerConfiguration must be instance of SlizaaServerConfiguration.");

    SlizaaServerSpec slizaaServerSpec = (SlizaaServerSpec) configuration;

    // step 1: validate
    logger.info("Validating requested Slizaa server configuration...");
    ServerSpecValidator.validate(_queryExecutor, slizaaServerSpec);
    
    // step 2: sync
    logger.info("Starting sync with '{}'...", _queryExecutor.getUrl());
    syncServerConfiguration(slizaaServerSpec);
    syncGraphdatabaseSpecs(slizaaServerSpec);
  }

  private void syncServerConfiguration(SlizaaServerSpec slizaaServerSpec) {
    
    logger.info("Reading current server configuration...");

    ServerConfiguration configuration = _queryExecutor.fetchServerConfiguration();

    logger.info("Current server configuration successfully read.");

    // are there any missing extensions?
    List<ServerExtensionSpec> missingExtensions = slizaaServerSpec.getServerExtensionSpecs().stream()
        .filter(extensionSpec -> !configuration.getInstalledExtensions().contains(extensionSpec))
        .collect(Collectors.toList());

    // are there any unwanted extensions?
    List<ServerExtension> unwantedExtensions = configuration.getInstalledExtensions().stream()
        .filter(extension -> !slizaaServerSpec.getServerExtensionSpecs().contains(extension))
        .collect(Collectors.toList());

    // install server extensions if necessary
    if (!missingExtensions.isEmpty() || !unwantedExtensions.isEmpty()) {

      logger.info("Detected {} missing and {} obsolete server extensions.", missingExtensions.size(),
          unwantedExtensions.size());

      _queryExecutor.installServerExtensions(slizaaServerSpec.getServerExtensionSpecs());

    } else {

      logger.info("All server extensions up to date.", missingExtensions.size(), unwantedExtensions.size());
    }
  }

  private void syncGraphdatabaseSpecs(SlizaaServerSpec slizaaServerSpec) {

    logger.info("Reading current database configurations...");

    GraphDatabases graphDatabases = _queryExecutor.fetchGraphDatabases();

    logger.info("Database configurations successfully read.");

    Map<String, GraphDatabase> graphDatabaseMap = graphDatabases.getGraphDatabases().stream()
        .collect(Collectors.toMap(graphDatabase -> graphDatabase.getIdentifier(), graphDatabase -> graphDatabase));

    Map<String, GraphdatabaseSpec> graphDatabaseSpecMap = slizaaServerSpec.getGraphdatabaseSpecs().stream()
        .collect(Collectors.toMap(spec -> spec.getIdentifier(), spec -> spec));

    List<String> missingDbs = graphDatabaseSpecMap.keySet().stream().filter(id -> !graphDatabaseMap.containsKey(id))
        .collect(Collectors.toList());

    List<String> unwantedDbs = graphDatabaseMap.keySet().stream().filter(id -> !graphDatabaseSpecMap.containsKey(id))
        .collect(Collectors.toList());

    List<String> existingDbs = graphDatabaseSpecMap.keySet().stream().filter(id -> graphDatabaseMap.containsKey(id))
        .collect(Collectors.toList());

    if (!missingDbs.isEmpty()) {
      logger.info("Databases to create: {}.", missingDbs);
    }

    if (!unwantedDbs.isEmpty()) {
      logger.info("Databases to delete: {}.", unwantedDbs);
    }

    if (!existingDbs.isEmpty()) {
      logger.info("Databases to update: {}.", existingDbs);
    }

    unwantedDbs.forEach(databaseId -> deleteDatabase(databaseId));
    missingDbs.forEach(databaseId -> createDatabase(graphDatabaseSpecMap.get(databaseId)));
    existingDbs.forEach(databaseId -> updateDatabase(graphDatabaseMap.get(databaseId), graphDatabaseSpecMap.get(databaseId)));
  }

  private void createDatabase(GraphdatabaseSpec graphdatabaseSpec) {

    try {
      logger.info("Creating database '{}'.", graphdatabaseSpec.getIdentifier());
      _queryExecutor.createGraphDatabase(graphdatabaseSpec.getIdentifier());
      graphdatabaseSpec.getContentDefinitionSpecs().forEach(contentSpec -> {

        try {

          _queryExecutor.setGraphDatabaseContentDefinition(graphdatabaseSpec.getIdentifier(), contentSpec.getType(),
              contentSpec.getDefinition());

        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      });

      _queryExecutor.parseGraphDatabase(graphdatabaseSpec.getIdentifier());
      
      if (!graphdatabaseSpec.getHierarchicalGraphSpecs().isEmpty()) {
       
        graphdatabaseSpec.getHierarchicalGraphSpecs().forEach(hgSpec -> {
          
          try {
            _queryExecutor.createHierarchicalGraph(graphdatabaseSpec.getIdentifier(), hgSpec.getIdentifier());
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        });
        
      }
      
      logger.info("Successfully created database '{}'.", graphdatabaseSpec.getIdentifier());
    
    } catch (Exception exception) {
      logger.error("Error whilst creating database '" + graphdatabaseSpec.getIdentifier() + "'.", exception);
    }
  }

  private void deleteDatabase(String databaseId) {
    
    try {
      logger.info("Deleting database '{}'.", databaseId);
      _queryExecutor.deleteGraphDatabase(databaseId);
      logger.info("Successfully deleted database '{}'.", databaseId);      
    } catch (Exception exception) {
      logger.error("Error whilst deleting database '" + databaseId + "'.", exception);
    }
  }

  private void updateDatabase(GraphDatabase graphDatabase, GraphdatabaseSpec graphdatabaseSpec) {
    
    String databaseId = graphdatabaseSpec.getIdentifier();
        
    logger.info("Updating database '{}'.", graphdatabaseSpec.getIdentifier());
    
    if (graphdatabaseSpec.isForceRebuild()) {
      try {
        
        logger.info("Deleting database '{}'.", databaseId);
        _queryExecutor.deleteGraphDatabase(databaseId);
        logger.info("Successfully deleted database '{}'.", databaseId);
       
        createDatabase(graphdatabaseSpec);
      } catch (Exception exception) {
        logger.error("Error whilst deleting database '" + databaseId + "'.", exception);
      }
    }
    
    //TODO
    
    logger.info("Successfully updated database '{}'.", graphdatabaseSpec.getIdentifier());
  }
}
