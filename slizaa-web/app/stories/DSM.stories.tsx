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
import { storiesOf } from '@storybook/react';
import * as React from 'react';
import { DSM } from '../src/components/dsm';
import {IDsmSelection} from "../src/components/dsm/IDsmProps";

function createCells(count: number): Array<{ row: number, column: number, value: number }> {
  const result: Array<{ row: number, column: number, value: number }> = [];
  for (let i = 0; i < count; i++) {
    for (let j = 0; j < count; j++) {
      result.push({row: i, column: j, value: i * j});      
    }
  }
  return result;
}

function createLabels(count: number): Array<{ id: string, text: string }> {
  const result: Array<{ id: string, text: string }> = [];
  for (let index = 0; index < count; index++) {
    result.push({id: "Module " + index, text: "Ueberlange Modulnamen " + index});
  }
  return result;
}

function handleOnHover(selection: IDsmSelection | undefined) {
  // tslint:disable-next-line
  console.log(selection !== undefined ? selection.sourceLabel.text + " : " + selection.targetLabel.text : "undefined");
}

storiesOf('DSM', module)
  .add('4x4 with cycle', () => (
    <DSM labels={createLabels(4)}
      cells={createCells(4)}
      stronglyConnectedComponents={[{ nodePositions: [1, 2] }]} 
      onHover={handleOnHover}
      verticalSideMarkerWidth={30}
      horizontalSideMarkerHeight={30}/>
  ))
  .add('4x4 without cycle', () => (
    <DSM labels={createLabels(4)}
      cells={createCells(4)}
      stronglyConnectedComponents={[]} 
      onHover={handleOnHover}
      verticalSideMarkerWidth={30}
      horizontalSideMarkerHeight={30}/>
  ))
  .add('20x20 with custom box size', () => (
    <DSM labels={createLabels(20)}
      cells={createCells(20)}
      stronglyConnectedComponents={[]} 
      horizontalBoxSize={50}
      verticalBoxSize={30}
      onHover={handleOnHover}
      verticalSideMarkerWidth={30}
      horizontalSideMarkerHeight={30}/>
  ))
  .add('40x40 without cycle', () => (
    <DSM labels={createLabels(40)}
      cells={createCells(40)}
      stronglyConnectedComponents={[]}
      onHover={handleOnHover}
      verticalSideMarkerWidth={30}
      horizontalSideMarkerHeight={30}/>
  ))
  .add('10x10 with cycles', () => (
    <DSM labels={createLabels(10)}
      cells={createCells(10)}
      stronglyConnectedComponents={ [{ nodePositions: [1, 2] }, { nodePositions: [7, 8, 9] } ]} 
      onHover={handleOnHover}
      verticalSideMarkerWidth={30}
      horizontalSideMarkerHeight={30}/>
  )); 