/**
 * slizaa-scanner-testfwk - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.scanner.testfwk;

import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 *
 */
@Ignore("This test has an invalid runtime dependency to 'io.codekontor.slizaa.neo4j' and 'io.codekontor.slizaa.jtype'.")
public class SlizaaTestServerRuleTest {

  // @formatter:off

  @ClassRule
  public static SlizaaTestServerRule slizaaTestServerRule = new SlizaaTestServerRule(

      // create the content provider
      ContentDefinitionProviderFactory.simpleBinaryMvnArtifact("com.google.guava", "guava", "25.1-jre"),

      // configure the backend
      job -> job.withDependency("io.codekontor.slizaa.neo4j:io.codekontor.slizaa.neo4j.importer:1.0.0-SNAPSHOT")
                .withDependency("io.codekontor.slizaa.neo4j:io.codekontor.slizaa.neo4j.graphdbfactory:1.0.0-SNAPSHOT")
                .withDependency("io.codekontor.slizaa.jtype:io.codekontor.slizaa.jtype.scanner:1.0.0-SNAPSHOT")
                .withExclusionPattern("*:io.codekontor.slizaa.scanner.spi-api")
                .withExclusionPattern("*:jdk.tools"));

  // @formatter:on

  @Test
  public void testIt() {

    // do nothing
  }
}
