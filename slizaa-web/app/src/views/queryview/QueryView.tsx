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
import React from "react";
import {Card} from "../../components/card";
import {VerticalSplitLayout} from "../../components/layout";
import {DependenciesViewProps} from "../dependenciesview/DependenciesViewLayoutConstants";
import {SlizaaHorizontalSplitView} from "../fwk/SlizaaHorizontalSplitView";
import {IQueryViewProps} from "./IQueryViewProps";
import {IQueryViewState} from "./IQueryViewState";

export class QueryView extends React.Component<IQueryViewProps, IQueryViewState> {

    constructor(props: IQueryViewProps) {
        super(props);
        this.state = {}
    }

    public render() {
        return (
            <SlizaaHorizontalSplitView
                id="QueryView"
                top={<VerticalSplitLayout
                    id="upper"
                    gutter={DependenciesViewProps.GUTTER_SIZE}
                    initialWidth={340}
                    left={
                        <Card
                            id="top1"
                            title="Top 1"/>
                    }
                    right={
                        <Card
                            id="top2"
                            title="Top 2"/>
                    }
                />}
                bottom={this.bottomElement()}
            />
        );
    }

    protected bottomElement(): JSX.Element {
        return (
            <Card
                id="bottom"
                title="Bottom"/>
        );
    }
}
