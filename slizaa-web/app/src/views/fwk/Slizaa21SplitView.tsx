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
import {HorizontalSplitLayout, VerticalSplitLayout} from "../../components/layout";
import DatabaseAndHierarchicalGraphContext from "../../components/slizaahgchooser/DatabaseAndHierarchicalGraphContext";
import {DependenciesViewProps} from "../dependenciesview/DependenciesViewLayoutConstants";
import {
    AbstractSlizaaSplitView,
    ISlizaaSplitViewComponent,
    ISlizaaSplitViewProps,
    ISlizaaSplitViewState
} from "./AbstractSlizaaSplitView";

export interface ISlizaa21SplitViewState extends ISlizaaSplitViewState {
    upperVerticalSplitPosition: number
}

export interface ISlizaa21SplitViewProps extends ISlizaaSplitViewProps {
    topLeft: ISlizaaSplitViewComponent,
    topRight: ISlizaaSplitViewComponent,
    bottom: ISlizaaSplitViewComponent,
}

export enum Slizaa21SplitViewPositions {
    TOP_LEFT, TOP_RIGHT, BOTTOM
}

export class Slizaa21SplitView extends AbstractSlizaaSplitView<ISlizaa21SplitViewProps, ISlizaa21SplitViewState> {

    public static GUTTER_SIZE = 4

    constructor(props: ISlizaa21SplitViewProps) {
        super(props);

        this.state = {
            horizontalSplitRatio: 500,
            upperVerticalSplitPosition: 500,
            viewHeight: 0,
            viewWidth: 0
        }
    }

    public render() {
        let horizontalSplitRatio = this.state.horizontalSplitRatio;
        let upperVerticalSplitPosition = this.state.upperVerticalSplitPosition;

        if (this.state.maximizedCardId) {
            const isTopElementSelected = this.state.maximizedCardId === Slizaa21SplitViewPositions.TOP_RIGHT.toString() ||  this.state.maximizedCardId === Slizaa21SplitViewPositions.TOP_LEFT.toString();
            horizontalSplitRatio = isTopElementSelected ? 1000 : 0;
            if (isTopElementSelected) {
                upperVerticalSplitPosition = this.state.maximizedCardId === Slizaa21SplitViewPositions.TOP_RIGHT.toString() ? 0 : this.state.viewWidth;
            }
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
                                gutter={Slizaa21SplitView.GUTTER_SIZE}
                                initialRatio={horizontalSplitRatio}
                                onRatioChanged={this.onHorizontalRatioChanged}
                                top={
                                    <VerticalSplitLayout
                                        id="upper"
                                        gutter={DependenciesViewProps.GUTTER_SIZE}
                                        initialWidth={upperVerticalSplitPosition}
                                        onWidthChanged={this.onUpperSplitLayoutWidthChanged}
                                        left={this.createCard(this.props.topLeft, Slizaa21SplitViewPositions.TOP_LEFT.toString())}
                                        right={this.createCard(this.props.topRight, Slizaa21SplitViewPositions.TOP_RIGHT.toString())}
                                    />
                                }
                                bottom={this.createCard(this.props.bottom, Slizaa21SplitViewPositions.BOTTOM.toString())}
                            />
                        )
                )}
            </DatabaseAndHierarchicalGraphContext.Consumer>
        );
    }

    private onUpperSplitLayoutWidthChanged = (id: string, newWidth: number): void => {
        this.setState({upperVerticalSplitPosition: newWidth});
    }
}

