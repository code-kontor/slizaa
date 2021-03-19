package io.codekontor.slizaa.server.service.provisioning.internal.tasks;

import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;
import io.codekontor.slizaa.server.slizaadb.SlizaaDatabaseState;

public class CreateHierarchicalGraphJobTask extends AbstractJobTask {

    private String _hierarchicalGraphId;

    public CreateHierarchicalGraphJobTask(ISlizaaService slizaaService, ISlizaaDatabase database, String hierarchicalGraphId) {
        super(slizaaService, database);

        this._hierarchicalGraphId = hierarchicalGraphId;

        setDescription(String.format("CreateHierarchicalGraphTask [%s, %s]", database.getIdentifier(), hierarchicalGraphId));
    }

    @Override
    protected boolean shouldExecuteJob(SlizaaDatabaseState state) {
        return SlizaaDatabaseState.RUNNING.equals(state) &&
                getDatabase().getHierarchicalGraph(this._hierarchicalGraphId) == null;
    }

    @Override
    protected void executeJob(SlizaaDatabaseState state) {
        getDatabase().newHierarchicalGraph(this._hierarchicalGraphId);
        awaitStateEntered(getDatabase(), SlizaaDatabaseState.RUNNING);
    }
}
