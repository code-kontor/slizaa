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
import { Icon, Layout, Menu } from 'antd';
import * as React from 'react';
import { connect } from 'react-redux';
import { BrowserRouter, Link, Route } from 'react-router-dom';
import { Dispatch } from 'redux';
import { SlizaaHgChooser } from 'src/components/slizaahgchooser';
import { actionSelectDatabase, actionSelectHierarchicalGraph } from 'src/redux/Actions';
import { IAppState } from 'src/redux/IAppState';
import ViewDsm from 'src/views/dependenciesview/internal/DependenciesView';

import { SelectParam } from 'antd/lib/menu';
import './MainView.css';
import { DependencyVisualisation, SlizaaSvg } from './SlizaaIcons';

interface IProps {
  currentDatabase: string
  currentHierarchicalGraph: string
  dispatchSelectDatabase: (selectedDatabaseId: string) => void
  dispatchSelectHierarchicalGraph: (selectHierarchicalGraphId: string) => void
}
interface IState {
  collapsed: boolean
  selectedKeys?: string[]
}
export class MainView extends React.Component<IProps, IState> {

  constructor(props: IProps) {
    super(props);

    this.state = {
      collapsed: true
    }

    this.onCollapse = this.onCollapse.bind(this);
  }

  public render() {

    return (
      <BrowserRouter basename="/slizaa">
        <Layout
          style={{ minHeight: '100vh' }}>
          <Layout.Header style={{ padding: 0 }}>
            <Link to="/">
              <svg height="40px" viewBox="0 0 493.923 175.948" style={{ paddingLeft: "24px", verticalAlign: "middle" }}>
                <SlizaaSvg />
              </svg>
            </Link>
            <Icon
              className="trigger"
              type={this.state.collapsed ? 'menu-unfold' : 'menu-fold'}
              onClick={this.toggleCollapsed}
            />
            <SlizaaHgChooser
              currentDatabase={this.props.currentDatabase}
              currentHierarchicalGraph={this.props.currentHierarchicalGraph}
              onDatabaseSelect={this.props.dispatchSelectDatabase}
              onHierarchicalGraphSelect={this.props.dispatchSelectHierarchicalGraph}
            />
          </Layout.Header>
          <Layout>
            <Layout.Sider
              theme="dark"
              collapsible={true}
              collapsed={this.state.collapsed}
              onCollapse={this.onCollapse}
              trigger={null}
              collapsedWidth={0}
              width={170}
            >
              <Menu defaultSelectedKeys={this.state.selectedKeys ? this.state.selectedKeys : ['1']}
                theme="dark"
                mode="inline"
                onSelect={this.onSelect}>
                <Menu.Item key="1">
                  <Icon component={DependencyVisualisation} />
                  <span>Dependencies</span>
                  <Link to="/" />
                </Menu.Item>
              </Menu>
            </Layout.Sider>
            <Layout.Content style={{ padding: 8, minHeight: 280 }}>
              <Route exact={true} path="/" component={ViewDsm} />
            </Layout.Content>
          </Layout>
        </Layout>
      </BrowserRouter>

    )
  }

  protected onSelect = (p: SelectParam) => {
    // tslint:disable-next-line:no-console
    console.log(p);
    this.setState({ ...this.state, selectedKeys: p.selectedKeys });
  }

  protected onCollapse(isCollapsed: boolean) {
    this.setState({ ...this.state, collapsed: isCollapsed });
  }

  protected toggleCollapsed = () => {
    this.setState({
      collapsed: !this.state.collapsed,
    });
  }
}

const mapStateToProps = (state: IAppState) => {
  return {
    currentDatabase: state.currentDatabase,
    currentHierarchicalGraph: state.currentHierarchicalGraph,
  }
}

const mapDispatchToProps = (dispatch: Dispatch) => { // tslint:disable-line
  return {
    dispatchSelectDatabase: (selectedDatabaseId: string) => {
      dispatch(actionSelectDatabase(selectedDatabaseId));
    },
    dispatchSelectHierarchicalGraph: (selectedDatabaseId: string) => {
      dispatch(actionSelectHierarchicalGraph(selectedDatabaseId));
    }
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(MainView);