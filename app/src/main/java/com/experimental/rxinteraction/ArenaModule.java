package com.experimental.rxinteraction;

import android.util.Log;

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
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

@Module
public class ArenaModule {

    private static final String TAG = ArenaModule.class.getSimpleName();

    private final ArenaApplication application;

    private static final List<ArenaCard> mEventsSoFar = Lists.newArrayList();

    private static final BehaviorSubject<List<ArenaCard>> customAllCardsSubject =
            BehaviorSubject.create();

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
    PublishSubject<ArenaCard> provideCardChoicePublishSubject() {
        return PublishSubject.create();
    }

    @Provides
    @Singleton
    BehaviorSubject<Either<ArenaCard, ClearEvent>> provideCardChoiceBehaviorSubject() {
        BehaviorSubject<Either<ArenaCard, ClearEvent>> behaviorSubject = BehaviorSubject.create();
        behaviorSubject.subscribe(handleNewEvent(), handleError());
        return behaviorSubject;
    }

    @Provides
    @Singleton
    Observable<List<ArenaCard>> provideCardChoicesObservable() {
        return customAllCardsSubject.asObservable();
    }

    private Action1<? super Either<ArenaCard, ClearEvent>> handleNewEvent() {
        return new Action1<Either<ArenaCard, ClearEvent>>() {
            @Override
            public void call(Either<ArenaCard, ClearEvent> eventEither) {
                if (eventEither.isRight()) {
                    mEventsSoFar.clear();
                } else {
                    mEventsSoFar.add(eventEither.left());
                }
                customAllCardsSubject.onNext(mEventsSoFar);
            }
        };
    }

    private Action1<Throwable> handleError() {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, throwable.getMessage(), throwable);
                }
            }
        };
    }
}
