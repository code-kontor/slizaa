/**
 * slizaa-server-admin-rest - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.rest.impl;

import io.codekontor.slizaa.server.descr.GraphDatabaseDescr;
import io.codekontor.slizaa.server.descr.ServerExtensionDescr;
import io.codekontor.slizaa.server.descr.SlizaaServerDescr;
import io.codekontor.slizaa.service.spec.ISpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("slizaa-admin-rest")
public class AdminRestController {

	@Autowired
	private ISpecService specService;

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/")
	public SlizaaServerDescr description() {
		return specService.fetchDescription();
	}

	@GetMapping("/serverExtensions")
	public List<ServerExtensionDescr> serverExtensions() {
		return specService.fetchDescription().getServerExtensions();
	}

	@GetMapping("/graphDatabases")
	public List<GraphDatabaseDescr> graphDatabases() {
		return specService.fetchDescription().getGraphDatabases();
	}
}