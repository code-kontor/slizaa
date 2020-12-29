/**
 * slizaa-server-graphql - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.graphql.graphdatabase;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;
import io.codekontor.slizaa.server.slizaadb.IHierarchicalGraph;
import io.codekontor.slizaa.server.graphql.hierarchicalgraph.HierarchicalGraph;
import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLResolver;

/**
 * 
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
@Component
public class GraphDatabaseResolver implements GraphQLResolver<GraphDatabase> {

	@Autowired
	private ISlizaaService slizaaService;

	public List<HierarchicalGraph> getHierarchicalGraphs(GraphDatabase graphDatabase) {
		
		ISlizaaDatabase db = slizaaService.getGraphDatabase(graphDatabase.getIdentifier());
		
		if (db != null) {
			return db.getHierarchicalGraphs().stream()
					.map(hg -> new HierarchicalGraph(graphDatabase.getIdentifier(), hg.getIdentifier()))
					.collect(Collectors.toList());
		}
		
		return Collections.emptyList();
	}

	public HierarchicalGraph hierarchicalGraph(GraphDatabase graphDatabase, String identifier) {
		
		ISlizaaDatabase db = slizaaService.getGraphDatabase(graphDatabase.getIdentifier());
		
		if (db != null) {
			IHierarchicalGraph hg = db.getHierarchicalGraph(identifier);
			if (hg != null) {
				return new HierarchicalGraph(graphDatabase.getIdentifier(), identifier);
			}
		}
		
		return null;
	}
}
