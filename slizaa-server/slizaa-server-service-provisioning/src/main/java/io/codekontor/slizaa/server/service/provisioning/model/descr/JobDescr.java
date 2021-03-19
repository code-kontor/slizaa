package io.codekontor.slizaa.server.service.provisioning.model.descr;

public class JobDescr {

    private String description;
    private String state;

    public JobDescr(String description, String state) {
        this.description = description;
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public String getState() {
        return state;
    }
}
