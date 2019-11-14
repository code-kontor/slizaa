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
import * as React from 'react';
import { setupCanvas } from './DpiFixer';
import './DSM.css';
import { DefaultColorScheme, IDsmColorScheme } from './IDsmColorScheme';
import {IDsmCell, IDsmProps, IDsmSelection} from "./IDsmProps";

export class DSM extends React.Component<IDsmProps, {}> {

    private readonly FONT = "12px Arial";
    private readonly SEP_SIZE = 4;
    private readonly TEXT_CLIP_PADDING = 5;

    private canvasRef: HTMLCanvasElement | null;
    private renderingContext: CanvasRenderingContext2D | null;

    private markedCellLayerCanvasRef: HTMLCanvasElement | null;
    private markedCellLayerrenderingContext: CanvasRenderingContext2D | null;

    private colorScheme: IDsmColorScheme = new DefaultColorScheme;

    private mouseOver: boolean;
    private mouseDown: boolean;
    private verticalResize: boolean;
    private horizontalResize: boolean;
    private horizontalSideMarkerHeight: number;
    private verticalSideMarkerWidth: number;
    private currentlyMarkedX: number | undefined;
    private currentlyMarkedY: number | undefined;
    private newlyMarkedX: number | undefined;
    private newlyMarkedY: number | undefined;
    private currentlySelectedX: number | undefined;
    private currentlySelectedY: number | undefined;
    private newlySelectedX: number | undefined;
    private newlySelectedY: number | undefined;
    private sccNodePositions: number[];
    private matrixElements: IDsmCell[][];

    constructor(props: IDsmProps) {
        super(props);

        this.horizontalSideMarkerHeight = props.horizontalSideMarkerHeight;
        this.verticalSideMarkerWidth = props.verticalSideMarkerWidth;
    }

    public shouldComponentUpdate(nextProps: Readonly<IDsmProps>, nextState: Readonly<{}>, nextContext: any): boolean {
        return nextProps.labels !== this.props.labels ||
        nextProps.cells !== this.props.cells ||
        nextProps.stronglyConnectedComponents !== this.props.stronglyConnectedComponents ||
        nextProps.horizontalBoxSize !== this.props.horizontalBoxSize ||
        nextProps.verticalBoxSize !== this.props.verticalBoxSize ||
        nextProps.horizontalSideMarkerHeight !== this.props.horizontalSideMarkerHeight ||
        nextProps.verticalSideMarkerWidth !== this.props.verticalSideMarkerWidth;
    }

    public componentWillReceiveProps(nextProps: IDsmProps) {
        if (nextProps.horizontalSideMarkerHeight !== this.horizontalSideMarkerHeight ||
            nextProps.verticalSideMarkerWidth !== this.verticalSideMarkerWidth) {
            this.horizontalSideMarkerHeight = nextProps.horizontalSideMarkerHeight;
            this.verticalSideMarkerWidth = nextProps.verticalSideMarkerWidth;
        }
    }

