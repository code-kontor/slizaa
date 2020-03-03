/**
 * slizaa-server-graphql - Slizaa Static Software Analysis Tools
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
