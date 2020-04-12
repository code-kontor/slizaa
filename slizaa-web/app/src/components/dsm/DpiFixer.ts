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
// See: http://www.html5rocks.com/en/tutorials/canvas/hidpi/
// See: 
export function setupCanvas(canvas: HTMLCanvasElement, context: any, customWidth?: number, customHeight?: number, scale?: number): number {

    const scaleFactor = scale === undefined || scale <= 0 ? 1 : scale;

    const width = customWidth ||
        canvas.width || // attr, eg: <canvas width='400'>
        canvas.clientWidth; // keep existing width

    const height = customHeight ||
        canvas.height ||
        canvas.clientHeight;
    const deviceRatio = window.devicePixelRatio || 1;

    const bsRatio = context.webkitBackingStorePixelRatio ||
        context.mozBackingStorePixelRatio ||
        context.msBackingStorePixelRatio ||
        context.oBackingStorePixelRatio ||
        context.backingStorePixelRatio || 1;

    const ratio = deviceRatio / bsRatio;

    // Adjust canvas if ratio =/= 1
    if (deviceRatio !== bsRatio) {
        canvas.width = Math.round(width * ratio) * scaleFactor;
        canvas.height = Math.round(height * ratio) * scaleFactor;
        canvas.style.width = width  * scaleFactor + 'px';
        canvas.style.height = height  * scaleFactor + 'px';
        context.scale(ratio * scaleFactor, ratio *scaleFactor);
    }
    return ratio;
};
