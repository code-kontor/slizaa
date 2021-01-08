/**
 * slizaa-hierarchicalgraph-graphdb-testfwk - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.graphdb.testfwk;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import io.codekontor.slizaa.core.boltclient.IBoltClient;
import io.codekontor.slizaa.core.boltclient.testfwk.BoltClientConnectionRule;

import static org.assertj.core.api.Assertions.assertThat;


public class TestTest {

//  @ClassRule
//  public static Neo4jContainer neo4jContainer = 
//    new Neo4jContainer(DockerImageName.parse("neo4j:4.2.1"))
//      .withoutAuthentication()
//      .withNeo4jConfig("dbms.recovery.fail_on_missing_files", "false")
//      .withDatabase(MountableFile.forHostPath("C:\\tmp\\database\\mapstruct_1-2-0-Final-db\\neo4j"));
  
  // C:\tmp\database\mapstruct_1-2-0-Final-db
  
  
  
//  @ClassRule
//  public static BoltClientConnectionRule boltClientConnectionRule = new BoltClientConnectionRule(() -> neo4jContainer.getBoltUrl()); 

  @ClassRule
  public static GraphDatabaseSetupRule graphDatabaseSetup = new GraphDatabaseSetupRule("/mapstruct_1-2-0-Final-db.zip");

  @Test
  public void testTest() {
    
    IBoltClient boltClient = graphDatabaseSetup.getBoltClient();

    int count =  boltClient.syncExecCypherQuery("MATCH (node) RETURN count(node)", result -> result.single().get("count(node)").asInt());
    assertThat(count).isEqualTo(40549);
  }
}
