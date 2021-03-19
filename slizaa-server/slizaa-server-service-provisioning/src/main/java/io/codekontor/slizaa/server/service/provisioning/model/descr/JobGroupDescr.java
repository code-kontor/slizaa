package io.codekontor.slizaa.server.service.provisioning.model.descr;

import java.util.ArrayList;
import java.util.List;

public class JobGroupDescr {

    private String id;
    private List<JobDescr> jobDescriptions;

    public JobGroupDescr(String id) {
        this.id = id;
        this.jobDescriptions = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public List<JobDescr> getJobDescriptions() {
        return jobDescriptions;
    }

    public void add(JobDescr jobDescr) {
        this.jobDescriptions.add(jobDescr);
    }
}
