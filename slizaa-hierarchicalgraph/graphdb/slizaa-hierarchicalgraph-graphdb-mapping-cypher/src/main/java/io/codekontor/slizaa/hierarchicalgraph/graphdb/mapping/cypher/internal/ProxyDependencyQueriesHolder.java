/**
 * slizaa-hierarchicalgraph-graphdb-mapping-cypher - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.cypher.internal;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class ProxyDependencyQueriesHolder {

  /** - */
  private String[] _proxyDependencyQueries;

  /** - */
  private String[] _detailDependencyQueries;

  /**
   * <p>
   * Creates a new instance of type {@link ProxyDependencyQueriesHolder}.
   * </p>
   *
   * @param proxyDependencyQueries
   * @param detailDependencyQueries
   */
  public ProxyDependencyQueriesHolder(String[] proxyDependencyQueries, String[] detailDependencyQueries) {
    this._proxyDependencyQueries = checkNotNull(proxyDependencyQueries);
    this._detailDependencyQueries = checkNotNull(detailDependencyQueries);
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public String[] proxyDependencyQueries() {
    return this._proxyDependencyQueries;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public String[] detailDependencyQueries() {
    return this._detailDependencyQueries;
  }
}
