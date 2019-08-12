import {IGlobalDependenciesViewState} from "../../../redux/IAppState";

export interface IDependenciesViewProps {
    databaseId: string
    hierarchicalGraphId: string
    dependenciesViewState: IGlobalDependenciesViewState
    dispatchSidemarkerResize: (horizontalHeight: number, verticalWidth: number) => void
}