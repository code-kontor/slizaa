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

export interface ISlizaaSplitViewState {
    viewHeight: number
    viewWidth: number
    horizontalSplitRatio: number
    maximizedCardId?: string
}

export interface ISlizaaSplitViewProps {
    id: string,
    maximizedElementId?: string
}

export interface ISlizaaSplitViewComponent {
    title: string,
    allowOverflow?: boolean,
    element: React.ReactNode,
    menuProviderFunc?: () => React.ReactNode,
    buttonProviderFunc?: () => React.ReactNode,
}

export abstract class AbstractSlizaaSplitView<P extends ISlizaaSplitViewProps, S extends ISlizaaSplitViewState> extends React.Component<P, S> {

    public static GUTTER_SIZE = 4

    protected constructor(props: P) {
        super(props);
    }

    public componentDidMount(): void {
        this.updateWindowDimensions();
        window.addEventListener('resize', this.updateWindowDimensions);
    }

    public componentWillUnmount(): void {
        window.removeEventListener('resize', this.updateWindowDimensions);
    }

    protected createCard = (component: ISlizaaSplitViewComponent, componentId: string): JSX.Element => {

        const allowOverflow: boolean = component.allowOverflow ? true : false;

        return <Card title={component.title}
                     id={componentId.toString()}
                     padding={0}
                     allowOverflow={allowOverflow}
                     handleMaximize={this.handleMaximize}
                     menuProviderFunc={component.menuProviderFunc}
                     buttonProviderFunc={component.buttonProviderFunc}
        >
            {component.element}
        </Card>;
    }

    protected onHorizontalRatioChanged = (id: string, newRation: number): void => {
        this.setState({horizontalSplitRatio: newRation})
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
}

