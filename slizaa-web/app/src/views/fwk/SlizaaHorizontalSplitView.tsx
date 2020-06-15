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
import MaximizedCardSupportContext from "../../components/card/MaximizedCardSupportContext";
import {MaximizedCardSupportProvider} from "../../components/card/MaximizedCardSupportProvider";
import {HorizontalSplitLayout} from "../../components/layout";
import DatabaseAndHierarchicalGraphContext from "../../components/slizaahgchooser/DatabaseAndHierarchicalGraphContext";
import {ISlizaaHorizontalSplitViewProps} from "./ISlizaaHorizontalSplitViewProps";
import {ISlizaaHorizontalSplitViewState} from "./ISlizaaHorizontalSplitViewState";

export class SlizaaHorizontalSplitView extends React.Component<ISlizaaHorizontalSplitViewProps, ISlizaaHorizontalSplitViewState> {

    public static GUTTER_SIZE = 4

    constructor(props: ISlizaaHorizontalSplitViewProps) {
        super(props);

        this.state = {
            horizontalSplitRatio: 500,
            viewHeight: 600
        }
    }

    public componentDidMount(): void {
        this.updateWindowDimensions();
        window.addEventListener('resize', this.updateWindowDimensions);
    }

    public componentWillUnmount(): void {
        window.removeEventListener('resize', this.updateWindowDimensions);
    }

    public render() {
        return (
            <DatabaseAndHierarchicalGraphContext.Consumer>
                {dbAndHgContext => (
                    !dbAndHgContext.currentDatabase || !dbAndHgContext.currentHierarchicalGraph ?
                        (
                            null
                        ) :
                        (
                            <MaximizedCardSupportProvider>
                                <MaximizedCardSupportContext.Consumer>
                                    {ctx => (
                                        <HorizontalSplitLayout
                                            id={this.constructor.name}
                                            height={this.state.viewHeight}
                                            gutter={SlizaaHorizontalSplitView.GUTTER_SIZE}
                                            initialRatio={this.state.horizontalSplitRatio}
                                            onRatioChanged={this.onHorizontalRatioChanged}
                                            top={this.props.top}
                                            bottom={this.props.bottom}
                                        />
                                    )}
                                </MaximizedCardSupportContext.Consumer>
                            </MaximizedCardSupportProvider>
                        )
                )}
            </DatabaseAndHierarchicalGraphContext.Consumer>
        );
    }

    private updateWindowDimensions = (): void => {
        const newHeight = window.innerHeight - 55;
        this.setState(
            {
                viewHeight: newHeight,
            });
    }

    private onHorizontalRatioChanged = (id: string, newRation: number): void => {
        this.setState(
            {
                horizontalSplitRatio: newRation,
            })
    }
}

