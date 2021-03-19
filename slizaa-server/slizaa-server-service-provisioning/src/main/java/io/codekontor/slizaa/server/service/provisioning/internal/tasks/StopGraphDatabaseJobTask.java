package io.codekontor.slizaa.server.service.provisioning.internal.tasks;

import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;
import io.codekontor.slizaa.server.slizaadb.SlizaaDatabaseState;

public class StopGraphDatabaseJobTask extends AbstractJobTask {

    public StopGraphDatabaseJobTask(ISlizaaService slizaaService, ISlizaaDatabase database) {
        super(slizaaService, database);

        setDescription(String.format("StopGraphDatabaseTask [%s]", database.getIdentifier()));
    }

    @Override
    protected boolean shouldExecuteJob(SlizaaDatabaseState state) {
        return SlizaaDatabaseState.RUNNING.equals(state);
    }

    @Override
    protected void executeJob(SlizaaDatabaseState state) {
        getDatabase().stop();
        awaitStateEntered(getDatabase(), SlizaaDatabaseState.NOT_RUNNING);
    }
}
