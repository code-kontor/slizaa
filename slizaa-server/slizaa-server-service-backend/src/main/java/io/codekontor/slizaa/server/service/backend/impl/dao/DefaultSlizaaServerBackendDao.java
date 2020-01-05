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
package io.codekontor.slizaa.server.service.backend.impl.dao;

import io.codekontor.slizaa.server.service.configuration.IConfigurationService;
import io.codekontor.slizaa.server.service.backend.extensions.IExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class DefaultSlizaaServerBackendDao implements ISlizaaServerBackendDao {

	@Autowired
	private IConfigurationService _configurationService;

	/**
	 * @return
	 */
	@Override
	public List<IExtension> getInstalledExtensions() {
		
		try {
			
			//
			ExtensionsHolder config = _configurationService.load("io.codekontor.slizaa.server.service.backend", ExtensionsHolder.class);
			
			//
			if (config != null) {
				return config._extensions;
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
		//
		return Collections.emptyList();
	}

	/**
	 * @param extensions
	 */
	@Override
	public void saveInstalledExtensions(List<IExtension> extensions) {

		try {
			ExtensionsHolder extensionsHolder = new ExtensionsHolder();
			extensionsHolder._extensions = extensions;
			_configurationService.store("io.codekontor.slizaa.server.service.backend", extensionsHolder);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}
}
