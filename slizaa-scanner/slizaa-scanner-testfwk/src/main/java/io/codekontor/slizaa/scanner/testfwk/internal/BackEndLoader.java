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
package io.codekontor.slizaa.scanner.testfwk.internal;


import io.codekontor.mvnresolver.MvnResolverServiceFactoryFactory;
import io.codekontor.mvnresolver.api.IMvnResolverService;
import io.codekontor.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import io.codekontor.slizaa.scanner.api.graphdb.IGraphDbFactory;
import io.codekontor.slizaa.scanner.api.importer.IModelImporterFactory;
import io.codekontor.slizaa.scanner.cypherregistry.CypherRegistryUtils;
import io.codekontor.slizaa.scanner.cypherregistry.CypherStatementRegistry;
import io.codekontor.slizaa.scanner.spi.parser.IParserFactory;
import io.codekontor.slizaa.scanner.testfwk.AbstractSlizaaTestServerRule.ITestFwkBackEnd;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class BackEndLoader implements ITestFwkBackEnd {

  /** - */
  private IModelImporterFactory    _modelImporterFactory;

  /** - */
  private IGraphDbFactory          _graphDbFactory;

  /** - */
  private List<IParserFactory>     _parserFactories;

  /** - */
  private ICypherStatementRegistry _cypherStatementRegistry;

  /** - */
  private ClassLoader              _classLoader;

  /**
   * <p>
   * Creates a new instance of type {@link BackEndLoader}.
   * </p>
   *
   * @param configurer
   */
  public BackEndLoader(Consumer<IMvnResolverService.IMvnResolverJob> configurer) {
    this(configurer, BackEndLoader.class.getClassLoader());
  }

  /**
   * <p>
   * Creates a new instance of type {@link BackEndLoader}.
   * </p>
   *
   * @param configurer
   * @param mainClassLoader
   */
  public BackEndLoader(Consumer<IMvnResolverService.IMvnResolverJob> configurer, ClassLoader mainClassLoader) {

    //
    checkNotNull(configurer);
    checkNotNull(mainClassLoader);

    // create new maven resolver job...
    IMvnResolverService mvnResolverService = MvnResolverServiceFactoryFactory.createNewResolverServiceFactory()
        .newMvnResolverService().withDefaultRemoteRepository()
        // TODO: Make configurable!
        .withRemoteRepository("oss_sonatype_snapshots", "https://oss.sonatype.org/content/repositories/snapshots")
        .create();

    //
    IMvnResolverService.IMvnResolverJob mvnResolverJob = mvnResolverService.newMvnResolverJob();

    // ...configure it...
    configurer.accept(mvnResolverJob);

    // ... and create a new class loader from the result
    this._classLoader = new URLClassLoader(mvnResolverJob.resolveToUrlArray(), mainClassLoader);

    // Step 1: load services via service loader mechanism
    this._modelImporterFactory = singleService(IModelImporterFactory.class, this._classLoader);
    this._graphDbFactory = singleService(IGraphDbFactory.class, this._classLoader);
    this._parserFactories = allServices(IParserFactory.class, this._classLoader);

    // Step 2: create the cypher statement registry
    this._cypherStatementRegistry = new CypherStatementRegistry(() -> {
      return CypherRegistryUtils.getCypherStatementsFromClasspath(this._classLoader);
    });
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  @Override
  public IModelImporterFactory getModelImporterFactory() {
    return this._modelImporterFactory;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  @Override
  public IGraphDbFactory getGraphDbFactory() {
    return this._graphDbFactory;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  @Override
  public ICypherStatementRegistry getCypherStatementRegistry() {
    return this._cypherStatementRegistry;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  @Override
  public List<IParserFactory> getParserFactories() {
    return this._parserFactories;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  @Override
  public ClassLoader getClassLoader() {
    return this._classLoader;
  }

  /**
   * <p>
   * </p>
   *
   * @param type
   * @param classLoader
   * @return
   */
  private static <T> T singleService(Class<T> type, ClassLoader classLoader) {

    //
    checkNotNull(type);
    checkNotNull(classLoader);

    // get the service loader
    ServiceLoader<T> serviceLoader = ServiceLoader.load(type, classLoader);

    //
    Iterator<T> iterator = serviceLoader.iterator();
    if (!iterator.hasNext()) {
      throw new RuntimeException(String.format("No service of type '%s' available.", type));
    }

    //
    return iterator.next();
  }

  /**
   * <p>
   * </p>
   *
   * @param type
   * @param classLoader
   * @return
   */
  private static <T> List<T> allServices(Class<T> type, ClassLoader classLoader) {

    //
    checkNotNull(type);
    checkNotNull(classLoader);

    //
    List<T> result = new ArrayList<>();

    // get the service loader
    ServiceLoader<T> serviceLoader = ServiceLoader.load(type, classLoader);

    //
    Iterator<T> iterator = serviceLoader.iterator();
    while (iterator.hasNext()) {
      result.add(iterator.next());
    }

    //
    return result;
  }
}
