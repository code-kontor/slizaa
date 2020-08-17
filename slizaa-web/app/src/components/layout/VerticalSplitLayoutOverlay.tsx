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

export interface IHorizontalSplitLayoutOverlayProps {
    width: number
    gutter: number
    onWidthChanged: (newWidth: number) => void;
}

export interface IHorizontalSplitLayoutOverlayState {
    currentWidth: number,
    dragging: boolean
}

export class VerticalSplitLayoutOverlay extends React.Component<IHorizontalSplitLayoutOverlayProps, IHorizontalSplitLayoutOverlayState> {

    constructor(props: IHorizontalSplitLayoutOverlayProps) {
        super(props);
        this.state = {
            currentWidth: props.width,
            dragging: false
        };
    }

    public componentWillReceiveProps(nextProps: IHorizontalSplitLayoutOverlayProps) {
        this.setState({
            currentWidth: nextProps.width,
            dragging: false
        })
    }

    public render() {
        const dividerSize = this.props.width > 0 ? this.props.gutter : 0;
        return (
            <div style={{
                height: "100%",
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
                         flexFlow: "row",
                         height: "100%",
                         overflow: "hidden",
                     }}>
                    <div style={{
                        backgroundColor: "dimgray",
                        flex: "0 0 " + this.state.currentWidth + "px",
                        height: "100%",
                        opacity: 0.0,
                        visibility: this.state.dragging ? "visible" : "hidden",
                        width: this.state.currentWidth + "px",
                    }}/>
                    <DraggableCore
                        onStop={this.dragHandler()}
                        onStart={this.dragHandler()}
                        onDrag={this.dragHandler()}>
                        <div style={{
                            backgroundColor: this.state.dragging ? "dimgray" : "transparent",
                            cursor: "ew-resize",
                            pointerEvents: "auto",
                            // width: this.props.gutter ? this.props.gutter : 8 + "px",
                            width: dividerSize + "px",
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

    private dragHandler = (): DraggableEventHandler => {
        return (e: DraggableEvent, data: DraggableData): void | false => {
            if (e.type === "mousedown") {
                this.setState({
                    dragging: true
                });
            } else {
                const newWidth = this.state.currentWidth + data.deltaX;

                if (e.type === "mouseup") {
                    this.setState({
                        currentWidth: newWidth,
                        dragging: false
                    });
                    this.props.onWidthChanged(newWidth)
                } else if (e.type === "mousemove") {
                    this.setState({
                        currentWidth: newWidth
                    });
                }
            }
        }
    }
}