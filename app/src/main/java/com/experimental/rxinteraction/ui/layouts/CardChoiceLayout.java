package com.experimental.rxinteraction.ui.layouts;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.experimental.rxinteraction.ArenaApplication;
import com.experimental.rxinteraction.ArenaCard;
import com.experimental.rxinteraction.BuildConfig;
import com.experimental.rxinteraction.ArenaClass;
import com.experimental.rxinteraction.R;
import com.experimental.rxinteraction.util.CardChoiceProvider;
import com.experimental.rxinteraction.util.ClearEvent;
import com.experimental.rxinteraction.util.Either;
import com.google.common.base.Optional;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

import static com.experimental.rxinteraction.ArenaClass.UN_CHOSEN;

public class CardChoiceLayout extends LinearLayout {

    private static final String TAG = CardChoiceLayout.class.getSimpleName();

    @Inject BehaviorSubject<Either<ArenaCard, ClearEvent>> selectCardSubject;
    @Inject BehaviorSubject<ArenaClass> classChoiceSubject;
    @Inject CardChoiceProvider cardChoiceProvider;

    @InjectView(R.id.card_image) ImageView cardImage;

    @Nullable Subscription clearStateSubscription;
    @Nullable Subscription createStateSubscription;
    @Nullable private ArenaCard currentCard;

    public CardChoiceLayout(Context context) {
        super(context);
    }

    public CardChoiceLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CardChoiceLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @OnClick(R.id.card_image)
    public void submit(View view) {
        if (currentCard != null) {
            selectCardSubject.onNext(Either.<ArenaCard, ClearEvent>left(currentCard));
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.inject(this);

        ArenaApplication.inject(this);

        if (clearStateSubscription == null || clearStateSubscription.isUnsubscribed()) {
            clearStateSubscription = classChoiceSubject.filter(isResetEvent())
                    .subscribe(handleResetEvent(), handleResetFailure());
        }

        if (createStateSubscription == null || createStateSubscription.isUnsubscribed()) {
            createStateSubscription = classChoiceSubject.filter(isNotResetEvent())
                    .subscribe(handleChosenClassEvent(), handleChosenClassFailure());
        }
    }

    private void cleanUpSubscription(@Nullable Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    // success

    private Action1<? super ArenaClass> handleChosenClassEvent() {
        return new Action1<ArenaClass>() {
            @Override
            public void call(ArenaClass arenaClass) {
                Optional<ArenaCard> card = cardChoiceProvider.getNextCard(arenaClass);

                if (card.isPresent()) {
                    ArenaCard arenaCard = card.get();
                    currentCard = arenaCard;
                    cardImage.setImageResource(arenaCard.getImageResource());
                }
            }
        };
    }

    private Action1<? super ArenaClass> handleResetEvent() {
        return new Action1<ArenaClass>() {
            @Override
            public void call(ArenaClass arenaClass) {
                cleanUpSubscription(clearStateSubscription);
                cleanUpSubscription(createStateSubscription);
            }
        };
    }

    // errors

    private Action1<Throwable> handleResetFailure() {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, throwable.getMessage(), throwable);
                }
            }
        };
    }

    private Action1<Throwable> handleChosenClassFailure() {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, throwable.getMessage(), throwable);
                }
            }
        };
    }

    // Filters

    private Func1<? super ArenaClass, Boolean> isNotResetEvent() {
        return new Func1<ArenaClass, Boolean>() {
            @Override
            public Boolean call(ArenaClass arenaClass) {
                return !isResetEvent().call(arenaClass);
            }
        };
    }

    private Func1<? super ArenaClass, Boolean> isResetEvent() {
        return new Func1<ArenaClass, Boolean>() {
            @Override
            public Boolean call(ArenaClass arenaClass) {
                return arenaClass == UN_CHOSEN;
            }
        };
    }
}
