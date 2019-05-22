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
import { Spin } from 'antd';
import * as React from 'react';
import { Query } from 'react-apollo';
import { HasInstalledExtensions } from './__generated__/HasInstalledExtensions'
import { GQ_HAS_INSTALLED_EXTENSIONS } from './GqlQueries';
import { ServerConfigWizard } from './ServerConfigWizard';

export class ServerConfigValidator extends React.Component<{}, {}> {

    constructor(props: {}) {
        super(props)
    
        this.rerender = this.rerender.bind(this)
      }

    public rerender() {
        this.forceUpdate();
    }

    public render() {
        return <Query<HasInstalledExtensions, {}> query={GQ_HAS_INSTALLED_EXTENSIONS} >
        {({ loading, error, data }) => {
            if (loading) {  
                return <Spin size="large" /> 
            }
            if (data && data.serverConfiguration.hasInstalledExtensions) {
                return <div>{this.props.children}</div>
            } else {
                return <ServerConfigWizard rerender={this.rerender}/>
            }
        }}
    </Query>
    }
}