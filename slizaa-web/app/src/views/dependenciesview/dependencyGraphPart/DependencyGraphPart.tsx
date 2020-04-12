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
import {DependencyGraph} from "../../../components/dependencygraph";
import {IDependencyGraphEdge, IDependencyGraphNode} from "../../../components/dependencygraph/IDependencyGraphProps";
import {IDependencyGraphPartProps} from "./IDependencyGraphPartProps";

export class DependencyGraphPart extends React.Component<IDependencyGraphPartProps, any> {

    constructor(props: Readonly<IDependencyGraphPartProps>) {
        super(props);

        this.state = {
            hoveredDependency: undefined
        }
    }

    public render() {

        const nodes : IDependencyGraphNode[] = this.props.orderedNodes.map(node => ({id: node.id, text: node.text, iconIdentifier: node.iconIdentifier}));
        const edges :IDependencyGraphEdge[] = this.props.dependencies
            .filter(edge => edge.value > 0 && edge.column !== edge.row)
            .map(edge => (
            {
                id: edge.column + "-" + edge.row,
                sourceId: nodes[edge.column].id,
                targetId: nodes[edge.row].id,
                weight: edge.value
            }));

        return <DependencyGraph
            onEdgeSelected={this.handleSelect}
            nodes={nodes}
            edges={edges}
            scss={[]}/>
    }

    private handleSelect = (edge: IDependencyGraphEdge | undefined): void => {
        // tslint:disable-next-line
        console.log(edge !== undefined ? edge.id : "undefined");
    }
}