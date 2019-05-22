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
package io.codekontor.slizaa.core.boltclient.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ExecutorService;

import io.codekontor.slizaa.core.boltclient.IBoltClient;
import io.codekontor.slizaa.core.boltclient.IBoltClientFactory;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 *
 */
public class BoltClientFactoryImpl implements IBoltClientFactory {

  /** - */
  private ExecutorService _executorService;

  /**
   * <p>
   * Creates a new instance of type {@link BoltClientFactoryImpl}.
   * </p>
   *
   * @param executorService
   */
  public BoltClientFactoryImpl(ExecutorService executorService) {
    this._executorService = checkNotNull(executorService);
  }

  @Override
  public IBoltClient createBoltClient(String uri) {
    return new BoltClientImpl(this._executorService, uri, uri, uri);
  }

  @Override
  public IBoltClient createBoltClient(String uri, String name) {
    return new BoltClientImpl(this._executorService, uri, name, uri);
  }

  /**
   * <p>
   * </p>
   *
   * @param uri
   * @param name
   * @param description
   * @return
   */
  @Override
  public IBoltClient createBoltClient(String uri, String name, String description) {
    return new BoltClientImpl(this._executorService, uri, name, description);
  }
}
