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
import './ServerConfigWizard.css';

import { Button, Steps } from 'antd';
import * as React from 'react';
import { Page1 } from './Page1';
import { Page2 } from './Page2';
import { Page3 } from './Page3';
import { Page4 } from './Page4';
const Step = Steps.Step;

interface IProps {
    readonly rerender: () => void;
}

interface IState {
    selectedItems: IServerExtension[];
    currentPage: number;
}

export interface IServerExtension {
    symbolicName: string,
    version: string
}

export class ServerConfigWizard extends React.Component<IProps, IState> {

    constructor(props: IProps) {
        super(props);
        this.state = {
            currentPage: 0,
            selectedItems: []
        };
        this.setSelectedItems = this.setSelectedItems.bind(this);
    }

    public next() {
        const current = this.state.currentPage + 1;
        this.setState({ ...this.state, currentPage: current });
    }

    public prev() {
        const current = this.state.currentPage - 1;
        this.setState({ ...this.state, currentPage: current });
    }

    public render() {
        const { currentPage } = this.state;
        const onClickPrevious = () => this.prev();
        const onClickDone = () => this.props.rerender();
        const onClickNext = () => this.next();
        return (
            <div className="server-config-wizard">
                <Steps current={currentPage} >
                    <Step key="step_1" title="STEP 1" />
                    <Step key="step_2" title="STEP 2" />
                    <Step key="step_3" title="STEP 3" />
                    <Step key="step_4" title="STEP 4" />
                </Steps>
                <div className="steps-content">
                    {currentPage === 0 && <Page1 />}
                    {currentPage === 1 && <Page2 selectedExtensions={this.state.selectedItems} onItemsSelected={this.setSelectedItems}/>}
                    {currentPage === 2 && <Page3 selectedExtensions={this.state.selectedItems} />}
                    {currentPage === 3 && <Page4 selectedExtensions={this.state.selectedItems} />}
                </div>
                <div className="steps-action">
                    <Button disabled={currentPage === 0 || currentPage === 3 } style={{ marginLeft: 8 }} onClick={onClickPrevious}>
                        Previous
                    </Button>
                    {
                        currentPage === 3
                        && <Button type="primary" onClick={onClickDone}>Install</Button>
                    }
                    {
                        currentPage < 3
                        && <Button type="primary" onClick={onClickNext}>Next</Button>
                    }
                </div>
            </div>
        );
    }

    protected setSelectedItems(items : IServerExtension[] ) : void {
        this.setState({...this.state, selectedItems: items});
    }
}
