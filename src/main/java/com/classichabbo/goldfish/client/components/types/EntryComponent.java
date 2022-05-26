package com.classichabbo.goldfish.client.components.types;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.components.Component;
import com.classichabbo.goldfish.client.views.types.GoldfishView;
import com.classichabbo.goldfish.client.views.types.entry.EntryView;
import com.classichabbo.goldfish.client.views.types.toolbars.EntryToolbar;
import com.classichabbo.goldfish.client.views.types.widgets.navigator.NavigatorView;

public class EntryComponent extends Component {
    private final EntryView entryView;

    public EntryComponent(EntryView entryView) {
        this.entryView = entryView;
    }

    public void tryLogin() {
        invoke(() -> {
            var goldfish = Movie.getInstance().getViewByClass(GoldfishView.class);

            if (goldfish != null) {
                goldfish.getComponent().loadWidgets();
            }

            this.toHotelView();
        });
    }

    public void toHotelView() {
        invoke(() -> {
            // Always open back up navigator when headed towards hotel view
            if (Movie.getInstance().isViewActive(NavigatorView.class)) {
                var navigator = Movie.getInstance().getViewByClass(NavigatorView.class);

                if (navigator != null && navigator.isCreated()) {
                    navigator.toFront();
                    navigator.setHidden(false);
                }
            }

            // Add entry toolbar back
            if (!Movie.getInstance().isViewActive(EntryToolbar.class)) {
                Movie.getInstance().createObject(new EntryToolbar(), this.entryView);
            }
        });

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
