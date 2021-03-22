/**
 * slizaa-server-service-provisioning - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.provisioning.model.diff;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Equivalence;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import io.codekontor.slizaa.server.service.provisioning.internal.diff.ServerConfigurationDiff;
import io.codekontor.slizaa.server.service.provisioning.model.IGraphDatabaseDTO;
import io.codekontor.slizaa.server.service.provisioning.model.ISlizaaServerConfigurationDTO;

public class ServerConfigurationDiffCreator {

  public static IServerConfigurationDiff<IGraphDatabaseDTO> createGraphDatabaseDiff(ISlizaaServerConfigurationDTO oldServerConfiguration, ISlizaaServerConfigurationDTO newServerConfiguration) {

    //
    checkNotNull(oldServerConfiguration);
    checkNotNull(newServerConfiguration);

    //
    Map<String, IGraphDatabaseDTO> oldEnvironmentComponents = asGraphDatabaseMap(oldServerConfiguration);
    Map<String, IGraphDatabaseDTO> newEnvironmentComponents = asGraphDatabaseMap(newServerConfiguration);
    MapDifference<String, IGraphDatabaseDTO> diff_1 = Maps.difference(oldEnvironmentComponents,
        newEnvironmentComponents, new Equivalence<IGraphDatabaseDTO>() {

          @Override
          protected boolean doEquivalent(IGraphDatabaseDTO graphDatabaseDTO_1, IGraphDatabaseDTO graphDatabaseDTO_2) {
            return graphDatabaseDTO_1.equals(graphDatabaseDTO_2);
          }

          @Override
          protected int doHash(IGraphDatabaseDTO graphDatabaseDTO) {
            return graphDatabaseDTO.hashCode();
          }
        });

    //
    return new ServerConfigurationDiff(diff_1.entriesOnlyOnLeft().values(), diff_1.entriesOnlyOnRight().values(),
        diff_1.entriesDiffering().values());
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  private static Map<String, IGraphDatabaseDTO> asGraphDatabaseMap(ISlizaaServerConfigurationDTO serverConfiguration) {

    //
    if (serverConfiguration == null) {
      return Collections.emptyMap();
    }

    //
    Map<String, IGraphDatabaseDTO> map = new HashMap<>();
    for (IGraphDatabaseDTO graphDatabaseDTO : serverConfiguration.getGraphDatabases()) {
      map.put(graphDatabaseDTO.getId(), graphDatabaseDTO);
    }
    return map;
  }
}
