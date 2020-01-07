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
import '../src/SlizaaApp.css'

import {storiesOf} from '@storybook/react';
import * as React from 'react';
import {Card} from '../src/components/card';
import {HorizontalSplitLayout} from '../src/components/layout';
import {VerticalSplitLayout} from "../src/components/layout/VerticalSplitLayout";

storiesOf('Layout', module)
    .add('HorizontalSplitLayout', () => (
        <HorizontalSplitLayout
            id="upper"
            initialWidth={450}
            gutter={8}
            left={
                <Card title="Left" allowOverflow={true}>
                    <div style={{backgroundColor: 'coral', width: "100%", height: "1000px"}}>Left</div>
                </Card>
            }
            right={
                <Card title="Right" allowOverflow={true}>
                    <div style={{backgroundColor: 'red', width: "750px", height: "1000px"}}>Right</div>
                </Card>}/>
    ))
    .add('HorizontalSplitLayout (no overflow)', () => (
        <HorizontalSplitLayout
            id="upper"
            initialWidth={450}
            gutter={8}
            left={
                <Card title="Left" allowOverflow={false}>
                    <div style={{backgroundColor: 'coral', height: "100%", width: "100%", overflow: 'auto'}}>
                        <div style={{backgroundColor: 'red', width: "100%", height: "450px"}}>TEST</div>
                    </div>
                </Card>
            }
            right={
                <Card title="Right" allowOverflow={false}>
                    <div style={{backgroundColor: 'coral', height: "100%", width: "100%", overflow: 'auto'}}>
                        <div style={{backgroundColor: 'red', width: "750px", height: "1000px"}}>Right</div>
                    </div>
                </Card>}/>
    ))
    .add('HorizontalSplitLayout 2x', () => (
        <div>
            <HorizontalSplitLayout
                id="upper"
                initialWidth={450}
                gutter={8}
                left={
                    <Card title="Left" allowOverflow={true}>
                        <div style={{backgroundColor: 'coral', width: "100%", height: "1000px"}}>Left</div>
                    </Card>
                }
                right={
                    <Card title="Right" allowOverflow={true}>
                        <div style={{backgroundColor: 'red', width: "750px", height: "1000px"}}>Right</div>
                    </Card>}/>
            <HorizontalSplitLayout
                id="upper"
                initialWidth={450}
                gutter={8}
                left={
                    <Card title="Left" allowOverflow={true}>
                        <div style={{backgroundColor: 'coral', width: "100%", height: "1000px"}}>Left</div>
                    </Card>
                }
                right={
                    <Card title="Right" allowOverflow={true}>
                        <div style={{backgroundColor: 'red', width: "750px", height: "1000px"}}>Right</div>
                    </Card>}/>
        </div>
    ))
    .add('VerticalSplitLayout (no overflow)', () => (
        <VerticalSplitLayout
            id="upper"
            initialRatio={500}
            height={550}
            gutter={8}
            top={
                <Card title="top" allowOverflow={false}>
                    <div style={{backgroundColor: 'coral', height: "100%", width: "100%", overflow: 'auto'}}>
                        <div style={{backgroundColor: 'red', width: "100%", height: "450px"}}>TEST</div>
                    </div>
                </Card>
            }
            bottom={
                <Card title="bottom" allowOverflow={false}>
                    <div style={{backgroundColor: 'coral', height: "100%", width: "100%", overflow: 'auto'}}>
                        <div style={{backgroundColor: 'red', width: "750px", height: "1000px"}}>Right</div>
                    </div>
                </Card>}/>
    ));