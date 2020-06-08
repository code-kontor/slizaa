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
import {Dropdown, Menu} from 'antd';
import {ClickParam} from "antd/lib/menu";
import * as React from 'react';
import './Card.css';
import {EmptyIcon, HeartIcon, PandaIcon} from "./CardIcons";

export interface ICardProps {
    title: string
    allowOverflow?: boolean
    padding?: number
}

export class Card extends React.Component<ICardProps> {



    public render() {

        const menu = (
            <Menu onClick={this.handleMenuClick} selectedKeys={["1"]}>
                <Menu.Item key="1" >
                    <EmptyIcon />
                    1st menu item
                </Menu.Item>
                <Menu.Item key="2">
                    <HeartIcon />
                    2nd menu item
                </Menu.Item>
                <Menu.Item key="3">
                    <EmptyIcon />
                    3rd item
                </Menu.Item>
            </Menu>
        );

        const styleProperties = {
            overflow: this.props.allowOverflow !== undefined && !this.props.allowOverflow ? 'hidden' : 'auto',
            padding: this.props.padding !== undefined ? this.props.padding + 'px ' + this.props.padding + 'px' : '10px 10px',
        }

        const body = this.props.allowOverflow !== undefined && !this.props.allowOverflow ?
            <div className="slizaa-card-body" style={styleProperties}>{this.props.children}</div> :
            <div className="slizaa-card-body" style={styleProperties}>{this.props.children}</div>;

        return (
            <div className="slizaa-card" >
                <div className="slizaa-card-title" >
                    <div style={{ float: "left", width: "50%"}}>{this.props.title}</div>
                    <div style={{ display: "inline-block", float: "right"}}>
                        <Dropdown overlay={menu} placement="bottomRight">
                            <HeartIcon />
                        </Dropdown>
                        <PandaIcon style={{ paddingLeft: "5px" }} />
                    </div>
                </div>
                {body}
            </div>
        );
    }

    private handleMenuClick = (param: ClickParam): void => {
        // tslint:disable-next-line:no-console
        console.log('click left button', param);
    }

}
