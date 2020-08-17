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
import {ElkExtendedEdge, ElkNode, ElkPoint} from 'elkjs/lib/elk.bundled.js'
import * as React from "react";
import {setupCanvas} from "../dsm/DpiFixer";
import './DependencyGraph.css';
import { IDependencyGraphCanvasProps } from './IDependencyGraphCanvasProps';

export class DependencyGraphCanvas extends React.Component<IDependencyGraphCanvasProps, {}> {

    private readonly DEFAULT_SCALE = 1.0;

    private readonly CLICKABLE_EDGE_RANGE = 6;
    private readonly CORNER_RADIUS = 3;
    private readonly NODE_HEIGHT = 25;
    private readonly NODE_PADDING = 10;

    private readonly TEXT_FONT = "12px Arial";

    private containerRef: HTMLDivElement | null;

    private canvasRef: HTMLCanvasElement | null;
    private renderingContext: CanvasRenderingContext2D | null;

    private markedDependencyLayerCanvasRef: HTMLCanvasElement | null;
    private markedDependencyLayerRenderingContext: CanvasRenderingContext2D | null;

    private newlyMarkedX: number | undefined;
    private newlyMarkedY: number | undefined;

    private selectedEdgeId?: string ;

    constructor(props: IDependencyGraphCanvasProps) {
        super(props);
    }

    public componentDidMount(): void {

        if (this.canvasRef && this.markedDependencyLayerCanvasRef) {
            this.renderingContext = this.canvasRef.getContext("2d");
            this.markedDependencyLayerRenderingContext = this.markedDependencyLayerCanvasRef.getContext("2d");
            if (this.renderingContext) {
                this.renderingContext.canvas.onclick = ((event: MouseEvent) => {
                    this.newlyMarkedX = event.offsetX;
                    this.newlyMarkedY = event.offsetY;
                    this.updateMarkerLayer();
                }).bind(this)
            }
            this.draw();
        }
    }

    public render() {
        return <div className="dsm-canvas-container" ref={ref => (this.containerRef = ref)}>
            <canvas id="markedDependencyLayer" ref={ref => (this.markedDependencyLayerCanvasRef = ref)}/>
            <canvas id="main" ref={ref => (this.canvasRef = ref)}/>
        </div>
    }

    private scale = () => {
        return this.DEFAULT_SCALE;
    }

    private draw = () => {

        if (this.containerRef &&
            this.canvasRef && this.renderingContext &&
            this.markedDependencyLayerCanvasRef && this.markedDependencyLayerRenderingContext &&
            this.props.rootNode && this.props.rootNode.width && this.props.rootNode.height) {

            const renderingContext = this.renderingContext;

            // setup the canvas
            setupCanvas(this.canvasRef, this.renderingContext, this.props.rootNode.width, this.props.rootNode.height, this.scale());
            setupCanvas(this.markedDependencyLayerCanvasRef, this.markedDependencyLayerRenderingContext, this.props.rootNode.width, this.props.rootNode.height, this.scale());

            // we have to "fix" the coordinates for edges
            this.postProcessLayout();

            // draw the nodes
            if (this.props.rootNode.children) {
                this.props.rootNode.children.forEach(node => {
                        this.drawNode(renderingContext, node);
                    }
                )
            }

            // draw the edges
            if (this.props.rootNode.edges) {
                this.props.rootNode.edges.forEach(edge => {
                        this.drawEdge(renderingContext, edge as ElkExtendedEdge,);
                    }
                )
            }
        }
    }

