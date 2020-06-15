import React from 'react';

export interface IDatabaseAndHierarchicalGraphContext {
    currentDatabase: Readonly<string | undefined>;
    currentHierarchicalGraph: Readonly<string | undefined>;
    setCurrentDatabase(currentDatabase: string): void;
    setCurrentHierarchicalGraph(currentDatabase: string): void;
}

const defaultCtx : IDatabaseAndHierarchicalGraphContext =  {
    currentDatabase: undefined,
    currentHierarchicalGraph: undefined,
    setCurrentDatabase(currentDatabase: string): void {
        // nothing
    },
    setCurrentHierarchicalGraph(currentDatabase: string): void {
        // nothing
    }
}

const DatabaseAndHierarchicalGraphContext = React.createContext(defaultCtx);
export default DatabaseAndHierarchicalGraphContext;