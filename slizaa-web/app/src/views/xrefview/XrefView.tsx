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
import { Button, Empty } from 'antd';
import ApolloClient from "apollo-client";
import React from "react";
import {ApolloConsumer} from "react-apollo";
import { SlizaaDependencyViewer } from 'src/components/slizaadependencyviewer/SlizaaDependencyViewer';
import {SlizaaCrossReferenceViewer} from "../../components/slizaacrossreferenceviewer/SlizaaCrossReferenceViewer";
import {Slizaa11SplitView} from "../fwk/Slizaa11SplitView";
import {IXrefViewProps} from "./IXrefViewProps";
import {IXrefViewState} from "./IXrefViewState";

export class XrefView extends React.Component<IXrefViewProps, IXrefViewState> {

    public constructor(props: IXrefViewProps) {
        super(props);
        this.state = {}
    }

    public render() {
        // 

        return (
            <ApolloConsumer>
                {cl => (
                    <Slizaa11SplitView
                        id="XrefView"
                        top={{
                            buttonProviderFunc: this.buttonProvider,
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
            <SlizaaCrossReferenceViewer
                client={client}
                databaseId={this.props.databaseId}
                hierarchicalGraphId={this.props.hierarchicalGraphId}
                onSelect={this.handleSelect}
            />
        );
    }

    private dependencyDetails(client: ApolloClient<any>): React.ReactElement {

        return  this.state.inspectSourceNodeIds && this.state.inspectSourceNodeIds.length > 0 && this.state.inspectTargetNodeIds && this.state.inspectTargetNodeIds.length > 0 ? 
            <SlizaaDependencyViewer
                key={this.state.inspectSourceNodeIds[0] + "-" + this.state.inspectTargetNodeIds[0]}
                client={client}
                databaseId={this.props.databaseId}
                hierarchicalGraphId={this.props.hierarchicalGraphId}
                sourceNodeId={this.state.inspectSourceNodeIds[0]}
                targetNodeId={this.state.inspectTargetNodeIds[0]}
            />
            :
            <Empty style={{display: "flex", justifyContent: "center", alignItems: "center", height: "100%", flexDirection:"column" }} image={Empty.PRESENTED_IMAGE_SIMPLE} />        
    }

    private handleSelect = (aSelectedSourceNodeIds?:string[], aSelectedTargetNodeIds?:string[]) : void => {
        this.setState({
            selectedSourceNodeIds: aSelectedSourceNodeIds,
            selectedTargetNodeIds: aSelectedTargetNodeIds
        });
    }

    private handleInspect = () : void => {
        this.setState({
            inspectSourceNodeIds: this.state.selectedSourceNodeIds,
            inspectTargetNodeIds: this.state.selectedTargetNodeIds
        });
    }
    
    private buttonProvider = () : React.ReactNode => {
        
        const enabled = this.state.selectedSourceNodeIds && this.state.selectedSourceNodeIds.length > 0 && this.state.selectedTargetNodeIds && this.state.selectedTargetNodeIds.length > 0 

        return <Button disabled={!enabled} className="slizaa-card-header-button" size={"small"} onClick={this.handleInspect}>Inspect</Button>
    }
}
