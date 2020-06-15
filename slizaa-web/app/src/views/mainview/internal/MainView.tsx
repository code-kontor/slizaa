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
import {Icon, Layout, Menu} from 'antd';
import * as React from 'react';
import {BrowserRouter, Link, Route, withRouter} from 'react-router-dom';
import {SlizaaHgChooser} from 'src/components/slizaahgchooser';
import DatabaseAndHierarchicalGraphContext
    from "../../../components/slizaahgchooser/DatabaseAndHierarchicalGraphContext";
import {DependenciesView} from "../../dependenciesview";
import {QueryView} from "../../queryview/QueryView";
import './MainView.css';
import {DependencyVisualisation, DependencyVisualisationIcon, SlizaaSvg} from './SlizaaIcons';

interface IState {
    collapsed: boolean
}

export class MainView extends React.Component<any, IState> {

    constructor(props: any) {
        super(props);

        this.state = {
            collapsed: true
        }
    }

    public render() {
        return (
            <BrowserRouter basename="/slizaa">
                <Layout style={{minHeight: '100vh'}}>
                    <Layout.Header style={{padding: 0}}>
                        <Icon
                            className="trigger"
                            type={this.state.collapsed ? 'menu-unfold' : 'menu-fold'}
                            onClick={this.toggleCollapsed}
                        />
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
                            collapsible={true}
                            collapsed={this.state.collapsed}
                            onCollapse={this.onCollapse}
                            trigger={null}
                            collapsedWidth={0}
                            width={170}
                        >
                            <Linkmenu/>
                        </Layout.Sider>
                        <Layout.Content style={{padding: 4, minHeight: 280}}>
                            <Route key="/query" exact={true} path="/query" component={QueryViewComponent}/>
                            <Route key="/dependencies" exact={true} path="/dependencies"
                                   component={DependencyViewComponent}/>
                        </Layout.Content>
                    </Layout>
                </Layout>
            </BrowserRouter>
        )
    }

    protected onCollapse = (isCollapsed: boolean) => {
        this.setState({...this.state, collapsed: isCollapsed});
    }

    protected toggleCollapsed = () => {
        this.setState({
            collapsed: !this.state.collapsed,
        });
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

function QueryViewComponent() {
    return <DatabaseAndHierarchicalGraphContext.Consumer>
        {ctx => (
            <QueryView/>
        )}
    </DatabaseAndHierarchicalGraphContext.Consumer>
}

const Linkmenu = withRouter(props => {
    const {location} = props;
    return (
        <Menu mode="inline" selectedKeys={[location.pathname]} theme="light">
            <Menu.Item key="/dependencies">
                <DependencyVisualisationIcon />
                <Link to="/dependencies">Dependencies</Link>
            </Menu.Item>
            <Menu.Item key="/query">
                <Icon component={DependencyVisualisation}/>
                <Link to="/query">Query</Link>
            </Menu.Item>
        </Menu>
    );
});
