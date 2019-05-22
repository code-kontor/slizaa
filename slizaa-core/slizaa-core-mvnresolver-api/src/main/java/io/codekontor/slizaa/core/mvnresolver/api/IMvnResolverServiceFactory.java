/**
 * slizaa-core-mvnresolver-api - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.core.mvnresolver.api;

import java.io.File;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public interface IMvnResolverServiceFactory {

  /**
   * <p>
   * </p>
   *
   * @return
   */
  MvnResolverServiceFactoryBuilder newMvnResolverService();

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
   */
  public static interface MvnResolverServiceFactoryBuilder {

    /**
     * <p>
     * </p>
     *
     * @param id
     * @param url
     * @return
     */
    MvnResolverServiceFactoryBuilder withRemoteRepository(String id, String url);

    /**
     * <p>
     * </p>
     *
     * @return
     */
    MvnResolverServiceFactoryBuilder withDefaultRemoteRepository();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    IMvnResolverService create();
  }
}
