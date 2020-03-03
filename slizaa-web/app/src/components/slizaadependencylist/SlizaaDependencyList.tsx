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

import {Table} from "antd";
import * as React from "react";
import {ISlizaaDependencyListProps} from "./ISlizaaDependencyListProps";
import {ISlizaaDependencyListState} from "./ISlizaaDependencyListState";

export class SlizaaDependencyList extends React.Component<ISlizaaDependencyListProps, ISlizaaDependencyListState> {

    constructor(props: ISlizaaDependencyListProps) {
        super(props);

        this.state = {
            pageNumber: 1,
        };
    }

    public render() {
        const dataSource = [
            {
                index: "2"
            },
            {
                index: "4"
            },
            {
                index: "6"
            }
        ]

        const columns = [
            {
                dataIndex: 'index',
                key: 'index',
                title: 'Hurra',
            }
        ]
        return <Table size={"small"} dataSource={dataSource} columns={columns} />
    }
}