    public componentDidMount() {

        if (this.canvasRef && this.markedCellLayerCanvasRef) {

            this.renderingContext = this.canvasRef.getContext("2d")
            this.markedCellLayerrenderingContext = this.markedCellLayerCanvasRef.getContext("2d")


            if (this.markedCellLayerrenderingContext) {

                this.markedCellLayerrenderingContext.canvas.onmousedown = ((event: MouseEvent) => {
                    this.mouseDown = true;
                }).bind(this)

                this.markedCellLayerrenderingContext.canvas.onmouseup = ((event: MouseEvent) => {
                    this.mouseDown = false;

                    if (!this.horizontalResize && ! this.verticalResize) {
                        const position = this.computePosition(event.offsetX, event.offsetY);
                        this.newlySelectedX = position[0];
                        this.newlySelectedY = position[1];
                        requestAnimationFrame(this.updateMarkedLayer);
                        if (this.props.onSelect) {
                            const selection : IDsmSelection | undefined =
                                this.newlySelectedX !== undefined && this.newlySelectedY !== undefined ?
                            {
                                selectedCell: this.matrixElements[this.newlySelectedX][this.newlySelectedY],
                                sourceLabel: this.props.labels[this.newlySelectedY],
                                targetLabel: this.props.labels[this.newlySelectedX]
                            } :
                                    undefined;
                            this.props.onSelect(selection);
                        }
                    }
                }).bind(this)

                this.markedCellLayerrenderingContext.canvas.onmouseenter = ((event: MouseEvent) => {
                    this.mouseOver = true;
                    requestAnimationFrame(this.updateMarkedLayer);
                }).bind(this)

                this.markedCellLayerrenderingContext.canvas.onmouseleave = ((event: MouseEvent) => {
                    this.mouseDown = false;
                    this.mouseOver = false;
                    this.newlyMarkedX = undefined;
                    this.newlyMarkedY = undefined;
                    requestAnimationFrame(this.updateMarkedLayer);
                    if (this.props.onHover) {
                        this.props.onHover(undefined);
                    }
                }).bind(this)

                this.markedCellLayerrenderingContext.canvas.onmousemove = ((event: MouseEvent) => {

                    // handle the resize dragging...
                    if (this.mouseDown && (this.verticalResize || this.horizontalResize)) {

                        if (this.verticalResize && event.offsetX !== this.verticalSideMarkerWidth &&
                            this.horizontalResize && event.offsetY !== this.horizontalSideMarkerHeight) {
                            this.horizontalSideMarkerHeight = event.offsetY;
                            this.verticalSideMarkerWidth = event.offsetX;
                            requestAnimationFrame(this.draw);
                            if (this.props.onSideMarkerResize) {
                                this.props.onSideMarkerResize(event.offsetX, event.offsetY);
                            }
                        } else if (this.verticalResize && event.offsetX !== this.verticalSideMarkerWidth) {
                            this.horizontalSideMarkerHeight = this.horizontalSideMarkerHeight;
                            this.verticalSideMarkerWidth = event.offsetX;
                            requestAnimationFrame(this.draw);
                            if (this.props.onSideMarkerResize) {
                                this.props.onSideMarkerResize(event.offsetX, this.horizontalSideMarkerHeight);
                            }
                        } else if (this.horizontalResize && event.offsetY !== this.horizontalSideMarkerHeight) {
                            this.horizontalSideMarkerHeight = event.offsetY;
                            this.verticalSideMarkerWidth = this.verticalSideMarkerWidth;
                            requestAnimationFrame(this.draw);
                            if (this.props.onSideMarkerResize) {
                                this.props.onSideMarkerResize(this.verticalSideMarkerWidth, event.offsetY);
                            }
                        }
                    }
                    else {

                        // check if we are in a 'resize' area
                        this.verticalResize = event.offsetX > this.verticalSideMarkerWidth - 2 * this.SEP_SIZE &&
                            event.offsetX < this.verticalSideMarkerWidth + this.SEP_SIZE;

                        this.horizontalResize = event.offsetY > this.horizontalSideMarkerHeight - 2 * this.SEP_SIZE &&
                            event.offsetY < this.horizontalSideMarkerHeight + this.SEP_SIZE;

                        // change the cursor
                        if (this.markedCellLayerrenderingContext) {
                            if (this.verticalResize && this.horizontalResize) {
                                this.markedCellLayerrenderingContext.canvas.style.cursor = "nwse-resize";
                            } else if (this.horizontalResize) {
                                this.markedCellLayerrenderingContext.canvas.style.cursor = "ns-resize";
                            } else if (this.verticalResize) {
                                this.markedCellLayerrenderingContext.canvas.style.cursor = "ew-resize";
                            } else {
                                this.markedCellLayerrenderingContext.canvas.style.cursor = "initial";
                            }
                        }

                        const position = this.computePosition(event.offsetX, event.offsetY);

                        if (this.currentlyMarkedX !== position[0] || this.currentlyMarkedY !== position[1]) {
                            this.newlyMarkedX = position[0];
                            this.newlyMarkedY = position[1];

                            if (this.props.onHover) {
                                const selection : IDsmSelection | undefined =
                                    this.newlyMarkedX !== undefined && this.newlyMarkedY !== undefined ?
                                        {
                                            selectedCell: this.matrixElements[this.newlyMarkedX][this.newlyMarkedY],
                                            sourceLabel: this.props.labels[this.newlyMarkedY],
                                            targetLabel: this.props.labels[this.newlyMarkedX]
                                        } :
                                        undefined;
                                this.props.onHover(selection);
                            }
                        }
                    }
                }).bind(this)
            }
            this.draw();
        }
    }

