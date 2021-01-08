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
package io.codekontor.slizaa.core.boltclient;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 *
 */
public class BoltClientTest extends AbstractBoltClientTest {

  @Test
  public void testGetNode() throws Exception {
    String result = boltClient().getNode(0);
    assertThat(result).isEqualTo("{\"id\":0,\"labels\":[\"Person\"],\"properties\":{\"name\":\"Andres\",\"title\":\"Developer\"},\"__type\":\"NODE\"}");
  }

  @Test
  public void testGetNode_CustomConverter() throws Exception {
    String result = boltClient().getNode(0, node -> node.id() + " : " + node.asMap().toString());
    assertThat(result).isEqualTo("0 : {name=Andres, title=Developer}");
  }

  @Test
  public void testSyncExecCypherQuery() throws Exception {
    String result = boltClient().syncExecCypherQuery("MATCH (n) return n", IBoltClient.jsonMappingFunction());
    assertThat(result).isEqualTo("[{\"n\":{\"id\":0,\"labels\":[\"Person\"],\"properties\":{\"name\":\"Andres\",\"title\":\"Developer\"},\"__type\":\"NODE\"}},{\"n\":{\"id\":1,\"labels\":[\"Person\"],\"properties\":{\"name\":\"George\",\"title\":\"Project Owner\"},\"__type\":\"NODE\"}}]");
  }
}
