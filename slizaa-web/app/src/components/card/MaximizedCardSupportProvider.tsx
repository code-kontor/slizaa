import * as React from "react";
import {Card} from "../../components/card";
import {ICardProps} from "./Card";
import MaximizedCardSupportContext from './MaximizedCardSupportContext';

export interface IMaximizedCardSupportProviderState {
    maximizedCardProps: ICardProps | undefined;
    children: React.ReactNode | undefined;
}

export class MaximizedCardSupportProvider extends React.Component<any, IMaximizedCardSupportProviderState> {

    constructor(props: any) {
        super(props);

        this.state = {
            children: undefined,
            maximizedCardProps: undefined,
        }
    }

    public render() {

        const children = this.state.maximizedCardProps ?
            (<Card title={this.state.maximizedCardProps.title}
                   id={this.state.maximizedCardProps.id}
                   padding={0}>
                {this.state.children}
            </Card>) :
            this.props.children;

        //
        return (
            <MaximizedCardSupportContext.Provider
                value={{
                    handleOnMaximize: this.handleOnMaximize,
                    maximizedCardProps: this.props.maximizedCardProps,
                }}
            >
                {children}
            </MaximizedCardSupportContext.Provider>
        );
    }

    private handleOnMaximize = (card: Card): void => {
        if (this.state.maximizedCardProps) {
            if (this.state.maximizedCardProps.id === card.props.id) {
                this.setState({
                    children: undefined,
                    maximizedCardProps: undefined,
                })
            }
        } else {
            this.setState({
                children: card.props.children,
                maximizedCardProps: card.props,
            })
        }
    }
}