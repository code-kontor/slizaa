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