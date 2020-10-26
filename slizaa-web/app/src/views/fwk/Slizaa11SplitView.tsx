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
import * as React from 'react';
import {HorizontalSplitLayout} from "../../components/layout";
import DatabaseAndHierarchicalGraphContext from "../../components/slizaahgchooser/DatabaseAndHierarchicalGraphContext";
import {
    AbstractSlizaaSplitView,
    ISlizaaSplitViewComponent,
    ISlizaaSplitViewProps,
    ISlizaaSplitViewState
} from "./AbstractSlizaaSplitView";

export interface ISlizaa11SplitViewProps extends ISlizaaSplitViewProps {
    top: ISlizaaSplitViewComponent,
    bottom: ISlizaaSplitViewComponent,
}

export enum Slizaa11SplitViewPositions {
    TOP, BOTTOM
}

export class Slizaa11SplitView extends AbstractSlizaaSplitView<ISlizaa11SplitViewProps, ISlizaaSplitViewState> {

    public static GUTTER_SIZE = 4

    constructor(props: ISlizaa11SplitViewProps) {
        super(props);

        this.state = {
            horizontalSplitRatio: 500,
            viewHeight: 0,
            viewWidth: 0
        }
    }

    public render() {
        let horizontalSplitRatio = this.state.horizontalSplitRatio;

         if (this.state.maximizedCardId) {
             const isTopElementSelected = this.state.maximizedCardId === Slizaa11SplitViewPositions.TOP.toString();
             horizontalSplitRatio = isTopElementSelected ? 1000 : 0;
         }

        return (
            <DatabaseAndHierarchicalGraphContext.Consumer>
                {dbAndHgContext => (
                    !dbAndHgContext.currentDatabase || !dbAndHgContext.currentHierarchicalGraph ?
                        (
                            null
                        ) :
                        (
                            <HorizontalSplitLayout
                                id={this.constructor.name}
                                height={this.state.viewHeight}
                                gutter={Slizaa11SplitView.GUTTER_SIZE}
                                initialRatio={horizontalSplitRatio}
                                onRatioChanged={this.onHorizontalRatioChanged}
                                top={this.createCard(this.props.top, Slizaa11SplitViewPositions.TOP.toString())}
                                bottom={this.createCard(this.props.bottom, Slizaa11SplitViewPositions.BOTTOM.toString())}
                            />
                        )
                )}
            </DatabaseAndHierarchicalGraphContext.Consumer>
        );
    }
}

