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
import { Button, Icon, Spin } from 'antd';
import * as React from 'react';
import { Query } from 'react-apollo';
import { GraphDbConfigurationEditor } from 'src/components/graphdbconfigurationeditor';
import { AllGraphDatabases } from './__generated__/AllGraphDatabases';
import { GQ_ALL_GRAPH_DATABASES } from './GqlQueries';

export class SettingsView extends React.Component<{}, {}> {

  public render() {
    return <Query<AllGraphDatabases, {}> query={GQ_ALL_GRAPH_DATABASES} >
      {({ loading, error, data }) => {
        if (loading) {
          return <Spin size="large" />
        }
        if (data && data.graphDatabases) {

         const content = data.graphDatabases.map(db => <GraphDbConfigurationEditor key={db.identifier} graphdatabase={db} />);

          return <div>
            <Button type="dashed"><Icon type="plus" /> Add internal GraphDB</Button>
            <Button type="dashed"><Icon type="plus" />Add external GraphDB</Button>
            {content}
            </div>
        }
        return <h1>HAE?</h1>
      }}
    </Query>
  }
}