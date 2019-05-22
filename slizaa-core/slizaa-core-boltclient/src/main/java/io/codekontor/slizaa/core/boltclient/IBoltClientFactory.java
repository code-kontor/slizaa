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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ExecutorService;

import io.codekontor.slizaa.core.boltclient.internal.BoltClientFactoryImpl;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 *
 */
public interface IBoltClientFactory {

  /**
   * <p>
   * </p>
   *
   * @param uri
   * @return
   */
  IBoltClient createBoltClient(String uri);

  /**
   * <p>
   * </p>
   *
   * @param uri
   * @param name
   * @return
   */
  IBoltClient createBoltClient(String uri, String name);

  /**
   * <p>
   * </p>
   *
   * @param uri
   * @param name
   * @param description
   * @return
   */
  IBoltClient createBoltClient(String uri, String name, String description);

  /**
   * <p>
   * </p>
   *
   * @param executorService
   * @return
   */
  public static IBoltClientFactory newInstance(ExecutorService executorService) {
    return new BoltClientFactoryImpl(checkNotNull(executorService));
  }
}
