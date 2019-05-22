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

import { storiesOf } from '@storybook/react';
import * as React from 'react';
import { Card } from '../src/components/card';
import { HorizontalSplitLayout, ResizableBox } from '../src/components/layout';

storiesOf('Layout', module)
  .add('Resizable box (100px)', () => (
    <ResizableBox id="lowerResizableBox" intitalHeight={100} >
      <div style={{ backgroundColor: 'coral', width: "100%", height: "1000px" }}>Hello!</div>
    </ResizableBox>
  ))
  .add('Resizable box (350px)', () => (
    <ResizableBox id="lowerResizableBox" intitalHeight={350} >
      <div style={{ backgroundColor: 'coral', width: "100%", height: "1000px" }}>Hello!</div>
    </ResizableBox>
  ))
  .add('HorizontalSplitLayout', () => (
    <ResizableBox id="lowerResizableBox" intitalHeight={100} >
      <HorizontalSplitLayout
        id="upper"
        initialWidth={450}
        left={
          <Card title="Left">
            <div style={{ backgroundColor: 'coral', width: "100%", height: "1000px" }}>Left</div>
          </Card>
        }
        right={
          <Card title="Right">
            <div style={{ backgroundColor: 'red', width: "750px", height: "1000px" }}>Right</div>
          </Card>} />
    </ResizableBox>
  ));