/**
 * slizaa-server-service-provisioning - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.provisioning.internal.tasks;

import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;
import io.codekontor.slizaa.server.slizaadb.SlizaaDatabaseState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartGraphDatabaseJobTask extends AbstractJobTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartGraphDatabaseJobTask.class);

    public StartGraphDatabaseJobTask(ISlizaaService slizaaService, ISlizaaDatabase database) {
        super(slizaaService, database);

        setDescription(String.format("StartGraphDatabaseTask [%s]", database.getIdentifier()));
    }

    @Override
    protected boolean shouldExecuteJob(SlizaaDatabaseState state) {
        return SlizaaDatabaseState.NOT_RUNNING.equals(state);
    }

    @Override
    protected void executeJob(SlizaaDatabaseState state) {
        LOGGER.info("Starting slizaa database {}.", getDatabase().getIdentifier());
        getDatabase().start();
        awaitStateEntered(getDatabase(), SlizaaDatabaseState.RUNNING);
        LOGGER.info("Successfully started slizaa database {}.", getDatabase().getIdentifier());
    }
}
