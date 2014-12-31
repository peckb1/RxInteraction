package com.experimental.rxinteraction;

import com.experimental.rxinteraction.util.CardChoiceProvider;
import com.experimental.rxinteraction.util.ClassChoiceProvider;
import com.experimental.rxinteraction.util.ClearEvent;
import com.experimental.rxinteraction.util.Either;
import com.google.common.collect.Lists;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Observable;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

import static com.experimental.rxinteraction.util.ClearEvent.CLEAR;

@Module
public class ArenaModule {

    private final ArenaApplication application;

    private final List<ArenaCard> mEventsSoFar = Lists.newArrayList();

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

    @Provides
    @Singleton
    BehaviorSubject<Either<ArenaCard, ClearEvent>> provideCardChoiceSubject() {
        return BehaviorSubject.create(Either.<ArenaCard, ClearEvent>right(CLEAR));
    }

    @Provides
    @Singleton
    Observable<List<ArenaCard>> provideCardChoicesObservable(
            BehaviorSubject<Either<ArenaCard, ClearEvent>> singleEvent) {

        return singleEvent.map(new Func1<Either<ArenaCard,ClearEvent>, List<ArenaCard>>() {
            @Override
            public List<ArenaCard> call(Either<ArenaCard,ClearEvent> eventEither) {
                if (eventEither.isRight()) {
                    mEventsSoFar.clear();
                    return mEventsSoFar;
                } else {
                    mEventsSoFar.add(eventEither.left());
                    return mEventsSoFar;
                }
            }
        });
    }
}
