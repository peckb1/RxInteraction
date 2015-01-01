package com.experimental.rxinteraction;

import android.app.Application;

import com.experimental.rxinteraction.ui.ArenaActivity;
import com.experimental.rxinteraction.ui.CardChoiceFragment;
import com.experimental.rxinteraction.ui.ClassChoiceFragment;
import com.experimental.rxinteraction.ui.fabs.ChosenCardsFAB;
import com.experimental.rxinteraction.ui.fabs.ChosenClassFAB;
import com.experimental.rxinteraction.ui.layouts.CardChoiceLayout;
import com.experimental.rxinteraction.ui.layouts.ClassChoiceLayout;
import com.experimental.rxinteraction.util.CardChoiceProvider;

import javax.inject.Singleton;

import dagger.Component;

public class ArenaApplication extends Application {

    @Singleton
    @Component(modules = ArenaModule.class)
    public interface ArenaComponent {
        void inject(ArenaActivity homeActivity);

        void inject(CardChoiceFragment cardChoiceFragment);

        void inject(ClassChoiceLayout layout);
        void inject(CardChoiceLayout layout);

        void inject(CardChoiceProvider cardChoiceProvider);

        void inject(ChosenClassFAB chosenClassFAB);
        void inject(ChosenCardsFAB chosenCardsFAB);
    }

    private static ArenaComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = Dagger_ArenaApplication$ArenaComponent.builder()
                .arenaModule(new ArenaModule(this))
                .build();
    }

    public ArenaComponent component() {
        return component;
    }

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
}