    private postProcessLayout = () => {

        if (this.props.rootNode) {

            //
            const parentMap: Map<string, ElkNode | undefined> = new Map();

            if (this.props.rootNode.children) {

                // fix the node layout
                this.props.rootNode.children.forEach(node => {

                        parentMap.set(node.id, undefined);

                        const offsetX = node.x !== undefined ? node.x : 0;
                        const offsetY = node.y !== undefined ? node.y : 0;

                        if (node.children) {
                            node.children.forEach(n => {

                                parentMap.set(n.id, node);

                                n.x = n.x !== undefined ? n.x + offsetX : offsetX;
                                n.y = n.y !== undefined ? n.y + offsetY : offsetY;
                            });
                        }
                    }
                )
            }

            // fix the edges
            if (this.props.rootNode.edges) {

                this.props.rootNode.edges.forEach(edge => {

                    const extendedEdge: ElkExtendedEdge = edge as ElkExtendedEdge;

                    const sourceParent = parentMap.get(extendedEdge.sources[0]);
                    const targetParent = parentMap.get(extendedEdge.targets[0]);
                    const offsetX = sourceParent !== undefined && targetParent !== undefined && sourceParent.id === targetParent.id && sourceParent.x !== undefined ? sourceParent.x : 0;
                    const offsetY = sourceParent !== undefined && targetParent !== undefined && sourceParent.id === targetParent.id && sourceParent.y !== undefined ? sourceParent.y : 0;

                    if (extendedEdge.labels) {
                        extendedEdge.labels.forEach(label => {
                                if (label.x !== undefined && label.y !== undefined) {
                                    label.x = label.x + offsetX;
                                    label.y = label.y + offsetY;
                                }
                            }
                        )
                    }

                    // fix the sections
                    extendedEdge.sections.forEach(section => {

                        section.startPoint.x = section.startPoint.x + offsetX;
                        section.startPoint.y = section.startPoint.y + offsetY;

                        if (section.bendPoints) {
                            section.bendPoints.forEach(bendPoint => {
                                bendPoint.x = bendPoint.x + offsetX;
                                bendPoint.y = bendPoint.y + offsetY;
                            })
                        }

                        section.endPoint.x = section.endPoint.x + offsetX;
                        section.endPoint.y = section.endPoint.y + offsetY;
                    })
                });
            }
        }
    }

    private updateMarkerLayer = () => {

        if (this.newlyMarkedX && this.newlyMarkedY) {

            const newlySelected = this.computeSelectedEdge(this.newlyMarkedX / this.scale(), this.newlyMarkedY /  this.scale());

            // draw
            if (this.markedDependencyLayerCanvasRef && this.markedDependencyLayerRenderingContext) {

                // clear the canvas
                this.markedDependencyLayerRenderingContext.clearRect(0, 0, this.markedDependencyLayerRenderingContext.canvas.width, this.markedDependencyLayerRenderingContext.canvas.height)

                if (newlySelected !== undefined) {
                    this.markedDependencyLayerRenderingContext.lineWidth = 2;
                    this.drawEdge(this.markedDependencyLayerRenderingContext, newlySelected)
                }
            }

            this.selectedEdgeId = newlySelected?.id;
            if (this.props.onEdgeSelected) {
                this.props.onEdgeSelected(this.selectedEdgeId);
            }
        }
    }

    private drawNode = (context: CanvasRenderingContext2D, node: ElkNode) => {

        if (node.x && node.y && node.width && node.height) {

            context.save();
            context.font = this.TEXT_FONT;

            if (node.children) {

                context.fillStyle = "rgba(255, 0, 0, 0.1)";

                this.roundRect(
                    context,
                    node.x,
                    node.y,
                    node.width,
                    node.height,
                    this.CORNER_RADIUS,
                    true,
                    false
                );
            } else {

                context.fillStyle = "rgba(255, 255, 255, 1.0)";

                this.roundRect(
                    context,
                    node.x,
                    node.y,
                    node.width,
                    node.height,
                    this.CORNER_RADIUS,
                    true,
                    true
                );

                // ...set the clipping area
                context.beginPath();
                context.rect(node.x,
                    node.y,
                    node.width - this.NODE_PADDING,
                    node.height,);
                context.clip();

                const nodeLabel = node.labels && node.labels[0] ? node.labels[0].text : node.id;
                context.fillStyle = "rgba(0, 0, 0, 1.0)";
                context.textAlign = "left";
                context.textBaseline = "middle";
                context.fillText(nodeLabel, node.x + this.NODE_PADDING, node.y + (this.NODE_HEIGHT / 2));
            }
            context.restore();

            if (node.children) {
                node.children.forEach(n => {
                    this.drawNode(context, n);
                });
            }
        }
    }


