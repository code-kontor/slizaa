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
import {Dropdown} from 'antd';
import * as React from 'react';
import './Card.css';
import {FullScreenIcon, MenuIcon} from "./CardIcons";

export interface ICardProps {
    id: string
    title: string
    allowOverflow?: boolean
    padding?: number
    handleMaximize?: (id: string) => void
    menuProviderFunc?: () => React.ReactNode
}

export class Card extends React.Component<ICardProps, any> {

    public render() {

        const styleProperties = {
            overflow: this.props.allowOverflow !== undefined && !this.props.allowOverflow ? 'hidden' : 'auto',
            padding: this.props.padding !== undefined ? this.props.padding + 'px ' + this.props.padding + 'px' : '10px 10px',
        }

        const inlineBlock = this.props.menuProviderFunc ?
            (
                <div style={{display: "inline-block", float: "right"}}>
                    <Dropdown overlay={this.props.menuProviderFunc} placement="bottomRight">
                        <MenuIcon/>
                    </Dropdown>
                    <FullScreenIcon style={{paddingLeft: "5px"}} onClick={this.handleMaximizeClick}/>
                </div>
            )
            :
            (
                <div style={{display: "inline-block", float: "right"}}>
                    <FullScreenIcon style={{paddingLeft: "5px"}} onClick={this.handleMaximizeClick}/>
                </div>
            );

        return <div className="slizaa-card">
            <div className="slizaa-card-title">
                <div style={{float: "left", width: "50%"}}>{this.props.title}</div>
                {inlineBlock}
            </div>
            <div className="slizaa-card-body" style={styleProperties}>{this.props.children}</div>
        </div>;
    }

    private handleMaximizeClick = (event: React.MouseEvent<HTMLElement, MouseEvent>): void => {
        // tslint:disable-next-line:no-console
        console.log('click left button', event);
        if (this.props.handleMaximize) {
            this.props.handleMaximize(this.props.id);
        }
    }

    /*    private handleIconClick = (ctx: IMaximizedCardSupportContext): ((event: React.MouseEvent<HTMLElement, MouseEvent>) => void) => {
            return (event: React.MouseEvent<HTMLElement, MouseEvent>): void => {
                if (ctx.handleOnMaximize) {
                    ctx.handleOnMaximize(this);
                }
            }
        }*/
}
