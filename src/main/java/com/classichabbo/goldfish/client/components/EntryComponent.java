package com.classichabbo.goldfish.client.components;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.views.types.entry.EntryView;
import com.classichabbo.goldfish.client.views.types.toolbars.EntryToolbar;
import com.classichabbo.goldfish.client.views.types.widgets.navigator.NavigatorView;
import com.classichabbo.goldfish.client.scripts.Cloud;
import com.classichabbo.goldfish.util.DimensionUtil;

import java.util.concurrent.ThreadLocalRandom;

public class EntryComponent {
    private final EntryView entryView;

    public EntryComponent(EntryView entryView) {
        this.entryView = entryView;
        this.entryView.getClouds().clear();

        // Kickstart some clouds after turn point :^)
        for (int i = 0; i < ThreadLocalRandom.current().nextInt(2, 5) + 1; i++) {
            int initX = this.entryView.getCloudTurnPoint() + ThreadLocalRandom.current().nextInt(30, (int) (DimensionUtil.getProgramWidth() * 0.5));
            ;
            int initY = ThreadLocalRandom.current().nextInt(0, (int) (DimensionUtil.getProgramHeight() * 0.66));

            this.addCloud("right", initX, initY);
        }

        // Kickstart some clouds before turn point :^)
        for (int i = 0; i < ThreadLocalRandom.current().nextInt(1, 2) + 1; i++) {
            int initX = this.entryView.getCloudTurnPoint() - ThreadLocalRandom.current().nextInt(35, 60);
            int initY = ThreadLocalRandom.current().nextInt(0, (int) (DimensionUtil.getProgramHeight() * 0.66));

            this.addCloud("left", initX, initY);
        }
    }

    /**
     * Add cloud handler
     */
    public void addCloud(String direction, int initX, int initY) {
        var cloud = new Cloud(this.entryView.getCloudTurnPoint(), "cloud_" + ThreadLocalRandom.current().nextInt(4), direction, initX, initY);
        cloud.setViewOrder(this.entryView.CLOUD_Z_INDEX);

        this.entryView.getClouds().add(cloud);
        this.entryView.getChildren().add(cloud);
    }

    public void tryLogin() {
        var navigator = Movie.getInstance().getViews().stream().filter(x -> x instanceof NavigatorView).findFirst().orElse(null);

        if (navigator == null) {
            Movie.getInstance().createObject(new NavigatorView());
        } else {
            navigator.toFront();
            navigator.setHidden(false);
        }

        Movie.getInstance().createObject(new EntryToolbar(), this.entryView);
    }

    public void entryViewResume() {
        var navigator = Movie.getInstance().getViews().stream().filter(x -> x instanceof NavigatorView).findFirst().orElse(null);

        if (navigator != null) {
            Movie.getInstance().createObject(new EntryToolbar(), this.entryView);
        }

        // Movie.getInstance().createObject(new Alert("Project Havana - Habbo Hotel v31 emulation\n\nRelease: r31_20090312_0433_13751_b40895fb610dbe96dc7b9d6477eeeb4\n\nContributors:\n - ThuGie, Copyright, Raptosaur, Hoshiko, TOMYSSHADOW, Elijah\n   Romauld, Glaceon, Nillus, Holo Team, Meth0d, office.boy, bbadzz\n\n   Big thanks to Sefhriloff & Ascii for assisting with SnowStorm.\n\nMade by Quackster from RaGEZONE"));

        // Remove loading bar (moved to here so it removes it before it starts animating)
        // (if this is wrong please don't hate me was just finalising EntryToolbar) :)

        /*Platform.runLater(() -> {
            var loader = Movie.getInstance().getInterfaceByClass(LoadingScreen.class);

            if (loader != null) {
                System.out.println("loader removal");
                Movie.getInstance().removeObject(loader);
            }

            Movie.getInstance().createObject(new EntryToolbar(this.entryView), this.entryView);

            var navigator = Movie.getInstance().getViews().stream().filter(x -> x instanceof Navigator).findFirst().orElse(null);

            if (navigator == null) {
                Movie.getInstance().createObject(new Navigator());
            } else {
                navigator.toFront();
                navigator.setHidden(false);
            }
        });*/
    }
}
