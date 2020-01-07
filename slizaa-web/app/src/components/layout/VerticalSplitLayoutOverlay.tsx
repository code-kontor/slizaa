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
import {DraggableCore, DraggableData, DraggableEvent, DraggableEventHandler} from 'react-draggable';

export interface IVerticalSplitLayoutOverlayProps {
    height: number
    gutter: number
    initialRatio: number
    onRatioChanged?: (newRatio: number) => void
}

export interface IVerticalSplitLayoutOverlayState {
    currentRatio: number
    dragging: boolean
}

export class VerticalSplitLayoutOverlay extends React.Component<IVerticalSplitLayoutOverlayProps, IVerticalSplitLayoutOverlayState> {

    constructor(props: IVerticalSplitLayoutOverlayProps) {
        super(props);
        this.state = {
            currentRatio: props.initialRatio,
            dragging: false
        };
    }

    public componentWillReceiveProps(nextProps: IVerticalSplitLayoutOverlayProps) {
        this.state = {
            currentRatio: nextProps.initialRatio,
            dragging: false
        };
    }

    public render() {
        return (
            <div style={{
                height: this.props.height,
                left: "0",
                pointerEvents: "none",
                position: "absolute",
                top: "0",
                width: "100%",
            }}>
                <div className="contentFlexContainer"
                     style={{
                         backgroundColor: "transparent",
                         display: "flex",
                         flexFlow: "column",
                         height: "100%",
                     }}>
                    <div style={{
                        backgroundColor: "dimgray",
                        flex: "0 0 " + (this.state.currentRatio / 1000) * this.props.height + "px",
                        opacity: 0.0,
                        visibility: this.state.dragging ? "visible" : "hidden",
                    }}/>
                    <DraggableCore
                        onStop={this.handleDragTopHeight()}
                        onStart={this.handleDragTopHeight()}
                        onDrag={this.handleDragTopHeight()}>
                        <div style={{
                            backgroundColor: this.state.dragging ? "dimgray" : "transparent",
                            cursor: "ns-resize",
                            height: this.props.gutter ? this.props.gutter : 8 + "px",
                            pointerEvents: "auto",
                            zIndex: 1000
                        }}/>
                    </DraggableCore>
                    <div style={{flex: "1 0 0px", overflow: "hidden"}}>
                        <div style={{
                            backgroundColor: "dimgray",
                            height: "100%",
                            opacity: 0.0,
                            visibility: this.state.dragging ? "visible" : "hidden",
                        }}/>
                    </div>
                </div>

            </div>
        );
    }

    private handleDragTopHeight = (): DraggableEventHandler => {
        return (e: DraggableEvent, data: DraggableData): void | false => {
            if (e.type === "mousedown") {
                this.setState({
                    dragging: true
                });
            } else {
                const newRation = this.state.currentRatio + ((data.deltaY * 1000) / this.props.height);
                if (e.type === "mouseup") {
                    this.setState({
                        currentRatio: newRation,
                        dragging: false
                    });
                    if (this.props.onRatioChanged) {
                        this.props.onRatioChanged(newRation)
                    }
                } else if (e.type === "mousemove") {
                    this.setState({
                        currentRatio: newRation,
                    });
                }
            }
        }
    }
}
