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
import * as React from "react";
import {ApolloConsumer} from "react-apollo";
import {DSM} from "../../../components/dsm";
import {IDsmLabel, IDsmSelection} from "../../../components/dsm/IDsmProps";
import {fetchSvg} from "../../../components/slizaaicon";
import {IDependencySelection} from "../IDependencyViewModel";
import './DsmPart.css';
import {IDsmPartProps} from "./IDsmPartProps";
import {IDsmPartState} from "./IDsmPartState";
import {ImageLabel} from "./ImageLabel";

export class DsmPart extends React.Component<IDsmPartProps, IDsmPartState> {

    constructor(props: Readonly<IDsmPartProps>) {
        super(props);

        this.state = {
            hoveredDependency: undefined
        }
    }

    public render() {

        const selectedDependencyLabel =
            this.state.hoveredDependency ?
                this.renderDependencySelection(this.state.hoveredDependency) :
                this.renderDependencySelection(this.props.dependencySelection);

        return <ApolloConsumer>
            {cl =>
                <div className="dsm-container">
                    <div className="dsm-selection">
                        {selectedDependencyLabel}
                    </div>
                    <div className="dsm">
                        <DSM labels={this.props.orderedNodes}
                             cells={this.props.dependencies}
                             stronglyConnectedComponents={this.props.stronglyConnectedComponents}
                             horizontalBoxSize={35}
                             verticalBoxSize={35}
                             horizontalSideMarkerHeight={this.props.horizontalSideMarkerHeight}
                             verticalSideMarkerWidth={this.props.verticalSideMarkerWidth}
                             onSideMarkerResize={this.props.onSideMarkerResize}
                             onSelect={this.onSelect}
                             onHover={this.onHoverDependency}
                             iconProvider={this.iconProvider(cl)}
                        />
                    </div>
                </div>
            }
        </ApolloConsumer>
    }

    private onSelect = (aSelection: IDsmSelection | undefined): void => {
        if (this.props.onSelect !== undefined) {

            const selection: IDependencySelection | undefined = aSelection !== undefined ? {
                selectedEdge: aSelection.selectedCell,
                sourceNode: aSelection.sourceLabel,
                targetNode: aSelection.targetLabel,

            } : undefined;

            this.props.onSelect(selection)
        }
    }

    private onHoverDependency = (aSelection: IDsmSelection | undefined): void => {
        if (aSelection !== undefined) {
            this.setState({
                hoveredDependency: {
                    selectedEdge: aSelection.selectedCell,
                    sourceNode: aSelection.sourceLabel,
                    targetNode: aSelection.targetLabel,
                }
            });
        } else {
            this.setState({
                hoveredDependency: undefined
            });
        }
    }

    private renderDependencySelection = (aSelection: IDependencySelection | undefined): React.ReactNode => {

        let element: React.ReactElement;

        if (aSelection !== undefined) {
            element = <div className="selected-dependency-container">
                <ImageLabel iconId={aSelection.sourceNode.iconIdentifier} label={aSelection.sourceNode.text}/>
                <div className="selected-dependency-arrow">⟹</div>
                <ImageLabel iconId={aSelection.targetNode.iconIdentifier} label={aSelection.targetNode.text}/>
                <div className="selected-dependency-weight">({aSelection.selectedEdge.value} Dependencies)</div>
            </div>
        } else {
            element = <div className="selected-dependency-container"/>
        }

        return element;
    }

    private iconProvider = (client: ApolloClient<any>): ((label: IDsmLabel) => Promise<string>) => {
        return (label) => fetchSvg(client, label.iconIdentifier);
    }
}