    public componentDidUpdate() {
        this.draw();
    }

    public render() {
        // tslint:disable-next-line:no-console
        console.log("RENDER!!")
        return (
            <div id="stage">
                <canvas id="markedCellLayer" ref={ref => (this.markedCellLayerCanvasRef = ref)} />
                <canvas id="main" ref={ref => (this.canvasRef = ref)} />
            </div>
        );
    }

    private computePosition = (offsetX: number, offsetY: number) : [number | undefined, number | undefined] => {

        let x: number | undefined =
            Math.floor((offsetX - this.verticalSideMarkerWidth) / this.getBoxSize().getHorizontalBoxSize())

        let y: number | undefined =
            Math.floor((offsetY - this.horizontalSideMarkerHeight) / this.getBoxSize().getVerticalBoxSize())

        if (x < 0 || x >= this.props.labels.length) { x = undefined }
        if (y < 0 || y >= this.props.labels.length) { y = undefined }

        return [x, y];
    }

    private draw = () => {

        if (this.canvasRef && this.renderingContext && this.markedCellLayerCanvasRef && this.markedCellLayerrenderingContext) {

            const horizontalSliceSize = this.getHorizontalSliceSize;
            const verticalSliceSize = this.getVerticalSliceSize;

            const itemCount = this.props.labels.length
            const width = horizontalSliceSize(itemCount) + this.verticalSideMarkerWidth + 2;
            const height = verticalSliceSize(itemCount) + this.horizontalSideMarkerHeight + 2;

            //
            this.renderingContext.canvas.width = width;
            this.renderingContext.canvas.height = height;
            this.markedCellLayerrenderingContext.canvas.width = width;
            this.markedCellLayerrenderingContext.canvas.height = height;

            //
            this.setupCanvas();

            // create structures
            this.sccNodePositions = [].concat.apply([], this.props.stronglyConnectedComponents.map(scc => scc.nodePositions));
            this.matrixElements = new Array(this.props.labels.length);
            for (let index = 0; index < this.matrixElements.length; index++) {
                this.matrixElements[index] = new Array(this.props.labels.length);
            }
            this.props.cells.forEach(cell => {
                this.matrixElements[cell.column][cell.row] = cell;
            });

            // draw the horizontal bar
            for (let i = 0; i < this.props.labels.length; i++) {
                this.drawHorizontalBar(i, this.renderingContext, false);
            }
            // draw the vertical bar
            for (let i = 0; i < this.props.labels.length; i++) {
                this.drawVerticalBar(i, this.renderingContext, false);
            }
            // this.drawMVerticalBar(this.renderingContext, width, height, this.sccNodePositions);
            this.drawMatrix(this.renderingContext, width, height);
        }
    }

