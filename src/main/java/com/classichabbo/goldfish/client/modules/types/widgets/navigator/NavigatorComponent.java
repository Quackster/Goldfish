package com.classichabbo.goldfish.client.modules.types.widgets.navigator;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.modules.Component;
import com.classichabbo.goldfish.client.modules.types.GoldfishView;
import com.classichabbo.goldfish.client.modules.types.club.ClubView;
import com.classichabbo.goldfish.client.modules.types.widgets.navigator.NavigatorView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NavigatorComponent extends Component {
    private final NavigatorView navigatorView;
    private final Map<Integer, NavigatorNode> nodes;
    private NavigatorNode currentNode;

    public NavigatorComponent(NavigatorView navigatorView) {
        this.navigatorView = navigatorView;
        this.nodes = new ConcurrentHashMap<>();
    }

    public void processNavigatorData(NavigatorNode tNodeInfo) {
        Gson gson = new Gson();
        System.out.println(gson.toJson(tNodeInfo));
        this.currentNode = tNodeInfo;

        // Rebuild node structure...
        this.nodes.put(tNodeInfo.getId(), this.currentNode);

        invoke(() -> {
            var navigatorView = Movie.getInstance().getViewByClass(NavigatorView.class);

            if (navigatorView != null) {
                navigatorView.updateRoomList(tNodeInfo);
            }
        });
    }

    /**
     * Finds the parent of the current node, if no parameter is specified then it will
     * default to the current node in-use.
     *
     * If no parent is found, then it will return null.
     */
    public NavigatorNode getParentNode(NavigatorNode... node) {
        var nodeToUse = node.length > 0 ? node[0] : this.currentNode;

        if (nodeToUse.getParentid() > 0 && this.nodes.containsKey(nodeToUse.getId())) {
            return this.nodes.get(0);
        }

        return null;
    }

    public NavigatorNode getCurrentNode() {
        return currentNode;
    }
}
