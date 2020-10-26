import {SlizaaRootNode} from "./internal/SlizaaRootNode";
import {ISlizaaRootNode} from "./ISlizaaRootNode";

export class SlizaaNodeFactory {

    public static DEFAULT_ICON_ID = "default";

    public static DEFAULT_ROOT_ICON_ID = "default";

    public static createRoot(title: string): ISlizaaRootNode {
        return new SlizaaRootNode("-1", title, SlizaaNodeFactory.DEFAULT_ROOT_ICON_ID, true);
    }
}