    private drawEdge = (context: CanvasRenderingContext2D, extendedEdge: ElkExtendedEdge) => {

        context.save();
        context.font ="9px Arial"
        context.setLineDash([3, 2]);

        if (extendedEdge.labels && extendedEdge.labels[0]) {
            const label = extendedEdge.labels[0];
            if (label.x !== undefined && label.y !== undefined) {
                context.fillText(label.text, label.x, label.y);
            }
        }

        extendedEdge.sections.forEach(section => {

            context.strokeStyle = section.startPoint.y > section.endPoint.y ? "#FF0000" : "#000000";
            context.fillStyle = section.startPoint.y > section.endPoint.y ? "#FF0000" : "#000000";

            let lastPoint: ElkPoint = section.startPoint;

            //
            if (section.bendPoints) {

                // tslint:disable-next-line:prefer-for-of
                for (let i = 0; i < section.bendPoints.length; i++) {

                    const currentPoint: ElkPoint = section.bendPoints[i];
                    const nextPoint = i < section.bendPoints.length - 1 ? section.bendPoints[i + 1] : section.endPoint;

                    const lastDeltaX = currentPoint.x - lastPoint.x;
                    const lastDeltaY = currentPoint.y - lastPoint.y;
                    const nextDeltaX = nextPoint.x - currentPoint.x;
                    const nextDeltaY = nextPoint.y - currentPoint.y;

                    context.beginPath();
                    context.moveTo(lastPoint.x, lastPoint.y);
                    lastPoint = {x : currentPoint.x, y : currentPoint.y};
                    if (lastDeltaX !== 0) {
                        context.lineTo(lastDeltaX > 0 ? currentPoint.x - this.CORNER_RADIUS : currentPoint.x + this.CORNER_RADIUS, currentPoint.y);
                        context.quadraticCurveTo(currentPoint.x, currentPoint.y, currentPoint.x, nextDeltaY < 0 ? currentPoint.y - this.CORNER_RADIUS : currentPoint.y + this.CORNER_RADIUS);
                        lastPoint.y = nextDeltaY < 0 ? currentPoint.y - this.CORNER_RADIUS : currentPoint.y + this.CORNER_RADIUS;
                    } else if (lastDeltaY !== 0) {
                        context.lineTo(currentPoint.x, lastDeltaY > 0 ? currentPoint.y - this.CORNER_RADIUS : currentPoint.y + this.CORNER_RADIUS);
                        context.quadraticCurveTo(currentPoint.x, currentPoint.y, nextDeltaX < 0 ? currentPoint.x - this.CORNER_RADIUS : currentPoint.x + this.CORNER_RADIUS, currentPoint.y);
                        lastPoint.x = nextDeltaX < 0 ? currentPoint.x - this.CORNER_RADIUS : currentPoint.x + this.CORNER_RADIUS;
                    }
                    context.stroke();
                }
            }

            const endpointYOffset = section.endPoint.y > section.startPoint.y ? -5 : +5;
            context.beginPath();
            context.moveTo(lastPoint.x, lastPoint.y);
            context.lineTo(section.endPoint.x, section.endPoint.y + endpointYOffset);
            // context.closePath();
            context.stroke();


            this.drawArrowhead(context, lastPoint, section.endPoint, 4);
        })

        context.restore();
    }

