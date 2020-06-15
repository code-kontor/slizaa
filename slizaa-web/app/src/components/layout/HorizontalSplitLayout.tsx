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
import {HorizontalSplitLayoutOverlay} from "./HorizontalSplitLayoutOverlay";

export interface IProps {
    id: string
    top: JSX.Element
    bottom: JSX.Element
    height: number
    gutter: number
    initialRatio: number
    onRatioChanged?: (id: string, newRatio: number) => void
}

export interface IState {
    gutter: number,
    currentRatio: number
}

export class HorizontalSplitLayout extends React.Component<IProps, IState> {

    constructor(props: IProps) {
        super(props);
        this.state = {
            currentRatio: props.initialRatio,
            gutter: props.gutter,
        };
    }

    public componentWillReceiveProps(nextProps: IProps) {
        this.setState( {
            currentRatio: nextProps.initialRatio,
            gutter: nextProps.gutter,
        });
    }

    public render() {
        return (
            <div style={{
                height: this.props.height + "px",
                position: "relative",
                width: "100%"
            }}>

                <HorizontalSplitLayoutOverlay
                    height={this.props.height}
                    initialRatio={this.state.currentRatio}
                    gutter={this.state.gutter}
                    onRatioChanged={this.handleRatioChanged}
                />

                <div style={{
                    backgroundColor: "transparent",
                    display: "flex",
                    flexFlow: "column",
                    height: "100%",
                    overflow: "hidden",
                    width: "100%",
                }}>

                    <div style={{
                        flex: "0 0 " + (this.state.currentRatio / 1000) * this.props.height + "px",
                        height: "100%",
                        overflow: "hidden"
                    }}>
                        {this.props.top}
                    </div>

                    <div style={{
                        backgroundColor: "transparent",
                        height: this.state.gutter + "px"
                    }}/>

                    <div style={{
                        flex: "1 0 0px",
                        overflow: "hidden"
                    }}>
                        {this.props.bottom}
                    </div>

                </div>
            </div>
        );
    }

    private handleRatioChanged = (newRatio: number): void => {
        this.setState({
            currentRatio: newRatio
        })
        if (this.props.onRatioChanged) {
            this.props.onRatioChanged(this.props.id, newRatio)
        }
    }
}