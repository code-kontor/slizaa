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

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import io.codekontor.slizaa.mojos.ecoregenerator.fwk.Booter;
import io.codekontor.slizaa.mojos.ecoregenerator.fwk.ManualRepositorySystemFactory;

public class AbstractEcoreGeneratorTest {

  /** - */
  @Rule
  public MojoRule            mojoRule      = new MojoRule();

  /** - */
  @Rule
  public TestResources       testResources = new TestResources();

  /** - */
  private String             _testProjectName;

  /** - */
  private EcoreGeneratorMojo _ecoreGeneratorMojo;

  /** - */
  private File               _basedir;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractEcoreGeneratorTest}.
   * </p>
   *
   * @param testProjectName
   */
  public AbstractEcoreGeneratorTest(String testProjectName) {
    this._testProjectName = checkNotNull(testProjectName);
  }

  /**
   * <p>
   * </p>
   *
   * @throws Exception
   */
  @Before
  public void setup() throws Exception {

    //
    this._basedir = this.testResources.getBasedir(this._testProjectName);

    System.out.println(this._basedir);

    // get the test pom file
    File testPom = new File(this._basedir, "pom.xml").getAbsoluteFile();
    assertNotNull(testPom);

    //
    this._ecoreGeneratorMojo = (EcoreGeneratorMojo) this.mojoRule.configureMojo(new EcoreGeneratorMojo(),
        this.mojoRule.extractPluginConfiguration("ecoregenerator-maven-plugin", testPom));

    // Create the Maven project by hand (...)
    final MavenProject mvnProject = new MavenProject();
    mvnProject.setFile(testPom);
    mvnProject.getBuild().setDirectory(new File(this._basedir, "target").getAbsolutePath());
    this.mojoRule.setVariableValueToObject(this._ecoreGeneratorMojo, "project", mvnProject);

    //
    RepositorySystem repositorySystem = ManualRepositorySystemFactory.newRepositorySystem();
    this.mojoRule.setVariableValueToObject(this._ecoreGeneratorMojo, "repoSystem", repositorySystem);
    Assert.assertNotNull(this.mojoRule.getVariableValueFromObject(this._ecoreGeneratorMojo, "repoSystem"));

    //
    DefaultRepositorySystemSession systemSession = Booter.newRepositorySystemSession(repositorySystem);
    this.mojoRule.setVariableValueToObject(this._ecoreGeneratorMojo, "repoSession", systemSession);
    Assert.assertNotNull(this.mojoRule.getVariableValueFromObject(this._ecoreGeneratorMojo, "repoSession"));

    this.mojoRule.setVariableValueToObject(this._ecoreGeneratorMojo, "repositories",
        Booter.newRepositories(repositorySystem, systemSession));
    Assert.assertNotNull(this.mojoRule.getVariableValueFromObject(this._ecoreGeneratorMojo, "repositories"));
  }

  protected MojoRule getMojoRule() {
    return this.mojoRule;
  }

  protected TestResources getTestResources() {
    return this.testResources;
  }

  protected String getTestProjectName() {
    return this._testProjectName;
  }

  protected EcoreGeneratorMojo getEcoreGeneratorMojo() {
    return this._ecoreGeneratorMojo;
  }

  protected File getBasedir() {
    return this._basedir;
  }

  /**
   * <p>
   * </p>
   *
   * @param params
   * @return
   */
  protected String[] replaceSeparator(String... params) {
    String[] result = new String[params.length];
    for (int i = 0; i < params.length; i++) {
      result[i] = params[i].replace('\\', File.separatorChar).replace('/', File.separatorChar);
      if (result[i].endsWith(File.separator)) {
        result[i] = result[i].substring(0, result[i].length() - 1) + '/';
      }
    }
    return result;
  }
}