    /**
     * Draw an arrowhead on a line on an HTML5 canvas.
     *
     * Based almost entirely off of http://stackoverflow.com/a/36805543/281460 with some modifications
     * for readability and ease of use.
     *
     * @param context The drawing context on which to put the arrowhead.
     * @param from A point, specified as an object with 'x' and 'y' properties, where the arrow starts
     *             (not the arrowhead, the arrow itself).
     * @param to   A point, specified as an object with 'x' and 'y' properties, where the arrow ends
     *             (not the arrowhead, the arrow itself).
     * @param radius The radius of the arrowhead. This controls how "thick" the arrowhead looks.
     */
    private drawArrowhead = (context: CanvasRenderingContext2D, from: ElkPoint, to: ElkPoint, radius: number) => {

        const xDelta = from.x - to.x;
        const yDelta = from.y - to.y;

        const xCenter = xDelta !== 0 ? (xDelta > 0 ? to.x + 5 : to.x - 5) : to.x;
        const yCenter = yDelta !== 0 ? (yDelta > 0 ? to.y + 5 : to.y - 5) : to.y;

        let angle;
        let x;
        let y;

        context.beginPath();

        angle = Math.atan2(to.y - from.y, to.x - from.x)
        x = radius * Math.cos(angle) + xCenter;
        y = radius * Math.sin(angle) + yCenter;

        context.moveTo(x, y);

        angle += (1.0 / 3.0) * (2 * Math.PI)
        x = radius * Math.cos(angle) + xCenter;
        y = radius * Math.sin(angle) + yCenter;

        context.lineTo(x, y);

        angle += (1.0 / 3.0) * (2 * Math.PI)
        x = radius * Math.cos(angle) + xCenter;
        y = radius * Math.sin(angle) + yCenter;

        context.lineTo(x, y);

        context.closePath();

        context.fill();
    }

    private roundRect = (context: CanvasRenderingContext2D, x: number, y: number, w: number, h: number, radius: number, fill: boolean, stroke: boolean) => {

        const r = x + w;
        const b = y + h;

        context.beginPath();
        context.moveTo(x + radius, y);
        context.lineTo(r - radius, y);
        context.quadraticCurveTo(r, y, r, y + radius);
        context.lineTo(r, y + h - radius);
        context.quadraticCurveTo(r, b, r - radius, b);
        context.lineTo(x + radius, b);
        context.quadraticCurveTo(x, b, x, b - radius);
        context.lineTo(x, y + radius);
        context.quadraticCurveTo(x, y, x + radius, y);
        context.closePath();

        if (fill) {
            context.fill();
        }
        if (stroke) {
            context.stroke();
        }
    }

    private computeSelectedEdge = (pointX: number, pointY: number): ElkExtendedEdge | undefined => {

        if (this.props.rootNode && this.props.rootNode.edges) {
            for (const edge of this.props.rootNode.edges) {
                const extendedEdge: ElkExtendedEdge = edge as ElkExtendedEdge;
                for (const section of extendedEdge.sections) {

                    let lastPoint = section.startPoint;
                    if (section.bendPoints) {
                        for (const currentPoint of section.bendPoints) {
                            if (this.isPointOnLine(lastPoint.x, lastPoint.y, currentPoint.x, currentPoint.y, pointX, pointY) === true) {
                                return extendedEdge;
                            }
                            lastPoint = currentPoint;
                        }
                    }

                    if (this.isPointOnLine(lastPoint.x, lastPoint.y, section.endPoint.x, section.endPoint.y, pointX, pointY) === true) {
                        return extendedEdge;
                    }
                }
            }
        }

        return undefined;
    }

    private isPointOnLine = (startX: number, startY: number, endX: number, endY: number, pointX: number, pointY: number) => {
        return (startX < endX ? (startX - this.CLICKABLE_EDGE_RANGE <= pointX && pointX <= endX + this.CLICKABLE_EDGE_RANGE) : (endX - this.CLICKABLE_EDGE_RANGE <= pointX && pointX <= startX + this.CLICKABLE_EDGE_RANGE)) &&
            (startY < endY ? (startY - this.CLICKABLE_EDGE_RANGE <= pointY && pointY <= endY + this.CLICKABLE_EDGE_RANGE) : (endY - this.CLICKABLE_EDGE_RANGE <= pointY && pointY <= startY + this.CLICKABLE_EDGE_RANGE));
    }
}


