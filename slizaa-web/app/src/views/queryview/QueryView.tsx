import React from "react";
import {Card} from "../../components/card";
import {VerticalSplitLayout} from "../../components/layout";
import {DependenciesViewProps} from "../dependenciesview/DependenciesViewLayoutConstants";
import {SlizaaHorizontalSplitView} from "../fwk/SlizaaHorizontalSplitView";
import {IQueryViewProps} from "./IQueryViewProps";
import {IQueryViewState} from "./IQueryViewState";

export class QueryView extends React.Component<IQueryViewProps, IQueryViewState> {

    constructor(props: IQueryViewProps) {
        super(props);
        this.state = {}
    }

    public render() {
        return (
            <SlizaaHorizontalSplitView
                id="QueryView"
                top={<VerticalSplitLayout
                    id="upper"
                    gutter={DependenciesViewProps.GUTTER_SIZE}
                    initialWidth={340}
                    left={
                        <Card
                            id="top1"
                            title="Top 1"/>
                    }
                    right={
                        <Card
                            id="top2"
                            title="Top 2"/>
                    }
                />}
                bottom={this.bottomElement()}
            />
        );
    }

    protected bottomElement(): JSX.Element {
        return (
            <Card
                id="bottom"
                title="Bottom"/>
        );
    }
}
