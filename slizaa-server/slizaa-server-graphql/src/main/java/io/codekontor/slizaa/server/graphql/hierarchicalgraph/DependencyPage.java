package io.codekontor.slizaa.server.graphql.hierarchicalgraph;

import  java.util.List;


public class DependencyPage {

    private PageInfo pageInfo;

    private List<Dependency> dependencies;

    public DependencyPage(PageInfo pageInfo, List<Dependency> dependencies) {
        this.pageInfo = pageInfo;
        this.dependencies = dependencies;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public List<Dependency> getDependencies() {
        return dependencies;
    }
}
