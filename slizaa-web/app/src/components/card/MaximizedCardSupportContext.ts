import React from 'react';
import {Card, ICardProps} from "./Card";

export interface IMaximizedCardSupportContext {
    handleOnMaximize: ((card: Card) => void) | undefined
    maximizedCardProps: ICardProps | undefined
}

const defaultCtx : IMaximizedCardSupportContext =  {
    handleOnMaximize: undefined,
    maximizedCardProps: undefined,
}

const MaximizedCardSupportContext = React.createContext(defaultCtx);
export default MaximizedCardSupportContext;