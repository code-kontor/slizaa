package io.codekontor.slizaa.server.service.provisioning.internal.tasks;

import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;
import io.codekontor.slizaa.server.slizaadb.SlizaaDatabaseState;

import java.io.IOException;

public class ParseGraphDatabaseJobTask extends AbstractJobTask {

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
            getDatabase().parse(_startDatabase);
            awaitStateEntered(getDatabase(), _startDatabase ? SlizaaDatabaseState.RUNNING : SlizaaDatabaseState.NOT_RUNNING);
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }
}
