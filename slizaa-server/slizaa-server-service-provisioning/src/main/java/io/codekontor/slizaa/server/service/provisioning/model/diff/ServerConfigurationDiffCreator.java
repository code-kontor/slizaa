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
