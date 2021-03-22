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
package io.codekontor.slizaa.server.service.provisioning;

import com.google.common.io.Files;
import io.codekontor.slizaa.server.service.provisioning.model.request.ContentDefinitionRequest;
import io.codekontor.slizaa.server.service.provisioning.model.request.GraphDatabaseRequest;
import io.codekontor.slizaa.server.service.provisioning.model.request.HierarchicalGraphRequest;
import io.codekontor.slizaa.server.service.provisioning.model.request.SlizaaServerConfigurationRequest;

import java.io.File;
import java.util.Collections;

public class ServerConfigurationRequests {

    public static SlizaaServerConfigurationRequest getServerConfigurationRequest_nodb() {

        return new SlizaaServerConfigurationRequest(Collections.emptyList());
    }

    public static SlizaaServerConfigurationRequest getServerConfigurationRequest_db01() {

        File tempDirectory = Files.createTempDir();
        tempDirectory.deleteOnExit();

        HierarchicalGraphRequest hierarchicalGraphRequest = new HierarchicalGraphRequest();
        hierarchicalGraphRequest.setId("identifier");

        GraphDatabaseRequest graphDatabaseRequest = new GraphDatabaseRequest(
                "db01",
                new ContentDefinitionRequest("directory", tempDirectory.getAbsolutePath()),
                Collections.singletonList(hierarchicalGraphRequest));

        return new SlizaaServerConfigurationRequest(Collections.singletonList(graphDatabaseRequest));
    }

    public static SlizaaServerConfigurationRequest getServerConfigurationRequest_db02_v1() {

        File tempDirectory = Files.createTempDir();
        tempDirectory.deleteOnExit();

        HierarchicalGraphRequest hierarchicalGraphRequest = new HierarchicalGraphRequest();
        hierarchicalGraphRequest.setId("identifier_1");

        GraphDatabaseRequest graphDatabaseRequest = new GraphDatabaseRequest(
                "db02",
                new ContentDefinitionRequest("directory", tempDirectory.getAbsolutePath()),
                Collections.singletonList(hierarchicalGraphRequest));

        return new SlizaaServerConfigurationRequest(Collections.singletonList(graphDatabaseRequest));
    }

    public static SlizaaServerConfigurationRequest getServerConfigurationRequest_db02_v2() {

        File tempDirectory = Files.createTempDir();
        tempDirectory.deleteOnExit();

        HierarchicalGraphRequest hierarchicalGraphRequest = new HierarchicalGraphRequest();
        hierarchicalGraphRequest.setId("identifier_2");

        GraphDatabaseRequest graphDatabaseRequest = new GraphDatabaseRequest(
                "db02",
                new ContentDefinitionRequest("directory", tempDirectory.getAbsolutePath()),
                Collections.singletonList(hierarchicalGraphRequest));

        return new SlizaaServerConfigurationRequest(Collections.singletonList(graphDatabaseRequest));
    }
}