    private drawMatrix = (renderingContext2D: CanvasRenderingContext2D, width: number, height: number) => {

        const horizontalSliceSize = this.getHorizontalSliceSize;
        const verticalSliceSize = this.getVerticalSliceSize;

        // draw the background for the complete matrix
        renderingContext2D.fillStyle = this.colorScheme.getMatrixBackgroundColor();
        renderingContext2D.fillRect(this.verticalSideMarkerWidth, this.horizontalSideMarkerHeight, horizontalSliceSize(this.props.labels.length), verticalSliceSize(this.props.labels.length));

        // draw the diagonal
        renderingContext2D.fillStyle = this.colorScheme.getMatrixDiagonalColor();
        for (let index = 0; index < this.props.labels.length; index++) {
            renderingContext2D.fillRect(this.verticalSideMarkerWidth + horizontalSliceSize(index), this.horizontalSideMarkerHeight + verticalSliceSize(index),
                horizontalSliceSize(index + 1) - horizontalSliceSize(index),
                verticalSliceSize(index + 1) - verticalSliceSize(index));
        }

        // draw the strongly connected components
        renderingContext2D.fillStyle = this.colorScheme.getCycleSideMarkerColor();
        this.props.stronglyConnectedComponents.forEach(cycle => {

            // extract the node positions
            const nodePositions = cycle.nodePositions;

            renderingContext2D.fillRect(this.verticalSideMarkerWidth + horizontalSliceSize(nodePositions[0]), this.horizontalSideMarkerHeight + verticalSliceSize(nodePositions[0]),
                horizontalSliceSize(nodePositions.length), verticalSliceSize(nodePositions.length));

            renderingContext2D.fillStyle = this.colorScheme.getCycleMatrixDiagonalColor();
            for (const position of nodePositions) {
                renderingContext2D.fillRect(this.verticalSideMarkerWidth + horizontalSliceSize(position), this.horizontalSideMarkerHeight + verticalSliceSize(position),
                    horizontalSliceSize(position + 1) - horizontalSliceSize(position),
                    verticalSliceSize(position + 1) - verticalSliceSize(position));
            }
        });

        // draw the text
        renderingContext2D.fillStyle = this.colorScheme.getMatrixTextColor();
        renderingContext2D.font = this.FONT;
        this.props.cells.forEach(item => {
            if (item.row !== item.column) {
                if (item.value) {
                    renderingContext2D.textAlign = "center";
                    renderingContext2D.textBaseline = "middle";
                    renderingContext2D.fillText('' + item.value,
                        this.verticalSideMarkerWidth + horizontalSliceSize(item.row) + this.getBoxSize().getHorizontalBoxSize() / 2,
                        this.horizontalSideMarkerHeight + verticalSliceSize(item.column) + this.getBoxSize().getVerticalBoxSize() / 2);
                }
            }
        });

        // draw the separator lines
        renderingContext2D.strokeStyle = this.colorScheme.getMatrixSeparatorColor();
        renderingContext2D.beginPath();
        for (let index = 0; index <= this.props.labels.length; index++) {

            renderingContext2D.moveTo(this.verticalSideMarkerWidth, this.horizontalSideMarkerHeight + verticalSliceSize(index));
            renderingContext2D.lineTo(this.verticalSideMarkerWidth + this.getBoxSize().getHorizontalBoxSize() * this.props.labels.length, this.horizontalSideMarkerHeight + verticalSliceSize(index));

            renderingContext2D.moveTo(this.verticalSideMarkerWidth + horizontalSliceSize(index), this.horizontalSideMarkerHeight);
            renderingContext2D.lineTo(this.verticalSideMarkerWidth + horizontalSliceSize(index), this.horizontalSideMarkerHeight + this.getBoxSize().getVerticalBoxSize() * this.props.labels.length);
        }
        renderingContext2D.stroke();

        // draw the cycle separator lines
        renderingContext2D.strokeStyle = this.colorScheme.getCycleSideMarkerSeparatorColor();
        renderingContext2D.beginPath();

        this.props.stronglyConnectedComponents.forEach(cycle => {

            // tslint:disable-next-line:prefer-for-of
            for (let index = 1; index < cycle.nodePositions.length; index++) {

                renderingContext2D.moveTo(this.verticalSideMarkerWidth + horizontalSliceSize(cycle.nodePositions[index]),
                    this.horizontalSideMarkerHeight + verticalSliceSize(cycle.nodePositions[0]));
                renderingContext2D.lineTo(this.verticalSideMarkerWidth + horizontalSliceSize(cycle.nodePositions[index]),
                    this.horizontalSideMarkerHeight + verticalSliceSize(cycle.nodePositions[cycle.nodePositions.length - 1] + 1));

                renderingContext2D.moveTo(this.verticalSideMarkerWidth + horizontalSliceSize(cycle.nodePositions[0]),
                    this.horizontalSideMarkerHeight + verticalSliceSize(cycle.nodePositions[index]));
                renderingContext2D.lineTo(this.verticalSideMarkerWidth + horizontalSliceSize(cycle.nodePositions[cycle.nodePositions.length - 1] + 1),
                    this.horizontalSideMarkerHeight + verticalSliceSize(cycle.nodePositions[index]));
            }

            renderingContext2D.stroke();
        });

        this.updateMarkedLayer();
    }

