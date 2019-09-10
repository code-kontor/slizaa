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
import { Select, Spin } from 'antd';
import * as React from 'react';
import { Query } from 'react-apollo';
import {AvailableServerExtensions} from "../../../gqlqueries/__generated__/AvailableServerExtensions";
import {GQ_AVAILABLE_SERVER_EXTENSIONS} from "../../../gqlqueries/GqlQueries";
import { IServerExtension } from './ServerConfigWizard';

interface IProps {
    selectedExtensions: IServerExtension[],
    onItemsSelected: (items: IServerExtension[]) => void
}


export class Page2 extends React.Component<IProps, {}> {

    constructor(props: IProps) {
        super(props);
    }

    public render() {
        return <Query<AvailableServerExtensions, {}> query={GQ_AVAILABLE_SERVER_EXTENSIONS} >
            {
                ({ loading, error, data }) => {
                    if (loading) {
                        return <Spin size="large" />
                    }
                    const children = data && data.availableServerExtensions.map((extension: any) => (
                        <Select.Option key={this.format(extension)}>{this.format(extension)}</Select.Option>
                    ))

                    return <div>{
                        <Select
                            mode="multiple"
                            className="serverExtensionsSelect"
                            style={{ width: '100%' }}
                            placeholder="Please select"
                            onChange={this.handleChange}
                            defaultValue={this.props.selectedExtensions.map(item => item.symbolicName + "-" + item.version)}>
                            {children}
                        </Select>
                    }</div>
                }}
        </Query>
    }

    protected handleChange = (selectedItems: string[]) => {
        const serverExtensions = selectedItems.map(item => this.parse(item));
        this.props.onItemsSelected(serverExtensions);
    };

    private format(extension: IServerExtension): string {
        return extension.symbolicName + "-" + extension.version;
    }

    private parse(extensionAsString: string): IServerExtension {
        const parsed = extensionAsString.split("-");
        return {
            symbolicName: parsed[0],
            version: parsed[1]
        }
    }
}
