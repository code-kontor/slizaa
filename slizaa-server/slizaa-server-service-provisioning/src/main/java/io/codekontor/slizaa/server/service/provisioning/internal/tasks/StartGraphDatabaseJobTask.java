package io.codekontor.slizaa.server.service.provisioning.internal.tasks;

import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;
import io.codekontor.slizaa.server.slizaadb.SlizaaDatabaseState;

public class StartGraphDatabaseJobTask extends AbstractJobTask {

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
        getDatabase().start();
        awaitStateEntered(getDatabase(), SlizaaDatabaseState.RUNNING);
    }
}
