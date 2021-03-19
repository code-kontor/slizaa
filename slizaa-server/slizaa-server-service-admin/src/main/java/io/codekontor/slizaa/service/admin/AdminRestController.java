/**
 * slizaa-server-service-admin - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.service.admin;

import io.codekontor.slizaa.server.service.provisioning.IProvisioningService;
import io.codekontor.slizaa.server.service.provisioning.model.descr.GraphDatabaseDescr;
import io.codekontor.slizaa.server.service.provisioning.model.descr.ServerExtensionDescr;
import io.codekontor.slizaa.server.service.provisioning.model.descr.SlizaaServerConfigurationDescr;
import io.codekontor.slizaa.server.service.provisioning.model.request.GraphDatabaseRequest;
import io.codekontor.slizaa.server.service.provisioning.model.request.SlizaaServerConfigurationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/slizaa-admin-rest")
public class AdminRestController {

    private static final String PREFIX_SLIZAA_ADMIN_REST = "/slizaa-admin-rest/";

    @Autowired
    private IProvisioningService _provisioningService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public SlizaaServerConfigurationDescr description() {
        return _provisioningService.fetchServerDescription();
    }

    @GetMapping("/serverExtensions")
    public List<ServerExtensionDescr> serverExtensions() {
        return _provisioningService.fetchServerDescription().getServerExtensions();
    }

    @GetMapping("/graphDatabases")
    public List<GraphDatabaseDescr> graphDatabases() {
        return _provisioningService.fetchServerDescription().getGraphDatabases();
    }

    @GetMapping("/graphDatabases/{id}")
    public GraphDatabaseDescr graphDatabases(@PathVariable(value = "id") String id) {
        return _provisioningService.fetchGraphDatabaseDescription(id);
    }

	@PostMapping(value = "/", consumes = "application/json", produces = "application/json")
	public void graphDatabaseRequest(@RequestBody SlizaaServerConfigurationRequest slizaaServerConfigurationRequest) {
        _provisioningService.provision(slizaaServerConfigurationRequest);
	}
}