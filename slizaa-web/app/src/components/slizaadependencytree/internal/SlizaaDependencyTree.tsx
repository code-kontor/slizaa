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
import ApolloClient from 'apollo-client';
import * as React from "react";
import {SlizaaIcon} from 'src/components/slizaaicon';
import {NodeType} from "../../../__generated__/query-types";
import {ISlizaaNode} from "../../../model/ISlizaaNode";
import {SlizaaNode} from "../../../model/SlizaaNode";
import {fetchChildrenFilterByDependencySet} from "../../../model/SlizaaNodeChildrenResolver";
import {HorizontalSplitLayout} from "../../layout";
import STree from "../../stree/STree";
import {ISlizaaDependencyTreeProps} from "./ISlizaaDependencyTreeProps";
import {ISlizaaDependencyTreeState} from "./ISlizaaDependencyTreeState";

export class SlizaaDependencyTree extends React.Component<ISlizaaDependencyTreeProps, ISlizaaDependencyTreeState> {

    constructor(props: ISlizaaDependencyTreeProps) {
        super(props);

        this.state = {
            sourceNode: SlizaaNode.createRoot("Root", "default"),
            targetNode: SlizaaNode.createRoot("Root", "default")
        };
    }

    public componentWillReceiveProps(nextProps: ISlizaaDependencyTreeProps) {


        if (nextProps.sourceNodeId !== this.props.sourceNodeId ||
            nextProps.targetNodeId !== this.props.targetNodeId) {

            // tslint:disable-next-line:no-console
            console.log("nextProps: " + nextProps + " : " + this.props)

            this.setState({
                sourceNode: SlizaaNode.createRoot("Root", "default"),
                targetNode: SlizaaNode.createRoot("Root", "default")
            })
        }
    }

    public render() {

        return <HorizontalSplitLayout id="upper" initialWidth={450}
                               left={

                                   <STree
                                       rootNode={this.state.sourceNode}
                                       // onExpand={this.onExpand}
                                       // onSelect={this.onSelect}
                                       loadData={this.loadChildrenFilteredByDependencySource(this.props.client, this.props.sourceNodeId, this.props.targetNodeId)}
                                       fetchIcon={this.fetchIcon}
                                   />

                               }
                               right={

                                   <STree
                                       rootNode={this.state.targetNode}
                                       // onExpand={this.onExpand}
                                       // onSelect={this.onSelect}
                                       loadData={this.loadChildrenFilteredByDependencyTarget(this.props.client, this.props.sourceNodeId, this.props.targetNodeId)}
                                       fetchIcon={this.fetchIcon}
                                   />

                               }
        />
    }

    private fetchIcon = (item: ISlizaaNode): React.ReactNode => {
        return <SlizaaIcon iconId={item.iconId}/>
    }

    private loadChildrenFilteredByDependencySource = (client: ApolloClient<any>, dependencySourceNodeId: string, dependencyTargetNodeId: string): (parent: SlizaaNode, callback: () => void) => Promise<{}> => {
        return (p: SlizaaNode, c: () => void) => fetchChildrenFilterByDependencySet(client, p, NodeType.SOURCE, dependencySourceNodeId, dependencyTargetNodeId, this.props.databaseId, this.props.hierarchicalGraphId, c);
    }

    private loadChildrenFilteredByDependencyTarget = (client: ApolloClient<any>, dependencySourceNodeId: string, dependencyTargetNodeId: string): (parent: SlizaaNode, callback: () => void) => Promise<{}> => {
        return (p: SlizaaNode, c: () => void) => fetchChildrenFilterByDependencySet(client, p, NodeType.TARGET, dependencySourceNodeId, dependencyTargetNodeId, this.props.databaseId, this.props.hierarchicalGraphId, c);
    }
}