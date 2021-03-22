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

import io.codekontor.slizaa.core.job.IJobTask;
import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;
import io.codekontor.slizaa.server.slizaadb.SlizaaDatabaseState;

import java.util.function.BooleanSupplier;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public abstract class AbstractJobTask implements IJobTask {

    private static final long POLL_INTERVAL = 500;

    private static final long TIMEOUT = 600000;

    private String _description;

    private ISlizaaService _slizaaService;

    private ISlizaaDatabase _slizaaDatabase;

    public AbstractJobTask(ISlizaaService slizaaService, ISlizaaDatabase database) {
        this._slizaaService = checkNotNull(slizaaService);
        this._slizaaDatabase = checkNotNull(database);
    }

    @Override
    public String getDescription() {
        return _description != null ? _description : this.getClass().getSimpleName();
    }

    public void setDescription(String description) {
        checkState(description == null || !description.isEmpty());
        this._description = description;
    }

    protected abstract boolean shouldExecuteJob(final SlizaaDatabaseState state);


    protected abstract void executeJob(final SlizaaDatabaseState state);

    @Override
    public Boolean call() throws Exception {
        try {
            awaitNonTransientState();
            SlizaaDatabaseState hostState = getCurrentState();
            if (shouldExecuteJob(hostState)) {
                executeJob(hostState);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    protected final ISlizaaService getSlizaaService() {
        return _slizaaService;
    }

    public final String getDatabaseId() {
        return _slizaaDatabase.getIdentifier();
    }

    public final ISlizaaDatabase getDatabase() {
        return _slizaaDatabase;
    }

    protected final SlizaaDatabaseState getCurrentState() {
        return _slizaaDatabase.getState();
    }

    protected final void awaitStateEntered(final ISlizaaDatabase slizaaDatabase, final SlizaaDatabaseState state) {
        checkNotNull(slizaaDatabase);
        checkNotNull(state);
        waitUntil(() -> state.equals(slizaaDatabase.getState()), TIMEOUT);
    }

    protected final void awaitStateLeft(final ISlizaaDatabase slizaaDatabase, final SlizaaDatabaseState state) {
        checkNotNull(slizaaDatabase);
        checkNotNull(state);
        waitUntil(() -> !state.equals(_slizaaDatabase.getState()), TIMEOUT);
    }

    protected final boolean isInTransientState() {
        SlizaaDatabaseState databaseState = _slizaaDatabase.getState();
        return SlizaaDatabaseState.STARTING.equals(databaseState)
                || SlizaaDatabaseState.STOPPING.equals(databaseState)
                || SlizaaDatabaseState.CREATING_HIERARCHICAL_GRAPH.equals(databaseState)
                || SlizaaDatabaseState.PARSING.equals(databaseState);
    }

    protected final void awaitNonTransientState() {
        waitUntil(() -> !isInTransientState(), TIMEOUT);
    }

    protected static void waitUntil(BooleanSupplier condition, long timeout) {
        long start = System.currentTimeMillis();
        while (!condition.getAsBoolean()) {
            try {
                Thread.sleep(POLL_INTERVAL);
            } catch (InterruptedException exception) {
                //
            }
            if (System.currentTimeMillis() - start > timeout) {
                throw new RuntimeException(String.format("Condition not meet within %s ms", timeout));
            }
        }
    }
}
