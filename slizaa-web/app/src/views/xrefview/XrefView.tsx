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
import ApolloClient from "apollo-client";
import React from "react";
import {ApolloConsumer} from "react-apollo";
import {SlizaaCrossReferenceViewer} from "../../components/slizaacrossreferenceviewer/SlizaaCrossReferenceViewer";
import {SlizaaDependencyViewer} from "../../components/slizaadependencyviewer/SlizaaDependencyViewer";
import {Slizaa11SplitView} from "../fwk/Slizaa11SplitView";
import {IXrefViewProps} from "./IXrefViewProps";
import {IXrefViewState} from "./IXrefViewState";

export class XrefView extends React.Component<IXrefViewProps, IXrefViewState> {

    constructor(props: IXrefViewProps) {
        super(props);
        this.state = {}
    }

    public render() {
        return (
            <ApolloConsumer>
                {cl => (
                    <Slizaa11SplitView
                        id="XrefView"
                        top={{
                            element: this.xrefViewElement(cl),
                            title: "Cross-Reference View",
                        }}
                        bottom={{
                            element: this.dependencyDetails(cl),
                            title: "Dependencies Details",
                        }}
                    />)
                }
            </ApolloConsumer>
        );
    }

    private xrefViewElement(client: ApolloClient<any>): React.ReactElement {
        return (
            <SlizaaCrossReferenceViewer client={client} databaseId={this.props.databaseId} hierarchicalGraphId={this.props.hierarchicalGraphId}/>
        );
    }

    private dependencyDetails(client: ApolloClient<any>): React.ReactElement {

        // return this.state.mainDependencySelection === undefined ?

            // return empty div if selected dependency is undefined
           // <div/> :

        const sourceNodeId = "263956";
        const targetNodeId = "7221";

        // else retunr the dependency viewer
            return <SlizaaDependencyViewer
                key={sourceNodeId + "-" + targetNodeId}
                client={client}
                databaseId={this.props.databaseId}
                hierarchicalGraphId={this.props.hierarchicalGraphId}
                sourceNodeId={sourceNodeId}
                targetNodeId={targetNodeId}
            />
    }
}
