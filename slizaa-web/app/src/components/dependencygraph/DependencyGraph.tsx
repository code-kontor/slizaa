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
import {Spin} from "antd";
import ELK, {ElkNode} from 'elkjs/lib/elk.bundled.js'
import * as React from "react";
import './DependencyGraph.css';
import {DependencyGraphCanvas} from "./DependencyGraphCanvas";
import {IDependencyGraphProps} from "./IDependencyGraphProps";
import {IDependencyGraphState} from "./IDependencyGraphState";

export class DependencyGraph extends React.Component<IDependencyGraphProps, IDependencyGraphState> {

    private readonly NODE_HEIGHT = 25;
    private readonly NODE_WIDTH = 250;

    private nodeLayout: ElkNode | null;

    // private selectedEdge?: IDependencyGraphEdge;

    constructor(props: IDependencyGraphProps) {
        super(props);

        this.state = {};
    }

    public componentDidMount(): void {

        // tslint:disable-next-line:no-console
        console.log(this.nodeLayout)

        const arrayOfEdges = this.props.edges.map(value => ({
            id: value.id,
            labels: [{text: value.weight}],
            sources: [value.sourceId],
            targets: [value.targetId],
        }));
        const arrayOfNodes = this.props.nodes.map(value => ({
            height: this.NODE_HEIGHT,
            id: value.id,
            labels: [{text: value.text}],
            width: this.NODE_WIDTH,
        }));
        const arrayOfScss = this.props.scss.map(value =>
            ({
                children: value.nodes.map(n => ({id: n.id, labels: [{text: n.text}], width: this.NODE_WIDTH, height: this.NODE_HEIGHT})),
                id: value.id,
                layoutOptions: {
                    'org.eclipse.elk.layered.edgeLabels.sideSelection': 'ALWAYS_UP',
                    'org.eclipse.elk.spacing.edgeLabel': '4',
                },
            })
        );

        const elk = new ELK();
        // tslint:disable:object-literal-sort-keys
        const graph = {
            id: "root",
            layoutOptions: {
                'elk.algorithm': 'layered',
                'org.eclipse.elk.direction': 'DOWN',
                'org.eclipse.elk.layered.mergeEdges': 'false',
                // 'org.eclipse.elk.layered.spacing.edgeNodeBetweenLayers': '10',
                'org.eclipse.elk.layered.nodePlacement.strategy': 'BRANDES_KOEPF',
                'org.eclipse.elk.hierarchyHandling': 'INCLUDE_CHILDREN',
                // 'org.eclipse.elk.spacing.edgeLabel': '4',
                'org.eclipse.elk.layered.edgeLabels.sideSelection': 'SMART_UP',
                'org.eclipse.elk.core.options.EdgeLabelPlacement': 'TAIL'
            },
            children: [...arrayOfNodes, ...arrayOfScss],
            edges: arrayOfEdges
        }

        // @ts-ignore
        elk.layout(graph)
            // @ts-ignore
            .then((value) => {
                this.nodeLayout = value;
                this.setState({rootNode: value})
            })
            // tslint:disable-next-line:no-console
            .catch(console.error)
    }

    public render() {

        if (!this.state.rootNode) {
            return <Spin className="slizaaSpinner" size="large" />
        }

        return <DependencyGraphCanvas rootNode={this.state.rootNode} />
    }
}


