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
import io.codekontor.slizaa.server.slizaadb.internal.SlizaaDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ParseGraphDatabaseJobTask extends AbstractJobTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParseGraphDatabaseJobTask.class);

    private boolean _startDatabase;

    public ParseGraphDatabaseJobTask(ISlizaaService slizaaService, ISlizaaDatabase database, boolean startDatabase) {
        super(slizaaService, database);

        _startDatabase = startDatabase;

        setDescription(String.format("ParseGraphDatabaseTask [%s, startDatabase=%s]", database.getIdentifier(), _startDatabase));
    }

    @Override
    protected boolean shouldExecuteJob(SlizaaDatabaseState state) {
        return true;
    }

    @Override
    protected void executeJob(SlizaaDatabaseState state) {
        try {
            LOGGER.info("Parsing content for slizaa database {}.", getDatabase().getIdentifier());
            getDatabase().parse(_startDatabase);
            awaitStateEntered(getDatabase(), _startDatabase ? SlizaaDatabaseState.RUNNING : SlizaaDatabaseState.NOT_RUNNING);
            LOGGER.info("Successfully parsed content for slizaa database {}.", getDatabase().getIdentifier());
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }
}
