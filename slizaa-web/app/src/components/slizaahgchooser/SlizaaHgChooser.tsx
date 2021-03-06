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
import {Button, Dropdown, Icon, Menu} from 'antd';
import {ClickParam} from 'antd/lib/menu';
import * as React from 'react';
import {Query} from 'react-apollo';

import {GraphDatabasesWithHierarchicalGraphs} from "../../gqlqueries/__generated__/GraphDatabasesWithHierarchicalGraphs";
import {GQ_GRAPH_DATABASES_WITH_HIERARCHICAL_GRAPHS} from "../../gqlqueries/GqlQueries";
import DatabaseAndHierarchicalGraphContext, {IDatabaseAndHierarchicalGraphContext} from "./DatabaseAndHierarchicalGraphContext";
import './SlizaaHgChooser.css';

export class SlizaaHgChooser extends React.Component<{}, {}> {

    public render() {
        return <DatabaseAndHierarchicalGraphContext.Consumer>
            {ctx => (

                <Query<GraphDatabasesWithHierarchicalGraphs, {}> query={GQ_GRAPH_DATABASES_WITH_HIERARCHICAL_GRAPHS}>
                    {({loading, error, data}) => {

                        if (data && data.graphDatabases) {

                            const databasesMenuItems = data.graphDatabases.map(database => <Menu.Item
                                key={database.identifier}>{database.identifier}</Menu.Item>);
                            const databasesMenu = <Menu onClick={this.handleDatabaseSelection(ctx)}>
                                {databasesMenuItems}
                            </Menu>;

                            const hierarchicalGraphMenuItems = data.graphDatabases
                                .filter(database => ctx.currentDatabase && database.identifier === ctx.currentDatabase)
                                .map(database => database.hierarchicalGraphs.map(item => <Menu.Item
                                    key={item.identifier}>{item.identifier}</Menu.Item>));
                            const hierarchicalGraphsMenu = <Menu onClick={this.handleHierarchicalGraphSelection(ctx)}>
                                {hierarchicalGraphMenuItems}
                            </Menu>;

                            return <div className="slizaaHgChooser" style={{display: "inline-block", float: "right"}}>
                                <Dropdown className="slizaaHgChooser-dropdown slizaaHgChooser-dropdown-database"
                                          overlay={databasesMenu} placement="bottomLeft">
                                    <Button className="slizaaHgChooser-button slizaaHgChooser-button-database">
                                        <div className="slizaaHgChooser-button-key"
                                             style={{display: "inline-block"}}>Database:
                                        </div>
                                        <div className="slizaaHgChooser-button-value" style={{display: "inline-block"}}>
                                            {ctx.currentDatabase ? ctx.currentDatabase :
                                                <Icon type="disconnect"/>}
                                        </div>
                                    </Button>
                                </Dropdown>

                                <Dropdown
                                    className="slizaaHgChooser-dropdown slizaaHgChooser-dropdown-hierarchicalgraph"
                                    overlay={hierarchicalGraphsMenu} placement="bottomLeft">
                                    <Button className="slizaaHgChooser-button slizaaHgChooser-button-hierarchicalgraph">
                                        <div className="slizaaHgChooser-button-key"
                                             style={{display: "inline-block"}}>Hierarchical Graph:
                                        </div>
                                        <div className="slizaaHgChooser-button-value" style={{display: "inline-block"}}>
                                            {ctx.currentHierarchicalGraph ? ctx.currentHierarchicalGraph :
                                                <Icon type="disconnect"/>}
                                        </div>
                                    </Button>
                                </Dropdown>
                            </div>
                        } else {
                            // TODO
                            return <div>error</div>
                        }
                    }}
                </Query>)}
        </DatabaseAndHierarchicalGraphContext.Consumer>
    }

    private handleDatabaseSelection = (ctx: IDatabaseAndHierarchicalGraphContext): ((param: ClickParam) => void) => {
        return (param: ClickParam): void => {
            if (ctx.setCurrentDatabase) {
                ctx.setCurrentDatabase(param.key);
            }
        }
    }

    private handleHierarchicalGraphSelection = (ctx: IDatabaseAndHierarchicalGraphContext): ((param: ClickParam) => void) => {
        return (param: ClickParam): void => {
            if (ctx.setCurrentHierarchicalGraph) {
                ctx.setCurrentHierarchicalGraph(param.key);
            }
        }
    }
}
