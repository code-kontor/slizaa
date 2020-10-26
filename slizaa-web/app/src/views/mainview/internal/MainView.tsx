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
import {Layout, Tooltip} from 'antd';
import * as React from 'react';
import {BrowserRouter, Link, Route, withRouter} from 'react-router-dom';
import {SlizaaHgChooser} from 'src/components/slizaahgchooser';
import DatabaseAndHierarchicalGraphContext
    from "../../../components/slizaahgchooser/DatabaseAndHierarchicalGraphContext";
import {DependenciesView} from "../../dependenciesview";
import {XrefView} from "../../xrefview/XrefView";
import './MainView.css';
import {CrossReferencerIcon, DependencyVisualisationIcon, SlizaaSvg} from './SlizaaIcons';

export class MainView extends React.Component<any, any> {

    public static readonly SIDER_WIDTH = 40;

    constructor(props: any) {
        super(props);
    }

    public render() {

        return (
            <BrowserRouter basename="/slizaa">
                <Layout style={{minHeight: '100vh'}}>
                    <Layout.Header style={{padding: "0px 15px"}}>
                        <Link to="/">
                            <svg height="30px" viewBox="0 0 493.923 175.948" style={{verticalAlign: "middle"}}>
                                <SlizaaSvg/>
                            </svg>
                        </Link>
                        <SlizaaHgChooser/>
                    </Layout.Header>
                    <Layout>
                        <Layout.Sider
                            theme="light"
                            collapsible={false}
                            collapsed={true}
                            trigger={null}
                            collapsedWidth={MainView.SIDER_WIDTH}
                            width={MainView.SIDER_WIDTH}
                        >
                            <Linkmenu/>
                        </Layout.Sider>

                        <Layout.Content style={{padding: 4, minHeight: 280}}>
                            <Route key="/xref" exact={true} path="/xref" component={XrefViewComponent}/>
                            <Route key="/dependencies" exact={true} path="/dependencies"
                                   component={DependencyViewComponent}/>
                        </Layout.Content>
                    </Layout>
                </Layout>
            </BrowserRouter>
        )
    }
}

function DependencyViewComponent() {
    return <DatabaseAndHierarchicalGraphContext.Consumer>
        {ctx => (
            <DependenciesView
                key={ctx.currentDatabase + "-" + ctx.currentHierarchicalGraph}
                databaseId={ctx.currentDatabase}
                hierarchicalGraphId={ctx.currentHierarchicalGraph}/>
        )}
    </DatabaseAndHierarchicalGraphContext.Consumer>
}

function XrefViewComponent() {
    return <DatabaseAndHierarchicalGraphContext.Consumer>
        {ctx => (
            <XrefView
                key={ctx.currentDatabase + "-" + ctx.currentHierarchicalGraph}
                databaseId={ctx.currentDatabase}
                hierarchicalGraphId={ctx.currentHierarchicalGraph}/>
        )}
    </DatabaseAndHierarchicalGraphContext.Consumer>
}

const Linkmenu = withRouter(props => {
    return (
        <div className="ant-layout-sider-children">
            <ul className="ant-menu ant-menu-vertical" role="menu">
                <Tooltip title={"Dependencies"} placement={"right"}>
                    <li className="ant-menu-item" role="menuitem"><i className="anticon">
                        <DependencyVisualisationIcon/>
                    </i>
                        <Link to="/dependencies"/>
                    </li>
                </Tooltip>
                <Tooltip title={"Cross-Referencer"} placement={"right"}>
                    <li className="ant-menu-item" role="menuitem">
                        <i className="anticon">
                            <CrossReferencerIcon/>
                        </i>
                        <Link to="/xref"/>
                    </li>
                </Tooltip>
            </ul>
        </div>
    );
});
