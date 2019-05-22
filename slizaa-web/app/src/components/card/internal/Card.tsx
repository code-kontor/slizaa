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
import './Card.css';

export interface IProps {
    title: string
    allowOverflow?: boolean
}

export class Card extends React.Component<IProps> {

    private readonly cardStyle = {
        alignItems: 'stretch',
        display: 'flex',
        flexFlow: 'column',
        height: '100%',
        width: '100%',
    };

    public render() {

        const body = this.props.allowOverflow !== undefined && !this.props.allowOverflow ?
            <div className="slizaa-card-body" style={{ flex: "0 0 0px"}} >{this.props.children}</div> :
            <div className="slizaa-card-body" style={{ flex: "1 0 0px", overflow: 'auto' }} >{this.props.children}</div>;

        return (
            <div className="slizaa-card" style={this.cardStyle}>
                <div className="slizaa-card-title" style={{ flex: "0 0 0px" }}>{this.props.title}</div>
                {body}
            </div>
        );
    }

}