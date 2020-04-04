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
import ELK, {ElkExtendedEdge, ElkNode, ElkPoint} from 'elkjs/lib/elk.bundled.js'
import * as React from "react";
import {setupCanvas} from "../dsm/DpiFixer";
import {IDependencyGraphState} from "./IDependencyGraphState";

export class DependencyGraph extends React.Component<any, IDependencyGraphState> {

    private readonly LINE_RANGE = 4;

    private readonly CORNER_RADIUS = 3;
    private readonly NODE_HEIGHT = 30;
    private readonly NODE_WIDTH = 170;

    private canvasRef: HTMLCanvasElement | null;
    private renderingContext: CanvasRenderingContext2D | null;

    private markedDependencyLayerCanvasRef: HTMLCanvasElement | null;
    private markedDependencyLayerRenderingContext: CanvasRenderingContext2D | null;

    private rootNode: ElkNode | null;
    private selectedEdge?: ElkExtendedEdge;

    // private currentlyMarkedX: number | undefined;
    // private currentlyMarkedY: number | undefined;
    private newlyMarkedX: number | undefined;
    private newlyMarkedY: number | undefined;

    private parentMap : Map<string, ElkNode | undefined>;

    constructor(props: any) {
        super(props);

        this.state = {};
        this.parentMap = new Map();
    }

    public componentDidMount(): void {

        if (this.canvasRef && this.markedDependencyLayerCanvasRef) {
            this.renderingContext = this.canvasRef.getContext("2d");
            this.markedDependencyLayerRenderingContext = this.markedDependencyLayerCanvasRef.getContext("2d");

            if (this.renderingContext) {
                this.renderingContext.canvas.onmousedown = ((event: MouseEvent) => {
                    this.newlyMarkedX = event.offsetX;
                    this.newlyMarkedY = event.offsetY;
                    this.updateMarkerLayer();
                }).bind(this)
            }
        }

        const elk = new ELK();
        // tslint:disable:object-literal-sort-keys
        const graph = {
            id: "root",
            layoutOptions: {
                'elk.algorithm': 'layered',
                'org.eclipse.elk.direction': 'DOWN',
                'org.eclipse.elk.layered.spacing.edgeNodeBetweenLayers': '20',
                'org.eclipse.elk.layered.nodePlacement.strategy': 'BRANDES_KOEPF',
                'org.eclipse.elk.hierarchyHandling': 'INCLUDE_CHILDREN',
            },
            children: [
                {id: "n1", width: this.NODE_WIDTH, height: this.NODE_HEIGHT},
                {id: "n2", width: this.NODE_WIDTH, height: this.NODE_HEIGHT},
                {id: "n5", width: this.NODE_WIDTH, height: this.NODE_HEIGHT},
                {
                    id: "virt",
                    children: [
                        {id: "n3", width: this.NODE_WIDTH, height: this.NODE_HEIGHT},
                        {id: "n4", width: this.NODE_WIDTH, height: this.NODE_HEIGHT},
                    ]
                },
                {id: "n6", width: this.NODE_WIDTH, height: this.NODE_HEIGHT},
            ],
            edges: [
                {id: "e1", sources: ["n1"], targets: ["n6"]},
                {id: "e2", sources: ["n2"], targets: ["n5"]},
                {id: "e3", sources: ["n5"], targets: ["n3"]},
                {id: "e4", sources: ["n3"], targets: ["n4"]},
                {id: "e5", sources: ["n4"], targets: ["n3"]},
                {id: "e6", sources: ["n3"], targets: ["n6"]},
                {id: "e7", sources: ["n2"], targets: ["n6"]},
            ]
        }

        elk.layout(graph)
            .then((value) => {
                this.rootNode = value;
                this.draw();
            })
            // tslint:disable-next-line:no-console
            .catch(console.error)
    }

    public render() {
        return <div id="dsm-canvas-container">
            <canvas id="markedDependencyLayer" ref={ref => (this.markedDependencyLayerCanvasRef = ref)}/>
            <canvas id="main" ref={ref => (this.canvasRef = ref)}/>
        </div>
    }