    private updateMarkedLayer = () => {

        // only redraw on change
        if (this.currentlyMarkedX !== this.newlyMarkedX || this.currentlyMarkedY !== this.newlyMarkedY ||
            this.currentlySelectedX !== this.newlySelectedX || this.currentlySelectedY !== this.newlySelectedY) {

            //
            if (this.markedCellLayerCanvasRef && this.markedCellLayerrenderingContext) {

                // clear rect
                if (this.currentlyMarkedX !== undefined && this.currentlyMarkedY !== undefined) {

                    // Store the current transformation matrix
                    this.markedCellLayerrenderingContext.save();

                    // Use the identity matrix while clearing the canvas
                    this.markedCellLayerrenderingContext.setTransform(1, 0, 0, 1, 0, 0);
                    this.markedCellLayerrenderingContext.clearRect(0, 0, this.markedCellLayerrenderingContext.canvas.width, this.markedCellLayerrenderingContext.canvas.height);

                    // Restore the transform
                    this.markedCellLayerrenderingContext.restore();
                }

                //
                this.currentlyMarkedX = this.newlyMarkedX;
                this.currentlyMarkedY = this.newlyMarkedY;
                this.currentlySelectedX = this.newlySelectedX;
                this.currentlySelectedY = this.newlySelectedY;

                //
                if (this.currentlyMarkedX !== undefined && this.currentlyMarkedY !== undefined) {

                    this.markCell(this.currentlyMarkedX, this.currentlyMarkedY, false);
                    this.markCell(this.currentlyMarkedY, this.currentlyMarkedX, false);

                    // mark vertical bar
                    this.drawVerticalBar(this.currentlyMarkedY, this.markedCellLayerrenderingContext, true);

                    // mark horizontal bar
                    this.drawHorizontalBar(this.currentlyMarkedX, this.markedCellLayerrenderingContext, true);
                }
            }
        }

        //
        if (this.currentlySelectedX !== undefined && this.currentlySelectedY !== undefined) {
            this.markCell(this.currentlySelectedX, this.currentlySelectedY, true);
        }

        if (this.mouseOver) {
            requestAnimationFrame(this.updateMarkedLayer);
        }
    }

