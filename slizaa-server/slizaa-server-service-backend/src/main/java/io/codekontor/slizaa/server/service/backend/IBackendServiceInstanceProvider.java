/**
 * slizaa-server-service-backend - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.backend;

import java.util.List;

import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import io.codekontor.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import io.codekontor.slizaa.scanner.api.graphdb.IGraphDbFactory;
import io.codekontor.slizaa.scanner.api.importer.IModelImporterFactory;
import io.codekontor.slizaa.scanner.spi.parser.IParserFactory;

public interface IBackendServiceInstanceProvider extends IBackendService {

	/**
	 *
	 * @return
	 */
	boolean hasModelImporterFactory();

	/**
	 *
	 * @return
	 */
	IModelImporterFactory getModelImporterFactory();

	/**
	 *
	 * @return
	 */
	boolean hasGraphDbFactory();

	/**
	 *
	 * @return
	 */
	IGraphDbFactory getGraphDbFactory();

	/**
	 *
	 * @return
	 */
	ICypherStatementRegistry getCypherStatementRegistry();

	/**
	 *
	 * @return
	 */
	List<IParserFactory> getParserFactories();

	/**
	 *
	 * @return
	 */
	List<IMappingProvider> getMappingProviders();
}
