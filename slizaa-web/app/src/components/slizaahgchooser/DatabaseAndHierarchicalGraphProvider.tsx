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
import DatabaseAndHierarchicalGraphContext from "./DatabaseAndHierarchicalGraphContext";

interface IDatabaseAndHierarchicalGraphProviderState {
    currentDatabase: string | undefined;
    currentHierarchicalGraph: string | undefined;
}

export class DatabaseAndHierarchicalGraphProvider extends React.Component<any, IDatabaseAndHierarchicalGraphProviderState> {

    constructor(props: any) {
        super(props);

        this.state = {
            currentDatabase: undefined,
            currentHierarchicalGraph: undefined
        }
    }

    public render() {

        //
        return (
            <DatabaseAndHierarchicalGraphContext.Provider
                value={{
                    currentDatabase: this.state.currentDatabase,
                    currentHierarchicalGraph: this.state.currentHierarchicalGraph,
                    setCurrentDatabase: (db: string): void => {
                        this.setState({
                            currentDatabase: db
                        });
                    },
                    setCurrentHierarchicalGraph: (hg: string): void => {
                        this.setState({
                            currentHierarchicalGraph: hg
                        });
                    },
                }}
            >
                {this.props.children}
            </DatabaseAndHierarchicalGraphContext.Provider>
        );
    }
}