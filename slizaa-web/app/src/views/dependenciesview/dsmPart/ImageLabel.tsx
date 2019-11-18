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
import {SlizaaIcon} from "../../../components/slizaaicon";
import './ImageLabel.css';

interface IImageLabelProps {
    iconId?: string
    label: string
}

export const ImageLabel = ({ iconId, label }: IImageLabelProps) => <div className="image-label-container">
    <SlizaaIcon iconId={iconId !== undefined ? iconId :""} />
    <div className="image-label-text">{label}</div>
</div>