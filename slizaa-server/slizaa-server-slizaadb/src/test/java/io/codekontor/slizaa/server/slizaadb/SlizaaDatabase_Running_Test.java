/**
 * slizaa-server-slizaadb - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.slizaadb;

import io.codekontor.slizaa.server.slizaadb.internal.Assertions;
import org.awaitility.Awaitility;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

public class SlizaaDatabase_Running_Test extends AbstractSlizaaDatabaseTest {

    @Test
    public void testStart() throws IOException, TimeoutException {

        ISlizaaDatabase graphDatabase = graphDatabaseFactory.newInstance("test", 1234, getDatabaseRootDirectory());
        assertThat(graphDatabase).isNotNull();

        graphDatabase.setContentDefinitionProvider("mvn", "org.springframework:spring-core:jar:5.1.6.RELEASE,org.springframework:spring-beans:jar:5.1.6.RELEASE");
        graphDatabase.parse(false);

        Assertions.assertParsing(graphDatabase);
        Awaitility.await().until(() -> SlizaaDatabaseState.NOT_RUNNING.equals(graphDatabase.getState()));
        Assertions.assertNotRunning(graphDatabase);

        graphDatabase.start();

        Assertions.assertStarting(graphDatabase);
        graphDatabase.awaitState(SlizaaDatabaseState.RUNNING, TIMEOUT);
        Assertions.assertRunning(graphDatabase);
    }

    @Test
    public void parseWithStart() throws IOException, TimeoutException {

        ISlizaaDatabase graphDatabase = graphDatabaseFactory.newInstance("test", 1234, getDatabaseRootDirectory());
        assertThat(graphDatabase).isNotNull();

        graphDatabase.setContentDefinitionProvider("mvn", "org.springframework:spring-core:jar:5.1.6.RELEASE,org.springframework:spring-beans:jar:5.1.6.RELEASE");
        graphDatabase.parse(true);

        Assertions.assertParsing(graphDatabase);

        graphDatabase.awaitState(SlizaaDatabaseState.RUNNING, 5000);
        Assertions.assertRunning(graphDatabase);
    }

    @Test
    public void testRestore() throws IOException, TimeoutException {

        ISlizaaDatabaseConfiguration databaseConfiguration = new SlizaaDatabaseConfiguration(
                "test",
                1234,
                SlizaaDatabaseState.RUNNING,
                "mvn",
                "org.springframework:spring-core:jar:5.1.6.RELEASE,org.springframework:spring-beans:jar:5.1.6.RELEASE");

        ISlizaaDatabase graphDatabase = graphDatabaseFactory.newInstance(databaseConfiguration, getDatabaseRootDirectory());

        Assertions.assertStarting(graphDatabase);

        graphDatabase.awaitState(SlizaaDatabaseState.RUNNING, 5000);
        Assertions.assertRunning(graphDatabase);
    }
}
