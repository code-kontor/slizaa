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
package io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.cypher;

import io.codekontor.slizaa.core.boltclient.IBoltClient;
import io.codekontor.slizaa.core.progressmonitor.IProgressMonitor;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.IHierarchyDefinitionProvider;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractQueryBasedHierarchyProvider implements IHierarchyDefinitionProvider, IBoltClientAware {

  /** - */
  private List<Long>   _toplevelNodeIds;

  /** - */
  private List<Long[]> _parentChildNodeIdsQueries;

  /**
   * {@inheritDoc}
   */
  @Override
  public void initialize(IBoltClient boltClient, IProgressMonitor progressMonitor) throws Exception {

    checkNotNull(boltClient);

    //
    this._toplevelNodeIds = new ArrayList<>();
    for (String query : toplevelNodeIdQueries()) {
      boltClient.syncExecAndConsume(query, result -> {
        result.forEachRemaining(record -> this._toplevelNodeIds.add(record.get(0).asLong()));
      });
    }

    //
    this._parentChildNodeIdsQueries = new ArrayList<>();
    for (String query : parentChildNodeIdsQueries()) {
      boltClient.syncExecAndConsume(query, result -> {
        result.forEachRemaining(record -> this._parentChildNodeIdsQueries.add(new Long[] { record.get(0).asLong(), record.get(1).asLong() }));
      });
    }
  }

  @Override
  public List<Long> getToplevelNodeIds() throws Exception {
    return this._toplevelNodeIds;
  }

  @Override
  public List<Long[]> getParentChildNodeIds() throws Exception {
    return this._parentChildNodeIdsQueries;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  protected abstract String[] toplevelNodeIdQueries();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  protected abstract String[] parentChildNodeIdsQueries();
}
