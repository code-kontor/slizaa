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
import '../src/SlizaaApp.css'

import {storiesOf} from '@storybook/react';
import * as React from 'react';
import {DependencyGraph} from "../src/components/dependencygraph";
import {IDependencyGraphEdge} from "../src/components/dependencygraph/IDependencyGraphProps";

storiesOf('DependencyGraph', module)
    .add('Simple DependencyGraph', () => (
        <DependencyGraph
            nodes={[
                {id: "n1", text: "module n1", iconIdentifier: ""},
                {id: "n2", text: "module n2", iconIdentifier: ""},
                {id: "n3", text: "module n3", iconIdentifier: ""},
                {id: "n4", text: "module n4", iconIdentifier: ""},
                {id: "n5", text: "module n5", iconIdentifier: ""},
                {id: "n6", text: "module n6", iconIdentifier: ""},
            ]}
            edges={[
                {id: "e1", sourceId: "n1", targetId: "n6", weight: 12},
                {id: "e2", sourceId: "n2", targetId: "n5", weight: 123},
                {id: "e3", sourceId: "n5", targetId: "n3", weight: 1046},
                {id: "e4", sourceId: "n3", targetId: "n4", weight: 25},
                {id: "e5", sourceId: "n4", targetId: "n3", weight: 26},
                {id: "e6", sourceId: "n3", targetId: "n6", weight: 345},
                {id: "e7", sourceId: "n2", targetId: "n6", weight: 176}
            ]}
            scss={[]}/>
    ))
    .add('Simple DependencyGraph 2', () => (
        <DependencyGraph
            onEdgeSelected={handleSelect}
            nodes={[
                {id: "n1", text: "module n1", iconIdentifier: ""},
                {id: "n2", text: "module n2", iconIdentifier: ""},
                {id: "n5", text: "module n5", iconIdentifier: ""},
                {id: "n6", text: "module n6", iconIdentifier: ""},
            ]}
            edges={[
                {id: "e1", sourceId: "n1", targetId: "n6", weight: 2},
                {id: "e2", sourceId: "n2", targetId: "n5", weight: 36},
                {id: "e3", sourceId: "n5", targetId: "n3", weight: 375},
                {id: "e4", sourceId: "n3", targetId: "n4", weight: 945},
                {id: "e5", sourceId: "n4", targetId: "n3", weight: 66},
                {id: "e6", sourceId: "n3", targetId: "n6", weight: 6},
                {id: "e7", sourceId: "n2", targetId: "n6", weight: 3686}
            ]}
            scss={[{
                id: "virt",
                nodes: [
                    {id: "n3", text: "module n3", iconIdentifier: ""},
                    {id: "n4", text: "module n4", iconIdentifier: ""}
                ]
            }
            ]}/>
    ));

function handleSelect(edge: IDependencyGraphEdge | undefined) {
    // tslint:disable-next-line
    console.log(edge !== undefined ? edge.id : "undefined");
}