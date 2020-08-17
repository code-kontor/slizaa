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
import {Card} from "../../components/card";
import {HorizontalSplitLayout, VerticalSplitLayout} from "../../components/layout";
import DatabaseAndHierarchicalGraphContext from "../../components/slizaahgchooser/DatabaseAndHierarchicalGraphContext";
import {DependenciesViewProps} from "../dependenciesview/DependenciesViewLayoutConstants";
import {ISlizaa21SplitViewComponent, ISlizaa21SplitViewProps} from "./ISlizaa21SplitViewProps";
import {ISlizaa21SplitViewState} from "./ISlizaa21SplitViewState";
import {Slizaa21SplitViewPositions} from "./Slizaa21SplitViewPositions";

export class Slizaa21SplitView extends React.Component<ISlizaa21SplitViewProps, ISlizaa21SplitViewState> {

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

    public componentDidMount(): void {
        this.updateWindowDimensions();
        window.addEventListener('resize', this.updateWindowDimensions);
    }

    public componentWillUnmount(): void {
        window.removeEventListener('resize', this.updateWindowDimensions);
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
                                        left={this.createCard(this.props.topLeft, Slizaa21SplitViewPositions.TOP_LEFT)}
                                        right={this.createCard(this.props.topRight, Slizaa21SplitViewPositions.TOP_RIGHT)}
                                    />
                                }
                                bottom={this.createCard(this.props.bottom, Slizaa21SplitViewPositions.BOTTOM)}
                            />
                        )
                )}
            </DatabaseAndHierarchicalGraphContext.Consumer>
        );
    }

    private createCard = (component: ISlizaa21SplitViewComponent, position: Slizaa21SplitViewPositions): JSX.Element => {

        const allowOverflow: boolean = component.allowOverflow ? true : false;

        return <Card title={component.title}
                     id={position.toString()}
                     padding={0}
                     allowOverflow={allowOverflow}
                     handleMaximize={this.handleMaximize}
        >
            {component.element}
        </Card>;
    }

    private updateWindowDimensions = (): void => {
        const newHeight = window.innerHeight - 55;
        const newWidth = window.innerWidth - 9;
        this.setState(
            {
                viewHeight: newHeight,
                viewWidth: newWidth
            });
    }

    private handleMaximize = (id?: string): void => {
        const newMaximizedCardId = this.state.maximizedCardId !== undefined && this.state.maximizedCardId === id ? undefined : id;
        this.setState({maximizedCardId: newMaximizedCardId})
    }


    private onHorizontalRatioChanged = (id: string, newRation: number): void => {
        this.setState({horizontalSplitRatio: newRation})
    }


    private onUpperSplitLayoutWidthChanged = (id: string, newWidth: number): void => {
        this.setState({upperVerticalSplitPosition: newWidth});
    }
}

