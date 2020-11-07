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

import io.codekontor.slizaa.service.admin.descr.GraphDatabaseDescr;
import io.codekontor.slizaa.service.admin.descr.ServerExtensionDescr;
import io.codekontor.slizaa.service.admin.descr.SlizaaServerDescr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminRestController {

	private static final String PREFIX_SLIZAA_ADMIN_REST = "/slizaa-admin-rest/";
	
  @Autowired
	private IAdminService specService;

  @RequestMapping(value = PREFIX_SLIZAA_ADMIN_REST, method = RequestMethod.GET)
	public SlizaaServerDescr description() {
		return specService.fetchDescription();
	}

	@GetMapping(PREFIX_SLIZAA_ADMIN_REST + "serverExtensions")
	public List<ServerExtensionDescr> serverExtensions() {
		return specService.fetchDescription().getServerExtensions();
	}

	@GetMapping(PREFIX_SLIZAA_ADMIN_REST + "graphDatabases")
	public List<GraphDatabaseDescr> graphDatabases() {
		return specService.fetchDescription().getGraphDatabases();
	}
}