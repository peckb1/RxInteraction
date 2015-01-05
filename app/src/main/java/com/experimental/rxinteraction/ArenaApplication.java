package com.experimental.rxinteraction;

import android.app.Application;

import com.experimental.rxinteraction.ui.ArenaActivity;
import com.experimental.rxinteraction.ui.CardChoiceFragment;
import com.experimental.rxinteraction.ui.dialogs.ChosenCardDialog;
import com.experimental.rxinteraction.ui.fabs.ChosenCardsFAB;
import com.experimental.rxinteraction.ui.fabs.ChosenClassFAB;
import com.experimental.rxinteraction.ui.layouts.ArenaFinishedLayout;
import com.experimental.rxinteraction.ui.layouts.CardChoiceLayout;
import com.experimental.rxinteraction.ui.layouts.ClassChoiceLayout;
import com.experimental.rxinteraction.util.CardChoiceProvider;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Our main Application which sets up the object graph used by our classes for Dependency injection
 */
public class ArenaApplication extends Application {

    /**
     * A single component that everything injects into for their dependency injection
     * <p/>
     * On a larger project splitting this up into multiple components separating logic/functionality seems like
     * it might be easier to maintain
     */
    @Singleton
    @Component(modules = ArenaModule.class)
    public interface ArenaComponent {
        void inject(ArenaActivity homeActivity);

        void inject(CardChoiceFragment cardChoiceFragment);

        void inject(ClassChoiceLayout layout);
        void inject(CardChoiceLayout layout);
        void inject(ArenaFinishedLayout arenaFinishedLayout);

        void inject(CardChoiceProvider cardChoiceProvider);

        void inject(ChosenClassFAB chosenClassFAB);
        void inject(ChosenCardsFAB chosenCardsFAB);

        void inject(ChosenCardDialog dialog);

    }

    // A static component graph, which allows for items which don't have direct access to the Application
    // the ability to add themselves to the graph.
    private static ArenaComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = Dagger_ArenaApplication$ArenaComponent.builder()
                .arenaModule(new ArenaModule())
                .build();
    }

    public ArenaComponent component() {
        return component;
    }

    // Static access methods to add an object into the object graph
    // Using a marker interface might work to shrink these down to a single method

    public static void inject(ClassChoiceLayout layout) {
        component.inject(layout);
    }

    public static void inject(CardChoiceLayout layout) {
        component.inject(layout);
    }

    public static void inject(CardChoiceProvider provider) {
        component.inject(provider);
    }

    public static void inject(ChosenClassFAB fab) {
        component.inject(fab);
    }

    public static void inject(ChosenCardsFAB fab) {
        component.inject(fab);
    }

    public static void inject(ChosenCardDialog dialog) {
        component.inject(dialog);
    }

    public static void inject(ArenaFinishedLayout arenaFinishedLayout) {
        component.inject(arenaFinishedLayout);
    }
}
