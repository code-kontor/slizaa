import ELK, {ElkExtendedEdge, ElkNode, ElkPoint} from 'elkjs/lib/elk.bundled.js'
import * as React from "react";
import {setupCanvas} from "../dsm/DpiFixer";
import {ISlizaaDependencyListState} from "./IDependencyGraphState";

export class DependencyGraph extends React.Component<any, ISlizaaDependencyListState> {

    private readonly CORNER_RADIUS = 3;
    private readonly NODE_HEIGHT = 30;
    private readonly NODE_WIDTH = 170;

    private canvasRef: HTMLCanvasElement | null;
    private renderingContext: CanvasRenderingContext2D | null;

    private rootNode: ElkNode | null;

    constructor(props: any) {
        super(props);

        this.state = {};
    }

    public componentDidMount(): void {

        if (this.canvasRef) {
            this.renderingContext = this.canvasRef.getContext("2d")
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
            },
            children: [
                {id: "n1", width: this.NODE_WIDTH, height: this.NODE_HEIGHT},
                {id: "n2", width: this.NODE_WIDTH, height: this.NODE_HEIGHT},
                {id: "n3", width: this.NODE_WIDTH, height: this.NODE_HEIGHT},
                {id: "n4", width: this.NODE_WIDTH, height: this.NODE_HEIGHT},
                {id: "n5", width: this.NODE_WIDTH, height: this.NODE_HEIGHT},
                {id: "n6", width: this.NODE_WIDTH, height: this.NODE_HEIGHT},
            ],
            edges: [
                {id: "e1", sources: ["n1"], targets: ["n6"]},
                {id: "e2", sources: ["n2"], targets: ["n6"]},
                {id: "e3", sources: ["n3"], targets: ["n1"]},
                {id: "e4", sources: ["n2"], targets: ["n6"]},
                {id: "e5", sources: ["n4"], targets: ["n5"]},
                {id: "e6", sources: ["n6"], targets: ["n3"]},
                {id: "e7", sources: ["n2"], targets: ["n6"]},
                {id: "e8", sources: ["n2"], targets: ["n5"]}
            ]
        }

        elk.layout(graph)
            // tslint:disable-next-line:no-console
            .then((value) => {
                this.rootNode = value;
                this.draw();
            })
            // tslint:disable-next-line:no-console
            .catch(console.error)
    }

    public render() {
        return <div id="dsm-canvas-container">
            <canvas id="main" ref={ref => (this.canvasRef = ref)}/>
        </div>
    }

    private draw = () => {

        if (this.canvasRef && this.renderingContext && this.rootNode && this.rootNode.width && this.rootNode.height) {

            const renderingContext = this.renderingContext;
            const scale = 1.6;

            // tslint:disable-next-line:no-console
            console.log(JSON.stringify(this.rootNode))

            setupCanvas(this.canvasRef, this.renderingContext, this.rootNode.width * scale, this.rootNode.height * scale);
            renderingContext.scale(scale, scale)

            // draw the nodes
            if (this.rootNode.children) {
                this.rootNode.children.forEach(node => {
                        this.drawNode(renderingContext, node);
                    }
                )
            }


            // draw the edges
            if (this.rootNode.edges) {
                this.rootNode.edges.forEach(edge => {

                        const extendedEdge: ElkExtendedEdge = edge as ElkExtendedEdge;

                        extendedEdge.sections.forEach(section => {

                            renderingContext.strokeStyle = section.startPoint.y > section.endPoint.y ? "#FF0000" : "#000000";
                            renderingContext.fillStyle = section.startPoint.y > section.endPoint.y ? "#FF0000" : "#000000";

                            let lastPoint = section.startPoint;

                            renderingContext.beginPath();
                            renderingContext.moveTo(section.startPoint.x, section.startPoint.y);

                            //
                            if (section.bendPoints) {

                                // tslint:disable-next-line:prefer-for-of
                                for (let i = 0; i < section.bendPoints.length; i++) {

                                    const currentPoint = section.bendPoints[i];
                                    const nextPoint = i < section.bendPoints.length - 1 ? section.bendPoints[i + 1] : section.endPoint;

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

                            renderingContext.lineTo(section.endPoint.x, section.endPoint.y);
                            renderingContext.stroke();

                            this.drawArrowhead(renderingContext, lastPoint, section.endPoint, 4);
                        })
                    }
                )
            }
        }
    }

    private drawNode = (context: CanvasRenderingContext2D, node: ElkNode) => {

        if (node.x && node.y && node.width && node.height) {

            context.save();

            this.roundRect(
                context,
                node.x,
                node.y,
                node.width,
                node.height,
                this.CORNER_RADIUS
            );

            // ...set the clipping area
            context.beginPath();
            context.rect(node.x,
                node.y,
                node.width,
                node.height,);
            context.clip();

            context.textAlign = "left";
            context.textBaseline = "middle";
            context.fillText("i.c.s.h.model", node.x + (this.NODE_HEIGHT / 2) , node.y + (this.NODE_HEIGHT / 2) );

            // if (node.children) {
            //     node.children.forEach(child => {
            //             this.drawNode(context, node);
            //         }
            //     )
            // }

            context.restore();
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

    private roundRect = (context: CanvasRenderingContext2D, x: number, y: number, w: number, h: number, radius: number) => {

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
        context.stroke();
    }
}


