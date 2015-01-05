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

/**
 * The Module used to provide the 'Providers' and 'Subjects' used as communication layers across
 * the custom views
 */
@Module
@SuppressWarnings("unused")
public class ArenaModule {

    private static final String TAG = ArenaModule.class.getSimpleName();

    private static final List<ArenaCard> mEventsSoFar = Lists.newArrayList();

    private static final BehaviorSubject<List<ArenaCard>> customAllCardsSubject =
            BehaviorSubject.create();

    /**
     * A 'Provider' for the set of classes that can be chosen from for a given arena
     */
    @Provides
    @Singleton
    ClassChoiceProvider provideClassChoiceProvider() {
        return new ClassChoiceProvider();
    }

    /**
     * A 'Provider' for the set of cards that can be chosen for a given draft for a specific class
     */
    @Provides
    @Singleton
    CardChoiceProvider provideCardChoiceProvider() {
        return new CardChoiceProvider();
    }

    /**
     * A BehaviorSubject used to communicate the currently chosen class for the
     * active arena draft.
     * <p/>
     * This subject always contains the most recently chosen arena class
     */
    @Provides
    @Singleton
    BehaviorSubject<ArenaClass> provideClassChoiceSubject() {
        return BehaviorSubject.create();
    }

    /**
     * A PublishSubject that is used for communication across CardChoiceLayout objects to make sure
     * that multiple cards cannot be selected for a given round of choices.
     */
    @Provides
    @Singleton
    PublishSubject<ArenaCard> provideCardChoicePublishSubject() {
        return PublishSubject.create();
    }

    /**
     * A Behavior subject that always contains the last card choice made for a given arena draft.
     * <p/>
     * Used also to ensure the Observable containing all of the chosen cards in a draft can be cleared
     * when a new draft is started
     */
    @Provides
    @Singleton
    BehaviorSubject<Either<ArenaCard, ClearEvent>> provideCardChoiceBehaviorSubject() {
        BehaviorSubject<Either<ArenaCard, ClearEvent>> behaviorSubject = BehaviorSubject.create();
        behaviorSubject.subscribe(handleNewEvent(), handleError());
        return behaviorSubject;
    }

    /**
     * An observable which always returns the currently chosen set of cards for the active draft inside
     * the arena
     * <p/>
     * This observable is from a BehaviorSubject so any subscription to it will immediately return the
     * most recent set of cards chosen.
     */
    @Provides
    @Singleton
    Observable<List<ArenaCard>> provideCardChoicesObservable() {
        return customAllCardsSubject.asObservable();
    }

    /**
     * This custom action creates (in essence) a reset-able ReplaySubject that we expose as an Observable through
     * `provideCardChoicesObservable()` which uses the subject obtained through 'provideCardChoiceBehaviorSubject()'
     * to either add to the observable, or reset its state.
     */
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

    /**
     * The error handling partner to `handleNewEvent()`
     */
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
