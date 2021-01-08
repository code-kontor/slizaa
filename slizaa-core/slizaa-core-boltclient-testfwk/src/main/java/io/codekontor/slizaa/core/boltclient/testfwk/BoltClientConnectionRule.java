/**
 * slizaa-core-boltclient-testfwk - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.core.boltclient.testfwk;

import static com.google.common.base.Preconditions.checkNotNull;

import java.security.Provider;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import org.junit.rules.ExternalResource;
import io.codekontor.slizaa.core.boltclient.IBoltClient;
import io.codekontor.slizaa.core.boltclient.IBoltClientFactory;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class BoltClientConnectionRule extends ExternalResource {

  /** - */
  private IBoltClient _boltClient;
  
  private Supplier<String> _boltUriSupplier;

  /**
   * <p>
   * Creates a new instance of type {@link BoltClientConnectionRule}.
   * </p>
   *
   */
  public BoltClientConnectionRule() {
    this("localhost", 5001);
  }

  /**
   * <p>
   * Creates a new instance of type {@link BoltClientConnectionRule}.
   * </p>
   *
   * @param host
   * @param port
   */
  public BoltClientConnectionRule(String host, int port) {
    this._boltUriSupplier = () -> String.format("bolt://%s:%s", host, port);
  }
  
  public BoltClientConnectionRule(Supplier<String> boltUriSupplier) {
   this._boltUriSupplier = checkNotNull(boltUriSupplier);
  }

  /**
   * <p>
   * </p>
   *
   * @return the neo4JRemoteRepository
   */
  public IBoltClient getBoltClient() {
    return this._boltClient;
  }

  @Override
  protected void before() throws Throwable {
    this._boltClient = IBoltClientFactory.newInstance(Executors.newFixedThreadPool(5))
        .createBoltClient(_boltUriSupplier.get());
    this._boltClient.connect();
  }

  @Override
  protected void after() {

    try {
      this._boltClient.disconnect();
    } catch (Exception e) {
      //
    }
  }
}
