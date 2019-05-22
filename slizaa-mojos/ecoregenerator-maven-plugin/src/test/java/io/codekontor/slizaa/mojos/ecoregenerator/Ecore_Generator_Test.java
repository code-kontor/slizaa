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
public class Ecore_Generator_Test extends AbstractEcoreGeneratorTest {

  /**
   * <p>
   * Creates a new instance of type {@link Ecore_Generator_Test}.
   * </p>
   */
  public Ecore_Generator_Test() {
    super("ecore-generator");
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
        "pom.xml", 
        "src/", 
        "src/main/", 
        "src/main/resources/",
        "src/main/resources/model/", 
        "src/main/resources/model/hierarchicalgraph.ecore",
        "src/main/resources/model/hierarchicalgraph.genmodel", 
        "target/", 
        "target/workspace/",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/META-INF/",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/META-INF/MANIFEST.MF",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/build.properties",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/plugin.properties",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/plugin.xml",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/AbstractHGDependency.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/DefaultDependencySource.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/DefaultNodeSource.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/HGAggregatedDependency.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/HGCoreDependency.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/HGNode.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/HGProxyDependency.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/HGRootNode.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/HierarchicalgraphFactory.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/HierarchicalgraphPackage.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/IDependencySource.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/INodeSource.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/SourceOrTarget.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/impl/",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/impl/AbstractHGDependencyImpl.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/impl/DefaultDependencySourceImpl.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/impl/DefaultNodeSourceImpl.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/impl/HGAggregatedDependencyImpl.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/impl/HGCoreDependencyImpl.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/impl/HGNodeImpl.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/impl/HGProxyDependencyImpl.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/impl/HGRootNodeImpl.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/impl/HierarchicalgraphFactoryImpl.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/impl/HierarchicalgraphPackageImpl.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/impl/IdentifierToNodeMapImpl.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/impl/NodeToCoreDependenciesMapImpl.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/impl/NodeToCoreDependencyMapImpl.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/impl/StringToObjectMapImpl.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/impl/StringToStringMapImpl.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/util/",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/util/HierarchicalgraphAdapterFactory.java",
        "target/workspace/io.codekontor.slizaa.hierarchicalgraph.core.model/src-gen/io/codekontor/slizaa/hierarchicalgraph/util/HierarchicalgraphSwitch.java"));
    // @formatter:on
  }
}
