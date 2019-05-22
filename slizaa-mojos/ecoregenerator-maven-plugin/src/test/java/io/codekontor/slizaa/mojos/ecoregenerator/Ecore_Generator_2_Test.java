/**
 * ecoregenerator-maven-plugin - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.mojos.ecoregenerator;

import org.apache.maven.plugin.testing.resources.TestResources;
import org.junit.Test;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class Ecore_Generator_2_Test extends AbstractEcoreGeneratorTest {

  /**
   * <p>
   * Creates a new instance of type {@link Ecore_Generator_2_Test}.
   * </p>
   */
  public Ecore_Generator_2_Test() {
    super("ecore-generator-2");
  }

  /**
   * <p>
   * </p>
   *
   * @throws Exception
   */
  @Test
  public void test() throws Exception {

    //
    getEcoreGeneratorMojo().execute();

    // @formatter:off
    TestResources.assertDirectoryContents(getBasedir(),
        replaceSeparator(
        "model/",
        "model/hierarchicalgraph-neo4j.ecore",
        "model/hierarchicalgraph-neo4j.genmodel",
        "pom.xml",
        "target/",
        "target/workspace/",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/model/",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/model/hierarchicalgraph.ecore",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/model/hierarchicalgraph.genmodel",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/META-INF/",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/META-INF/MANIFEST.MF",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/build.properties",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/plugin.properties",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/plugin.xml",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/src-gen/",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/src-gen/io/",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/src-gen/io/codekontor/",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/src-gen/io/codekontor/slizaa/",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/src-gen/io/codekontor/slizaa/neo4j/",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/src-gen/io/codekontor/slizaa/neo4j/hierarchicalgraph/",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/src-gen/io/codekontor/slizaa/neo4j/hierarchicalgraph/Neo4JBackedDependencySource.java",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/src-gen/io/codekontor/slizaa/neo4j/hierarchicalgraph/Neo4JBackedNodeSource.java",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/src-gen/io/codekontor/slizaa/neo4j/hierarchicalgraph/Neo4JBackedRootNodeSource.java",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/src-gen/io/codekontor/slizaa/neo4j/hierarchicalgraph/Neo4jHierarchicalgraphFactory.java",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/src-gen/io/codekontor/slizaa/neo4j/hierarchicalgraph/Neo4jHierarchicalgraphPackage.java",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/src-gen/io/codekontor/slizaa/neo4j/hierarchicalgraph/impl/",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/src-gen/io/codekontor/slizaa/neo4j/hierarchicalgraph/impl/Neo4JBackedDependencySourceImpl.java",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/src-gen/io/codekontor/slizaa/neo4j/hierarchicalgraph/impl/Neo4JBackedNodeSourceImpl.java",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/src-gen/io/codekontor/slizaa/neo4j/hierarchicalgraph/impl/Neo4JBackedRootNodeSourceImpl.java",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/src-gen/io/codekontor/slizaa/neo4j/hierarchicalgraph/impl/Neo4jHierarchicalgraphFactoryImpl.java",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/src-gen/io/codekontor/slizaa/neo4j/hierarchicalgraph/impl/Neo4jHierarchicalgraphPackageImpl.java",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/src-gen/io/codekontor/slizaa/neo4j/hierarchicalgraph/util/",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/src-gen/io/codekontor/slizaa/neo4j/hierarchicalgraph/util/Neo4jHierarchicalgraphAdapterFactory.java",
        "target/workspace/io.codekontor.slizaa.neo4j.hierarchicalgraph/src-gen/io/codekontor/slizaa/neo4j/hierarchicalgraph/util/Neo4jHierarchicalgraphSwitch.java"));
    // @formatter:on

    // https://stackoverflow.com/questions/9386348/register-ecore-meta-model-programmatically
  }
}
