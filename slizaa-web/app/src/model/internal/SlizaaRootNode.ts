import {ISlizaaRootNode} from "../ISlizaaRootNode";
import {SlizaaNode} from "./SlizaaNode";

export class SlizaaRootNode extends SlizaaNode implements ISlizaaRootNode {

    private nodeCache: Map<string, SlizaaNode>;

    constructor(public key: string, public title: string, public iconId: string, public hasChildren: boolean) {
        super(key, title,  iconId, hasChildren)

        this.nodeCache = new Map();
    }

    public lookupNode(id: string): SlizaaNode | undefined {
        return this.nodeCache.get(id);
    }

    public register(node: SlizaaNode) {
        this.nodeCache.set(node.key, node);
    }
}