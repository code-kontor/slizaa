/**
 * slizaa-server-service-slizaa - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.slizaa.internal;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Import(SlizaaServiceTestConfiguration.class)
public abstract class AbstractSlizaaServiceTest {
  
  @Autowired
  public SlizaaServiceImpl _slizaaService;
  
	@ClassRule
	public static TemporaryFolder folder = new TemporaryFolder();
	
	private static File configurationRootDirectory;
	
	private static File databaseRootDirectory;

  @BeforeClass
	public static void beforeClass() throws Exception {
		
		configurationRootDirectory = folder.newFolder();
		databaseRootDirectory = folder.newFolder();
		
		System.setProperty("configuration.rootDirectory", configurationRootDirectory.getAbsolutePath());
		System.setProperty("database.rootDirectory", databaseRootDirectory.getAbsolutePath());
	}

	public SlizaaServiceImpl slizaaService() {
		return _slizaaService;
	}

  public static File getConfigurationRootDirectory() {
    return configurationRootDirectory;
  }

  public static File getDatabaseRootDirectory() {
    return databaseRootDirectory;
  }	
}
