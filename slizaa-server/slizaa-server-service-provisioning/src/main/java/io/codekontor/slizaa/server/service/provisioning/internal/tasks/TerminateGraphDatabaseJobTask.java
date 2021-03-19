package io.codekontor.slizaa.server.service.provisioning.internal.tasks;

import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;
import io.codekontor.slizaa.server.slizaadb.SlizaaDatabaseState;

public class TerminateGraphDatabaseJobTask extends AbstractJobTask {

    public TerminateGraphDatabaseJobTask(ISlizaaService slizaaService, ISlizaaDatabase database) {
        super(slizaaService, database);

        setDescription(String.format("TerminateGraphDatabaseTask [%s]", database.getIdentifier()));
    }

    @Override
    protected boolean shouldExecuteJob(SlizaaDatabaseState state) {
        return true;
    }

    @Override
    protected void executeJob(SlizaaDatabaseState state) {
        getDatabase().terminate();
        awaitStateEntered(getDatabase(), SlizaaDatabaseState.TERMINATED);
    }
}
