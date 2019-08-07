export interface IDsmProps {
    labels: IDsmLabel[];
    cells: IDsmCell[];
    stronglyConnectedComponents: IDsmStronglyConnectedComponent[];
    horizontalBoxSize?: number;
    verticalBoxSize?: number;
    horizontalSideMarkerHeight: number;
    verticalSideMarkerWidth: number;
    onSideMarkerResize?: (horizontalSideMarkerHeight: number | undefined, verticalSideMarkerWidth: number | undefined) => void;
    // TODO: Callback-Signature - Generics in TypeScript?
    onHover?:  (columnElementId: string | undefined, rowElementId: string | undefined) => void;
    onSelect?: (columnElementId: string | undefined, rowElementId: string | undefined) => void;
}

export interface IDsmLabel {
    id: string;
    text: string;
    iconUrl?: string;
}

export interface IDsmCell {
    row: number;
    column: number;
    value: number;
}

export interface IDsmStronglyConnectedComponent {
    nodePositions: number[];
}