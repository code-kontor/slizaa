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
import { DraggableCore, DraggableData, DraggableEvent, DraggableEventHandler } from 'react-draggable';

import './Layout.css';

export interface IProps {
    id: string
    left: JSX.Element
    right: JSX.Element
    initialWidth?: number
    gutter?: number
    onWidthChanged?: (id: string, newWidth: number) => void;
}

export interface IState {
    width: number
}

export class HorizontalSplitLayout extends React.Component<IProps, IState> {

    constructor(props: IProps) {
        super(props);
        this.state = {
            width: props.initialWidth ? props.initialWidth : 200
        };
    }

    public componentWillReceiveProps(nextProps: IProps) {
        if (nextProps.initialWidth !== this.state.width) {
            this.setState({ width: nextProps.initialWidth ? nextProps.initialWidth : 200 })
        }
    }

    public render() {
        return (
            <div className="contentFlexContainer" style={{ flexFlow: "row", height: "100%", overflow: "hidden", backgroundColor: "transparent" }}>
                <div className="item item1" style={{ flex: "0 0 " + this.state.width + "px", overflow: "hidden" }}>
                    {this.props.left}
                </div>
                <DraggableCore
                    onStop={this.dragHandler('onResizeStop')}
                    onStart={this.dragHandler('onResizeStart')}
                    onDrag={this.dragHandler('onResize')} >
                    <div className="verticalDivider" style={{ width: this.props.gutter ? this.props.gutter : 8 + "px", backgroundColor: "transparent" }} />
                </DraggableCore>
                <div className="item item2" style={{ flex: "1 0 0px", overflow: "hidden" }}>
                    {this.props.right}
                </div>
            </div>
        );
    }

    private dragHandler = (handlerName: string): DraggableEventHandler => {
        return (e: DraggableEvent, data: DraggableData): void | false => {
            const newWidth = this.state.width + data.deltaX;
            // Early return if no change after constraints
            if (newWidth === this.state.width) { return };
            this.setState({ width: newWidth });
            if (this.props.onWidthChanged) {
                this.props.onWidthChanged(this.props.id, newWidth);
            }
        }
    }
}