    private drawVerticalBar = (y: number, renderingContext: CanvasRenderingContext2D, mark: boolean) => {

        renderingContext.save();

        const isInCycle = this.isLabelInCycle(y);

        // step 1: fill the rect
        if (mark) {
            renderingContext.fillStyle = isInCycle ? this.colorScheme.getCycleSideMarkerMarkedColor() : this.colorScheme.getSideMarkerMarkedColor();
        } else {
            renderingContext.fillStyle = isInCycle ? this.colorScheme.getCycleSideMarkerColor() : this.colorScheme.getSideMarkerBackgroundColor();
        }

        renderingContext.fillRect(
            0,
            this.horizontalSideMarkerHeight + this.getVerticalSliceSize(y),
            this.verticalSideMarkerWidth - this.SEP_SIZE,
            this.getVerticalSliceSize(y + 1) - this.getVerticalSliceSize(y));

        // step 2: separators   
        renderingContext.strokeStyle = isInCycle ? this.colorScheme.getCycleSideMarkerSeparatorColor() : this.colorScheme.getSideMarkerSeparatorColor();
        renderingContext.beginPath();
        renderingContext.moveTo(0, this.horizontalSideMarkerHeight + this.getVerticalSliceSize(y));
        renderingContext.lineTo(this.verticalSideMarkerWidth - this.SEP_SIZE, this.horizontalSideMarkerHeight + this.getVerticalSliceSize(y));
        renderingContext.moveTo(this.verticalSideMarkerWidth - this.SEP_SIZE, this.horizontalSideMarkerHeight + this.getVerticalSliceSize(y));
        renderingContext.lineTo(this.verticalSideMarkerWidth - this.SEP_SIZE, this.horizontalSideMarkerHeight + this.getVerticalSliceSize(y + 1));
        if (y === this.props.labels.length - 1) {
            renderingContext.moveTo(0, this.horizontalSideMarkerHeight + this.getVerticalSliceSize(this.props.labels.length));
            renderingContext.lineTo(this.verticalSideMarkerWidth - this.SEP_SIZE, this.horizontalSideMarkerHeight + this.getVerticalSliceSize(this.props.labels.length));
        }
        renderingContext.stroke();

        // step 3: re-draw the text
        // ...set the clipping area
        renderingContext.beginPath();
        renderingContext.rect(
            0,
            this.horizontalSideMarkerHeight + this.getVerticalSliceSize(y),
            this.verticalSideMarkerWidth - (this.SEP_SIZE + this.TEXT_CLIP_PADDING),
            this.getVerticalSliceSize(y + 1) - this.getVerticalSliceSize(y));
        renderingContext.clip();

        // ...draw rotated text
        renderingContext.fillStyle = this.colorScheme.getSideMarkerTextColor();
        renderingContext.font = this.FONT;
        renderingContext.textAlign = "left";
        renderingContext.textBaseline = "middle";
        renderingContext.fillText(this.props.labels[y].text,
            10,
            this.horizontalSideMarkerHeight + this.getVerticalSliceSize(y) + this.getBoxSize().getVerticalBoxSize() / 2);

        renderingContext.restore();
    }

    private drawHorizontalBar = (x: number, renderingContext: CanvasRenderingContext2D, mark: boolean) => {

        renderingContext.save();

        const isInCycle = this.isLabelInCycle(x);

        // step 1: fill the rect
        if (mark) {
            renderingContext.fillStyle = isInCycle ? this.colorScheme.getCycleSideMarkerMarkedColor() : this.colorScheme.getSideMarkerMarkedColor();
        } else {
            renderingContext.fillStyle = isInCycle ? this.colorScheme.getCycleSideMarkerColor() : this.colorScheme.getSideMarkerBackgroundColor();
        }

        renderingContext.fillRect(
            this.verticalSideMarkerWidth + this.getHorizontalSliceSize(x),
            0,
            this.getHorizontalSliceSize(x + 1) - this.getHorizontalSliceSize(x),
            this.horizontalSideMarkerHeight - this.SEP_SIZE);

        // step 2: separators
        renderingContext.strokeStyle = isInCycle ? this.colorScheme.getCycleSideMarkerSeparatorColor() : this.colorScheme.getSideMarkerSeparatorColor();
        renderingContext.beginPath();
        renderingContext.moveTo(this.verticalSideMarkerWidth + this.getHorizontalSliceSize(x), 0);
        renderingContext.lineTo(this.verticalSideMarkerWidth + this.getHorizontalSliceSize(x), this.horizontalSideMarkerHeight - this.SEP_SIZE);
        renderingContext.moveTo(this.verticalSideMarkerWidth + this.getHorizontalSliceSize(x), this.horizontalSideMarkerHeight - this.SEP_SIZE);
        renderingContext.lineTo(this.verticalSideMarkerWidth + this.getHorizontalSliceSize(x + 1), this.horizontalSideMarkerHeight - this.SEP_SIZE);
        if (x === this.props.labels.length - 1) {
            renderingContext.moveTo(this.verticalSideMarkerWidth + this.getHorizontalSliceSize(this.props.labels.length), 0);
            renderingContext.lineTo(this.verticalSideMarkerWidth + this.getHorizontalSliceSize(this.props.labels.length), this.horizontalSideMarkerHeight - this.SEP_SIZE);
        }
        renderingContext.stroke();

        // step 2: re-draw the text
        // ...set the clipping area
        renderingContext.beginPath();
        renderingContext.rect(this.verticalSideMarkerWidth + this.getHorizontalSliceSize(x), 0,
            this.getHorizontalSliceSize(x + 1) - this.getHorizontalSliceSize(x), this.horizontalSideMarkerHeight - (this.SEP_SIZE + this.TEXT_CLIP_PADDING));
        renderingContext.clip();

        // ...draw rotated text
        renderingContext.translate(this.verticalSideMarkerWidth + this.getHorizontalSliceSize(x) + this.getBoxSize().getHorizontalBoxSize() / 2, 10);
        renderingContext.rotate(1 * Math.PI / 2);
        renderingContext.fillStyle = this.colorScheme.getSideMarkerTextColor();
        renderingContext.font = this.FONT;
        renderingContext.textAlign = "left";
        renderingContext.textBaseline = "middle";
        renderingContext.fillText(this.props.labels[x].text, 0, 0);

        renderingContext.restore();
    }