    private draw = () => {

        if (this.canvasRef && this.renderingContext &&
            this.markedDependencyLayerCanvasRef && this.markedDependencyLayerRenderingContext &&
            this.rootNode && this.rootNode.width && this.rootNode.height) {


            const renderingContext = this.renderingContext;
            const markedDependencyLayerRenderingContext = this.markedDependencyLayerRenderingContext;
            const scale = 1.6;

            // tslint:disable-next-line:no-console
            console.log(JSON.stringify(this.rootNode))

            // setup the canvas
            setupCanvas(this.canvasRef, this.renderingContext, this.rootNode.width * scale, this.rootNode.height * scale);
            renderingContext.scale(scale, scale)

            setupCanvas(this.markedDependencyLayerCanvasRef, this.markedDependencyLayerRenderingContext, this.rootNode.width * scale, this.rootNode.height * scale);
            markedDependencyLayerRenderingContext.scale(scale, scale)

            //
            this.parentMap.clear();

            // draw the nodes
            if (this.rootNode.children) {
                this.rootNode.children.forEach(node => {
                        this.parentMap.set(node.id, undefined);
                        this.drawNode(renderingContext, node, 0, 0);
                    }
                )
            }

            // draw the edges
            if (this.rootNode.edges) {
                this.rootNode.edges.forEach(edge => {

                        const extendedEdge: ElkExtendedEdge = edge as ElkExtendedEdge;

                        const sourceParent = this.parentMap.get(extendedEdge.sources[0]);
                        const targetParent = this.parentMap.get(extendedEdge.targets[0]);

                        const offsetX = sourceParent !== undefined && targetParent !== undefined && sourceParent === targetParent && sourceParent.x !== undefined?
                                        sourceParent.x : 0;

                        const offsetY = sourceParent !== undefined && targetParent !== undefined && sourceParent === targetParent && sourceParent.y !== undefined ?
                                        sourceParent.y : 0;

                        extendedEdge.sections.forEach(section => {

                            renderingContext.strokeStyle = section.startPoint.y > section.endPoint.y ? "#FF0000" : "#000000";
                            renderingContext.fillStyle = section.startPoint.y > section.endPoint.y ? "#FF0000" : "#000000";

                            let lastPoint : ElkPoint =  {
                                x: section.startPoint.x + offsetX,
                                y: section.startPoint.y + offsetY
                            };

                            renderingContext.beginPath();
                            renderingContext.moveTo(lastPoint.x, lastPoint.y);

                            //
                            if (section.bendPoints) {

                                // tslint:disable-next-line:prefer-for-of
                                for (let i = 0; i < section.bendPoints.length; i++) {

                                    const currentPoint :  ElkPoint = {
                                        x: section.bendPoints[i].x + offsetX,
                                        y: section.bendPoints[i].y + offsetY
                                    };
                                    const nextPoint = i < section.bendPoints.length - 1 ? section.bendPoints[i + 1] : {
                                        x: section.endPoint.x + offsetX,
                                        y: section.endPoint.y + offsetY
                                    };

                                    const lastDeltaX = currentPoint.x - lastPoint.x;
                                    const lastDeltaY = currentPoint.y - lastPoint.y;
                                    const nextDeltaX = nextPoint.x - currentPoint.x;
                                    const nextDeltaY = nextPoint.y - currentPoint.y;

                                    if (lastDeltaX !== 0) {
                                        renderingContext.lineTo(lastDeltaX > 0 ? currentPoint.x - this.CORNER_RADIUS : currentPoint.x + this.CORNER_RADIUS, currentPoint.y);
                                        renderingContext.quadraticCurveTo(currentPoint.x, currentPoint.y, currentPoint.x, nextDeltaY < 0 ? currentPoint.y - this.CORNER_RADIUS : currentPoint.y + this.CORNER_RADIUS);
                                    } else if (lastDeltaY !== 0) {
                                        renderingContext.lineTo(currentPoint.x, lastDeltaY > 0 ? currentPoint.y - this.CORNER_RADIUS : currentPoint.y + this.CORNER_RADIUS);
                                        renderingContext.quadraticCurveTo(currentPoint.x, currentPoint.y, nextDeltaX < 0 ? currentPoint.x - this.CORNER_RADIUS : currentPoint.x + this.CORNER_RADIUS, currentPoint.y);
                                    }

                                    lastPoint = currentPoint;
                                }
                            }

                            renderingContext.lineTo(section.endPoint.x  + offsetX, section.endPoint.y + offsetY);
                            renderingContext.stroke();

                            this.drawArrowhead(renderingContext, lastPoint, {x: section.endPoint.x  + offsetX, y: section.endPoint.y + offsetY}, 4);
                        })
                    }
                )
            }
        }
    }

    private updateMarkerLayer = () => {
        if (this.newlyMarkedX && this.newlyMarkedY) {
            this.selectedEdge = this.doIt(this.newlyMarkedX / 1.6, this.newlyMarkedY / 1.6);
            // tslint:disable-next-line:no-console
            console.log(this.selectedEdge)
        }
    }

    private drawNode = (context: CanvasRenderingContext2D, node: ElkNode, offsetX: number, offsetY: number) => {

        if (node.x && node.y && node.width && node.height) {

            const nodeX = node.x + offsetX;
            const nodeY = node.y + offsetY;

            context.save();

            if (node.children) {

                context.fillStyle = "rgba(255, 0, 0, 0.1)";

                this.roundRect(
                    context,
                    nodeX,
                    nodeY,
                    node.width,
                    node.height,
                    this.CORNER_RADIUS,
                    true,
                    false
                );
            }
            else {

                context.fillStyle = "rgba(255, 255, 255, 1.0)";

                this.roundRect(
                    context,
                    nodeX,
                    nodeY,
                    node.width,
                    node.height,
                    this.CORNER_RADIUS,
                    true,
                    true
                );

                // ...set the clipping area
                context.beginPath();
                context.rect(nodeX,
                    nodeY,
                    node.width,
                    node.height,);
                context.clip();

                context.fillStyle = "rgba(0, 0, 0, 1.0)";
                context.textAlign = "left";
                context.textBaseline = "middle";
                context.fillText("i.c.s.h." + node.id, nodeX + (this.NODE_HEIGHT / 2), nodeY + (this.NODE_HEIGHT / 2));
            }
            context.restore();

            if (node.children) {
                node.children.forEach(n => {
                    this.parentMap.set(n.id, node);
                    this.drawNode(context, n, nodeX, nodeY);
                });
            }
        }
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

    private doIt = (pointX: number, pointY: number): ElkExtendedEdge | undefined => {

        if (this.rootNode && this.rootNode.edges) {
            for (const edge of this.rootNode.edges) {
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
        return (startX < endX ? (startX - this.LINE_RANGE <= pointX && pointX <= endX + this.LINE_RANGE) : (endX - this.LINE_RANGE <= pointX && pointX <= startX + this.LINE_RANGE)) &&
            (startY < endY ? (startY - this.LINE_RANGE <= pointY && pointY <= endY + this.LINE_RANGE) : (endY - this.LINE_RANGE <= pointY && pointY <= startY + this.LINE_RANGE));
    }
}


