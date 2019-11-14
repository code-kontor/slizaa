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
import {DSM} from "../../../components/dsm";
import {IDsmSelection} from "../../../components/dsm/IDsmProps";
import {IDependencySelection} from "../internal/IDependenciesViewState";
import {IDsmPartProps} from "./IDsmPartProps";
import {IDsmPartState} from "./IDsmPartState";

export class DependencyOverviewPart extends React.Component<IDsmPartProps, IDsmPartState> {

    constructor(props: Readonly<IDsmPartProps>) {
        super(props);

        this.state = {
            hoveredDependency: undefined
        }
    }

    public render() {

        const selectedDependencyLabel =
            // has hoveredMainDependencySelection?
            this.state.hoveredDependency ?
                this.renderDependencySelection(this.state.hoveredDependency) :
                this.renderDependencySelection(this.props.dependencySelection);

        return <div className="dsm-container">
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
                />
            </div>
            <div className="dsm-selection">
                {selectedDependencyLabel}
            </div>
        </div>
    }

    private onHoverDependency = (aSelection: IDsmSelection | undefined): void => {
        if (aSelection !== undefined) {
            this.setState({
                hoveredDependency: {
                    sourceNodeId: aSelection.sourceLabel.id,
                    sourceNodeText: aSelection.sourceLabel.text,
                    targetNodeId: aSelection.targetLabel.id,
                    targetNodeText: aSelection.targetLabel.text,
                    weight: aSelection.selectedCell.value
                }
            });
        } else {
            this.setState({
                hoveredDependency: undefined
            });
        }
    }

    private renderDependencySelection = (aSelection: IDependencySelection | undefined): string => {
        return aSelection ?
            aSelection.sourceNodeText + " ⟹ " + aSelection.targetNodeText /* + "(" + aSelection.weight + ")"*/ :
            " "
    }
}