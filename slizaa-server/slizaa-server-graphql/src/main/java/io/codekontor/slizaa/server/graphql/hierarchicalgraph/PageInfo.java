package io.codekontor.slizaa.server.graphql.hierarchicalgraph;

public class PageInfo {

    private int pageNumber;

    private int maxPages;

    private int pageSize;

    private int totalCount;

    public PageInfo(int pageNumber, int maxPages, int pageSize, int totalCount) {
        this.pageNumber = pageNumber;
        this.maxPages = maxPages;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getMaxPages() {
        return maxPages;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
