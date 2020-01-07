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
import {IDependencySelection} from "../IDependenciesViewState";
import './DependencyOverviewPart.css';
import {IDependencyOverviewPartProps} from "./IDependencyOverviewPartProps";
import {IDependencyOverviewPartState} from "./IDependencyOverviewPartState";
import {ImageLabel} from "./ImageLabel";

export class DependencyOverviewPart extends React.Component<IDependencyOverviewPartProps, IDependencyOverviewPartState> {

    constructor(props: Readonly<IDependencyOverviewPartProps>) {
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
                        <DSM labels={this.props.labels}
                             cells={this.props.cells}
                             stronglyConnectedComponents={this.props.stronglyConnectedComponents}
                             horizontalBoxSize={35}
                             verticalBoxSize={35}
                             horizontalSideMarkerHeight={this.props.horizontalSideMarkerHeight}
                             verticalSideMarkerWidth={this.props.verticalSideMarkerWidth}
                             onSideMarkerResize={this.props.onSideMarkerResize}
                             onSelect={this.props.onSelect}
                             onHover={this.onHoverDependency}
                             iconProvider={this.iconProvider(cl)}
                        />
                    </div>
                </div>
            }
        </ApolloConsumer>
    }

    private onHoverDependency = (aSelection: IDsmSelection | undefined): void => {
        if (aSelection !== undefined) {
            this.setState({
                hoveredDependency: {
                    sourceNodeLabel: aSelection.sourceLabel,
                    targetNodeLabel: aSelection.targetLabel,
                    weight: aSelection.selectedCell.value
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
                <ImageLabel iconId={aSelection.sourceNodeLabel.iconIdentifier} label={aSelection.sourceNodeLabel.text}/>
                <div className="selected-dependency-arrow">⟹</div>
                <ImageLabel iconId={aSelection.targetNodeLabel.iconIdentifier} label={aSelection.targetNodeLabel.text}/>
                <div className="selected-dependency-weight">({aSelection.weight} Dependencies)</div>
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