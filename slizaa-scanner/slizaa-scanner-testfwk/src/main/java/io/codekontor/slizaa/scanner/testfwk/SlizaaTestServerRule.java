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
package io.codekontor.slizaa.scanner.testfwk;

import io.codekontor.slizaa.core.mvnresolver.api.IMvnResolverService.IMvnResolverJob;
import io.codekontor.slizaa.core.progressmonitor.DefaultProgressMonitor;
import io.codekontor.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import io.codekontor.slizaa.scanner.api.graphdb.IGraphDb;
import io.codekontor.slizaa.scanner.api.graphdb.IGraphDbFactory;
import io.codekontor.slizaa.scanner.api.importer.IModelImporter;
import io.codekontor.slizaa.scanner.api.importer.IModelImporterFactory;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import io.codekontor.slizaa.scanner.spi.parser.IParserFactory;
import io.codekontor.slizaa.scanner.testfwk.internal.VersionAsInProjectResolver;
import io.codekontor.slizaa.scanner.testfwk.internal.ZipUtil;

import java.io.File;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class SlizaaTestServerRule extends AbstractSlizaaTestServerRule {

  /**
   * -
   */
  private IContentDefinitionProvider _contentDefinitionProvider;

  /**
   * <p>
   * Creates a new instance of type {@link SlizaaTestServerRule}.
   * </p>
   *
   * @param contentDefinitionProvider
   * @param backEndLoaderConfigurer
   */
  public SlizaaTestServerRule(IContentDefinitionProvider contentDefinitionProvider,
      Consumer<IMvnResolverJob> backEndLoaderConfigurer) {

    //
    super(backEndLoaderConfigurer);

    //
    this._contentDefinitionProvider = contentDefinitionProvider;
  }

  /**
   * <p>
   * Creates a new instance of type {@link SlizaaTestServerRule}.
   * </p>
   *
   * @param workingDirectory
   * @param contentDefinitionProvider
   * @param backEndLoaderConfigurer
   */
  public SlizaaTestServerRule(File workingDirectory, IContentDefinitionProvider contentDefinitionProvider,
      Consumer<IMvnResolverJob> backEndLoaderConfigurer) {

    //
    super(workingDirectory, backEndLoaderConfigurer);

    //
    this._contentDefinitionProvider = contentDefinitionProvider;
  }

  public static String mavenArtifact(String groupId, String artifactId, String version) {
    return String.format("%s:%s:%s", checkNotNull(groupId), checkNotNull(artifactId), checkNotNull(version));
  }

  /**
   * <p>
   * </p>
   *
   * @param groupId
   * @param artifactId
   * @param versionFunction
   * @return
   */
  public static String mavenArtifact(String groupId, String artifactId,
      BiFunction<String, String, String> versionFunction) {

    //
    return String.format("%s:%s:%s", checkNotNull(groupId), checkNotNull(artifactId),
        checkNotNull(versionFunction).apply(groupId, artifactId));
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public static BiFunction<String, String, String> versionAsInProject() {
    return (groupId, artifactId) -> {
      return VersionAsInProjectResolver.resolveVersion(groupId, artifactId);
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IGraphDb createGraphDatabase(ITestFwkBackEnd testFwkBackEnd) {

    //
    IGraphDbFactory graphDbFactory = testFwkBackEnd.getGraphDbFactory();
    IModelImporterFactory modelImporterFactory = testFwkBackEnd.getModelImporterFactory();
    List<IParserFactory> parserFactories = testFwkBackEnd.getParserFactories();
    ICypherStatementRegistry cypherStatementRegistry = testFwkBackEnd.getCypherStatementRegistry();

    // parse
    IModelImporter modelImporter = modelImporterFactory.createModelImporter(
        SlizaaTestServerRule.this._contentDefinitionProvider, SlizaaTestServerRule.this.getDatabaseDirectory(),
        parserFactories, cypherStatementRegistry.getAllStatements());

    //
    DefaultProgressMonitor progressMonitor = new DefaultProgressMonitor("Parse", 100,
        DefaultProgressMonitor.consoleLogger());

    //
    executeWithThreadContextClassLoader(testFwkBackEnd.getClassLoader(),
        () -> modelImporter.parse(progressMonitor,
            () -> graphDbFactory.newGraphDb(5001, SlizaaTestServerRule.this.getDatabaseDirectory()).create()));

    //
    return modelImporter.getGraphDb();
  }

  /**
   * <p>
   * </p>
   *
   * @param file
   * @throws Exception
   */
  public void exportDatabaseAsZipFile(String file, boolean restart) throws Exception {
    this.getGraphDb().shutdown();

    ZipUtil.zipFile(this.getDatabaseDirectory().getAbsolutePath(), checkNotNull(file), true);
    if (restart) {
      setGraphDb(this.getTestFwkBackEnd().getGraphDbFactory().newGraphDb(5001, this.getDatabaseDirectory()).create());
    }
  }
}
