/**
 * slizaa-server-service-configuration - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.configuration.impl;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.codekontor.slizaa.server.service.configuration.IConfigurationService;

@Component
public class ConfigurationServiceImpl implements IConfigurationService {

  private static final Logger                       LOGGER              = LoggerFactory.getLogger(ConfigurationServiceImpl.class);
  
	@Autowired
	private ConfigurationServiceProperties _configProps;

	@PostConstruct
	public void init() {
	  LOGGER.info("Slizaa configuration service: " + _configProps.getRootDirectory());
	}

	@Override
	public <T> void store(String configurationIdentifier, T configuration) throws IOException {

	  //
	  store(configurationIdentifier, "configuration.json", configuration);
	}

	@Override
	public <T> T load(String configurationIdentifier, Class<T> type) throws IOException {

		//
	  return load(configurationIdentifier, "configuration.json", type);
	}

  @Override
  public <T> void store(String configurationIdentifier, String fileName, T configuration) throws IOException {
    
    //
    File file = new File(_configProps.getRootDirectoryAsFile(), configurationIdentifier + File.separatorChar + fileName);
    file.getParentFile().mkdirs();

    //
    ObjectMapper objectMapper = new ObjectMapper();
    // TODO: Replace with @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
    objectMapper.enableDefaultTyping();
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    objectMapper.writeValue(file, configuration);
    
  }

  @Override
  public <T> T load(String configurationIdentifier, String fileName, Class<T> type) throws IOException {
    
    //
    File file = new File(_configProps.getRootDirectory(), configurationIdentifier  + File.separatorChar + fileName);
    
    //
    if (file.exists()) {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.enableDefaultTyping();
      return objectMapper.readValue(file, type);      
    }
    
    //
    return null;
  }
}
