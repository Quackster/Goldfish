package com.classichabbo.goldfish.client.interfaces.types.entry;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.interfaces.types.loader.LoadingScreen;
import com.classichabbo.goldfish.client.interfaces.types.toolbars.EntryToolbar;
import com.classichabbo.goldfish.client.interfaces.types.widgets.Navigator;
import com.classichabbo.goldfish.client.scripts.Cloud;
import com.classichabbo.goldfish.client.util.DimensionUtil;

import java.util.concurrent.ThreadLocalRandom;

public class EntryComponent {
    private final EntryView entryView;

    public EntryComponent(EntryView entryView) {
        this.entryView = entryView;

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

    public void initLoginProcess() {
        System.out.println("init login");
        // Movie.getInstance().createObject(new Alert("Project Havana - Habbo Hotel v31 emulation\n\nRelease: r31_20090312_0433_13751_b40895fb610dbe96dc7b9d6477eeeb4\n\nContributors:\n - ThuGie, Copyright, Raptosaur, Hoshiko, TOMYSSHADOW, Elijah\n   Romauld, Glaceon, Nillus, Holo Team, Meth0d, office.boy, bbadzz\n\n   Big thanks to Sefhriloff & Ascii for assisting with SnowStorm.\n\nMade by Quackster from RaGEZONE"));

        // Remove loading bar (moved to here so it removes it before it starts animating)
        // (if this is wrong please don't hate me was just finalising EntryToolbar) :)
        var loadingBar = Movie.getInstance().getInterfaces().stream().filter(x -> x instanceof LoadingScreen).findFirst().orElse(null);//ifPresent(loadingBar -> Movie.getInstance().removeObject(loadingBar));

        if (loadingBar != null) {
            if (((LoadingScreen) loadingBar).getTotalLoaderProgress() >= 100)
                loadingBar.remove();
            else {
                tryLogin();
                return;
            }
        }

        var entryToolbar = Movie.getInstance().getInterfaces().stream().filter(x -> x instanceof EntryToolbar).findFirst().orElse(null);

        if (entryToolbar == null) {
            Movie.getInstance().createObject(new EntryToolbar(this.entryView));
        }

        var navigator = Movie.getInstance().getInterfaces().stream().filter(x -> x instanceof Navigator).findFirst().orElse(null);

        if (navigator == null) {
            Movie.getInstance().createObject(new Navigator());
        } else {
            navigator.toFront();
        }

    }

    private void tryLogin() {

    }
}
