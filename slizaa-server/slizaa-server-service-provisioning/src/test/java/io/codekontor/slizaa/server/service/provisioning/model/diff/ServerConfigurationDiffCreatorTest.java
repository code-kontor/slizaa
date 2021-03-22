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

import io.codekontor.slizaa.server.service.provisioning.model.IGraphDatabaseDTO;
import io.codekontor.slizaa.server.service.provisioning.model.descr.ContentDefinitionDescr;
import io.codekontor.slizaa.server.service.provisioning.model.descr.GraphDatabaseDescr;
import io.codekontor.slizaa.server.service.provisioning.model.descr.HierarchicalGraphDescr;
import io.codekontor.slizaa.server.service.provisioning.model.descr.SlizaaServerConfigurationDescr;
import io.codekontor.slizaa.server.service.provisioning.model.request.ContentDefinitionRequest;
import io.codekontor.slizaa.server.service.provisioning.model.request.GraphDatabaseRequest;
import io.codekontor.slizaa.server.service.provisioning.model.request.SlizaaServerConfigurationRequest;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Collections;

public class ServerConfigurationDiffCreatorTest {

    @Test
    public void testIt() {

        GraphDatabaseRequest graphDatabaseRequest = new GraphDatabaseRequest(
                "db01",
                new ContentDefinitionRequest("factoryIdShortForm", "definition"),
                Collections.emptyList());

        SlizaaServerConfigurationRequest serverConfigurationRequest = new SlizaaServerConfigurationRequest(Collections.singletonList(graphDatabaseRequest));

        GraphDatabaseDescr graphDatabaseDescription = new GraphDatabaseDescr(
                "db01",
                new ContentDefinitionDescr("factoryId", "factoryIdShortForm2", "definition"),
                Collections.emptyList(),
                "Running",
                1234,
                Collections.emptyList());

        SlizaaServerConfigurationDescr serverConfigurationDescr = new SlizaaServerConfigurationDescr(Collections.emptyList(), Collections.singletonList(graphDatabaseDescription));

        IServerConfigurationDiff<IGraphDatabaseDTO>  serverConfigurationDiff =
                ServerConfigurationDiffCreator.createGraphDatabaseDiff(serverConfigurationDescr, serverConfigurationRequest);

        System.out.println(serverConfigurationDiff.getComponentsToCreate());
        System.out.println(serverConfigurationDiff.getComponentsToRemove());

        serverConfigurationDiff.getComponentsToModify().forEach(
                vd -> {
                    System.out.println("old: " + vd.leftValue());
                    System.out.println("new: " + vd.rightValue());
                }
        );

    }
}
