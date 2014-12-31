package com.experimental.rxinteraction;

import com.experimental.rxinteraction.util.CardChoiceProvider;
import com.experimental.rxinteraction.util.ClassChoiceProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.subjects.BehaviorSubject;

@Module
public class ArenaModule {

    private final ArenaApplication application;

    public ArenaModule(ArenaApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    BehaviorSubject<ArenaClass> provideClassChoiceSubject() {
        return BehaviorSubject.create();
    }

    @Provides
    @Singleton
    ClassChoiceProvider provideClassChoiceProvider() {
        return new ClassChoiceProvider();
    }

    @Provides
    @Singleton
    CardChoiceProvider provideCardChoiceProvider() {
        return new CardChoiceProvider();
    }
}
