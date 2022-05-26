package com.classichabbo.goldfish.client.components.types;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.components.Component;
import com.classichabbo.goldfish.client.views.types.GoldfishView;
import com.classichabbo.goldfish.client.views.types.club.ClubView;
import com.classichabbo.goldfish.client.views.types.widgets.navigator.NavigatorView;

public class GoldfishComponent extends Component {
    private final GoldfishView goldfishView;

    public GoldfishComponent(GoldfishView goldfishView) {
        this.goldfishView = goldfishView;
    }

    public void loadModules() {
        invoke(() -> {
            if (!Movie.getInstance().isViewActive(ClubView.class)) {
                Movie.getInstance().createObject(new ClubView());
            }
        });
    }

    public void loadWidgets() {
        invoke(() -> {
            if (!Movie.getInstance().isViewActive(NavigatorView.class)) {
                Movie.getInstance().createObject(new NavigatorView());
            }
        });
    }
}
