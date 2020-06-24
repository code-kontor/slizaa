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
import * as React from "react";
import {Card} from "../../components/card";
import {ICardProps} from "./Card";
import MaximizedCardSupportContext from './MaximizedCardSupportContext';

export interface IMaximizedCardSupportProviderState {
    maximizedCardProps: ICardProps | undefined;
    children: React.ReactNode | undefined;
}

export class MaximizedCardSupportProvider extends React.Component<any, IMaximizedCardSupportProviderState> {

    constructor(props: any) {
        super(props);

        this.state = {
            children: undefined,
            maximizedCardProps: undefined,
        }
    }

    public render() {

        const children = this.state.maximizedCardProps ?
            (<Card title={this.state.maximizedCardProps.title}
                   id={this.state.maximizedCardProps.id}
                   padding={0}>
                {this.state.children}
            </Card>) :
            this.props.children;

        //
        return (
            <MaximizedCardSupportContext.Provider
                value={{
                    handleOnMaximize: this.handleOnMaximize,
                    maximizedCardProps: this.props.maximizedCardProps,
                }}
            >
                {children}
            </MaximizedCardSupportContext.Provider>
        );
    }

    private handleOnMaximize = (card: Card): void => {
        if (this.state.maximizedCardProps) {
            if (this.state.maximizedCardProps.id === card.props.id) {
                this.setState({
                    children: undefined,
                    maximizedCardProps: undefined,
                })
            }
        } else {
            this.setState({
                children: card.props.children,
                maximizedCardProps: card.props,
            })
        }
    }
}