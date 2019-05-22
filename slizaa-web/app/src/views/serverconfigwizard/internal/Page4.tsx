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
import { IServerExtension } from './ServerConfigWizard';

/**
 * Defines the properties for the react component Page4
 */
interface IProps {
    selectedExtensions: IServerExtension[] 
}

export const Page4 = ( props: IProps ) => {
    
    const listItems = props.selectedExtensions.map((extension, key) =>
        <li key={extension.symbolicName}>{extension.symbolicName}</li>);

    return <React.Fragment>
        <h1>STEP 4</h1>
        <ul>
            {listItems}
        </ul>
    </React.Fragment>
}