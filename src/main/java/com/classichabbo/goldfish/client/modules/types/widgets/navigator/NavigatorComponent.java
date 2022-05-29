package com.classichabbo.goldfish.client.modules.types.widgets.navigator;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.modules.Component;
import com.classichabbo.goldfish.client.modules.types.GoldfishView;
import com.classichabbo.goldfish.client.modules.types.club.ClubView;
import com.classichabbo.goldfish.client.modules.types.widgets.navigator.NavigatorView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class NavigatorComponent extends Component {
    private final NavigatorView navigatorView;
    private final List<NavigatorNode> nodes;

    public NavigatorComponent(NavigatorView navigatorView) {
        this.navigatorView = navigatorView;
        this.nodes = new ArrayList<>();
    }


    public void processNavigatorData(NavigatorNode tNodeInfo) {
        // Gson gson = new Gson();
        // System.out.println(gson.toJson(tNodeInfo));

        invoke(() -> {
            var navigatorView = Movie.getInstance().getViewByClass(NavigatorView.class);

            if (navigatorView != null) {
                
            }
        });
    }
}