    private markCell = (x: number, y: number, selected: boolean) => {

        if (this.markedCellLayerrenderingContext) {

            this.markedCellLayerrenderingContext.save();

            this.markedCellLayerrenderingContext.strokeStyle = selected ?
                this.colorScheme.getSelectedCellColor() :
                this.isCellInCycle(x, y) ? this.colorScheme.getCycleMatrixMarkedCellColor() : this.colorScheme.getMatrixMarkedCellColor();
            this.markedCellLayerrenderingContext.lineWidth = 3;
            this.markedCellLayerrenderingContext.strokeRect(
                this.verticalSideMarkerWidth + this.getHorizontalSliceSize(x) + 1,
                this.horizontalSideMarkerHeight + this.getVerticalSliceSize(y) + 1,
                this.getBoxSize().getHorizontalBoxSize() - 2,
                this.getBoxSize().getVerticalBoxSize() - 2);

            this.markedCellLayerrenderingContext.restore();
        }
    }

    private setupCanvas = () => {
        if (this.canvasRef && this.renderingContext) {
            setupCanvas(this.canvasRef, this.renderingContext);
        }
        if (this.markedCellLayerCanvasRef && this.markedCellLayerrenderingContext) {
            setupCanvas(this.markedCellLayerCanvasRef, this.markedCellLayerrenderingContext);
        }
    }

    private getHorizontalSliceSize = (count: number) => {
        return this.getBoxSize().getHorizontalBoxSize() * count;
    }

    private getVerticalSliceSize = (count: number) => {
        return this.getBoxSize().getVerticalBoxSize() * count;
    }

    private getBoxSize = () => {
        return {
            getHorizontalBoxSize: () => this.props.horizontalBoxSize ? this.props.horizontalBoxSize : 25,
            getVerticalBoxSize: () => this.props.verticalBoxSize ? this.props.verticalBoxSize : 25
        };
    }

    private isLabelInCycle = (index: number) => {

        if (this.sccNodePositions === undefined || this.sccNodePositions === null) {
            this.sccNodePositions = [].concat.apply([], this.props.stronglyConnectedComponents.map(scc => scc.nodePositions));
        }

        return this.sccNodePositions.includes(index);
    }

    private isCellInCycle = (x: number, y: number) => {

        if (this.props.stronglyConnectedComponents) {
            for (const scc of this.props.stronglyConnectedComponents) {
                if (scc.nodePositions.includes(x) && scc.nodePositions.includes(y)) {
                    return true;
                }
            }
        }

        return false;
    }
}