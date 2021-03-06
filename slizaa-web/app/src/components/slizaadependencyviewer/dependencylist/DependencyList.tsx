/*
 * slizaa-web - Slizaa Static Software Analysis Tools
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

import {Pagination, Table} from 'antd';
import * as React from 'react';
import {Query} from "react-apollo";
import {
    CoreDependenciesForAggregatedDependencies,
    CoreDependenciesForAggregatedDependencies_hierarchicalGraph_dependencySetForAggregatedDependency_filteredDependencies_dependencyPage_dependencies as DependencyRecord,
    CoreDependenciesForAggregatedDependenciesVariables
} from "../../../gqlqueries/__generated__/CoreDependenciesForAggregatedDependencies";
import {
    ResolvedProxyDependency,
    ResolvedProxyDependencyVariables
} from "../../../gqlqueries/__generated__/ResolvedProxyDependency";
import {
    GQ_CORE_DEPENDENCIES_FOR_AGGREGATED_DEPENDENCY,
    GQ_RESOLVED_PROXY_DEPENDENCY
} from "../../../gqlqueries/GqlQueries";
import {NodeType} from "../../../gqlqueries/query-types";
import './DependencyList.css';
import {IDependencyListProp} from "./IDependencyListProps";
import {IDependencyListState} from "./IDependencyListState";

export class DependencyList extends React.Component<IDependencyListProp, IDependencyListState> {

    private readonly PAGE_SIZE = 25;

    private readonly COLUMNS = [
        {
            dataIndex: 'sourceNode.text',
            key: 'sourceNode.text',
            title: 'Source',
            width: 120
        },
        {
            dataIndex: 'type',
            key: 'type',
            title: 'Type',
            width: 120
        },
        {
            dataIndex: 'targetNode.text',
            key: 'targetNode.text',
            title: 'Target',
            width: 120
        },
    ];

    constructor(props: any) {
        super(props);
        this.state = {
            currentPage: 1,
            expandedRows: [],
            pageSize: this.PAGE_SIZE
        }
    }

    public render() {
        if (!this.props.databaseId || !this.props.hierarchicalGraphId) {
            return null;
        }
        const variables: CoreDependenciesForAggregatedDependenciesVariables = {
            databaseId: this.props.databaseId,
            dependencySourceNodeId: this.props.dependencySourceNodeId,
            dependencyTargetNodeId: this.props.dependencyTargetNodeId,
            hierarchicalGraphId: this.props.hierarchicalGraphId,
            nodeSelections: [{selectedNodeIds: this.props.selectedSourceNodeIds, selectedNodesType:NodeType.SOURCE}, {selectedNodeIds:this.props.selectedTargetNodeIds, selectedNodesType:NodeType.TARGET}],
            pageNumber: this.state.currentPage,
            pageSize: this.state.pageSize,
        }

        return <Query<CoreDependenciesForAggregatedDependencies, CoreDependenciesForAggregatedDependenciesVariables>
            query={GQ_CORE_DEPENDENCIES_FOR_AGGREGATED_DEPENDENCY}
            fetchPolicy={"no-cache"}
            // pollInterval={1000}
            variables={variables}>

            {({data, loading, error}) => {

                if (error) {
                    return <h1>{error.message}</h1>
                }

               const pageInfo =
                    data &&
                    data.hierarchicalGraph &&
                    data.hierarchicalGraph.dependencySetForAggregatedDependency &&
                    data.hierarchicalGraph.dependencySetForAggregatedDependency.filteredDependencies.dependencyPage &&
                    data.hierarchicalGraph.dependencySetForAggregatedDependency.filteredDependencies.dependencyPage.pageInfo ?
                        data.hierarchicalGraph.dependencySetForAggregatedDependency.filteredDependencies.dependencyPage.pageInfo :
                        {maxPages: 0, pageNumber: 1, pageSize: this.PAGE_SIZE, totalCount: 0};

                const dependencies =
                    data &&
                    data.hierarchicalGraph &&
                    data.hierarchicalGraph.dependencySetForAggregatedDependency &&
                    data.hierarchicalGraph.dependencySetForAggregatedDependency.filteredDependencies.dependencyPage &&
                    data.hierarchicalGraph.dependencySetForAggregatedDependency.filteredDependencies.dependencyPage.dependencies ?
                        data.hierarchicalGraph.dependencySetForAggregatedDependency.filteredDependencies.dependencyPage.dependencies :
                        [];

                return <div style={ { display: "flex", flexDirection: "column", height: "100%"}}>
                    <div style={ { overflow: "auto", height: "100%" }} >
                        <Table
                            className={"fixedWidthTable"}
                            loading={loading}
                            columns={this.COLUMNS}
                            dataSource={dependencies}
                            size={"small"}
                            onExpandedRowsChange={this.handleExpandedRowsChange}
                            expandedRowKeys={this.state.expandedRows}
                            expandedRowRender={this.expandedRowRender}
                            scroll={{x:false, y:false}}
                            pagination={false}
                            /*pagination={{
                                current: this.state.currentPage,
                                defaultPageSize: this.PAGE_SIZE,
                                hideOnSinglePage: false,
                                onChange: this.handlePaginationChange,
                                position: "top",
                                total: pageInfo.totalCount,
                            }}*/
                        />
                    </div>
                    <div style={ { overflow: "none", padding: "15px 0px 15px 0px" }} >
                        <Pagination
                            showQuickJumper={true}
                            showSizeChanger={true}
                            total={pageInfo.totalCount}
                            pageSizeOptions={["10","25","50","100"]}
                            showTotal={showTotal}
                            defaultPageSize={this.state.pageSize}
                            defaultCurrent={1}
                            current={this.state.currentPage}
                            hideOnSinglePage={false}
                            onChange={this.handlePaginationChange}
                            onShowSizeChange={this.handlePaginationSizeChange}
                        />
                    </div>
                </div>

                function showTotal(total: number, range: [number, number]) {
                    return `${range[0]}-${range[1]} of ${total} items`;
                }
            }}
        </Query>
    }

    private expandedRowRender = (record: DependencyRecord) => {

        if (!this.props.databaseId || !this.props.hierarchicalGraphId) {
            return null;
        }

        const variables: ResolvedProxyDependencyVariables = {
            databaseId: this.props.databaseId,
            hierarchicalGraphId: this.props.hierarchicalGraphId,
            proxyDependencyId: record.id
        }

        return <Query<ResolvedProxyDependency, ResolvedProxyDependencyVariables>
            query={GQ_RESOLVED_PROXY_DEPENDENCY}
            fetchPolicy={"no-cache"}
            variables={variables}>

            {({data, loading, error}) => {

                if (error) {
                    return <h1>{error.message}</h1>
                }

                const dependencies =
                    data &&
                    data.hierarchicalGraph &&
                    data.hierarchicalGraph.dependency &&
                    data.hierarchicalGraph.dependency.resolvedDependencies ?
                        data.hierarchicalGraph.dependency.resolvedDependencies :
                        [];

                return <Table
                    showHeader={false}
                    columns={this.COLUMNS}
                    dataSource={dependencies}
                    pagination={false}
                    size={"small"}
                    loading={loading}
                />;

            }}
        </Query>
    };

   private handlePaginationChange = (page: number) => {
        this.setState({
            currentPage: page,
            expandedRows: []
        });
    }

    private handlePaginationSizeChange = (current: number, size: number) => {
        this.setState({
            pageSize: size
        });
    }

    private handleExpandedRowsChange = (expandedRowKeys: string[]) => {
        this.setState({expandedRows: expandedRowKeys});
    }
}