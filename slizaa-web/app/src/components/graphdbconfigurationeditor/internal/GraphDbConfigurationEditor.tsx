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
import { Button, Icon, Tabs } from 'antd';
import { ClickParam } from 'antd/lib/menu';
import * as React from 'react';
import { Card } from '../../card';

export interface IGraphDbConfiguration {
    identifier: string
    state: string
    availableActions: string[]
    port: number
    hierarchicalGraphs: IHierarchicalGraph[]

}

export interface IHierarchicalGraph {
    identifier: string;
  }
  

export interface IGraphDbConfigurationEditorProps {
    graphdatabase: IGraphDbConfiguration
}

export class GraphDbConfigurationEditor extends React.Component<IGraphDbConfigurationEditorProps, {}> {

    public render() {

        // const menuItems = this.props.graphdatabase.availableActions.map(action => <Menu.Item key="action">{action}</Menu.Item>);
        // const menu = <Menu onClick={this.onClick}>
        //        {menuItems}
        //    </Menu>;

        const hierarchicalGraphs = this.props.graphdatabase.hierarchicalGraphs.map(hg => <Tabs.TabPane tab={hg.identifier} key={hg.identifier}>{hg.identifier}</Tabs.TabPane>);

        return (
            <Card title="Internal GraphDB" allowOverflow={false} >
                <div>
                    <div>{this.props.graphdatabase.identifier}</div>
                    <div>{this.props.graphdatabase.state}</div>
                    <div>{this.props.graphdatabase.port}</div>
                        <Button >
                            {this.props.graphdatabase.state} <Icon type="down" />
                        </Button>
                    
                    <Tabs defaultActiveKey="1" >
                        {hierarchicalGraphs}
                    </Tabs>
                </div>
            </Card>
        );
    }

    protected onClick = (clickParam: ClickParam) => {
        // tslint:disable-next-line:no-console
        console.log(clickParam.key);
    }
}