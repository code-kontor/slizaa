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
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class SlizaaDatabase_Initial_Test extends AbstractSlizaaDatabaseTest {

    @Test
    public void testInitial() {

        ISlizaaDatabase graphDatabase = graphDatabaseFactory.newInstanceFromConfiguration("test", 1234, getDatabaseRootDirectory());
        Assertions.assertInitial(graphDatabase);
    }

    @Test
    public void testRestore() throws IOException {

        ISlizaaDatabaseConfiguration databaseConfiguration = new SlizaaDatabaseConfiguration(
                "test",
                1234,
                SlizaaDatabaseState.INITIAL,
                null,
                null);

        ISlizaaDatabase graphDatabase = graphDatabaseFactory.newInstanceFromConfiguration(databaseConfiguration, getDatabaseRootDirectory());
        Assertions.assertInitial(graphDatabase);
    }
}
