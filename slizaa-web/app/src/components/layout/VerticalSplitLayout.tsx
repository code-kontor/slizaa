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
import {VerticalSplitLayoutOverlay} from "./VerticalSplitLayoutOverlay";


export interface IProps {
    id: string
    left: JSX.Element
    right: JSX.Element
    initialWidth: number
    gutter: number
    onWidthChanged?: (id: string, newWidth: number) => void;
}

export interface IState {
    width: number,
    gutter: number
}

export class VerticalSplitLayout extends React.Component<IProps, IState> {

    constructor(props: IProps) {
        super(props);
        this.state = {
            gutter: this.props.gutter,
            width: props.initialWidth
        };
    }

    public componentWillReceiveProps(nextProps: IProps) {
        if (nextProps.initialWidth !== this.state.width) {
            this.setState({
                gutter: nextProps.gutter,
                width: nextProps.initialWidth
            })
        }
    }

    public render() {
        return (
            <div style={{height: "100%", position: "relative"}}>

                <VerticalSplitLayoutOverlay
                    width={this.state.width}
                    gutter={this.state.gutter}
                    onWidthChanged={this.handleNewWidth}/>

                <div className="contentFlexContainer"
                     style={{
                         backgroundColor: "transparent",
                         display: "flex",
                         flexFlow: "row",
                         height: "100%",
                         overflow: "hidden",
                     }}>

                    <div className="item item1" style={{
                        flex: "0 0 " + this.state.width + "px",
                        overflow: "hidden",
                    }}>
                        {this.props.left}
                    </div>
                    <div className="verticalDivider" style={{
                        backgroundColor: "transparent",
                        width: this.props.gutter + "px"
                    }}/>
                    <div className="item item2" style={{
                        flex: "1 0 0px",
                        overflow: "hidden"
                    }}>
                        {this.props.right}
                    </div>
                </div>

            </div>
        );
    }

    private handleNewWidth = (newWidth: number): void => {
        this.setState({
            width: newWidth > 0 ? newWidth : 0
        })
        if (this.props.onWidthChanged) {
            this.props.onWidthChanged(this.props.id, newWidth)
        }
    }
}