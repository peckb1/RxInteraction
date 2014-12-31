package com.experimental.rxinteraction.ui.fabs;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.experimental.rxinteraction.ArenaApplication;
import com.experimental.rxinteraction.ArenaClass;
import com.experimental.rxinteraction.BuildConfig;
import com.experimental.rxinteraction.R;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

import static android.app.AlertDialog.Builder;
import static com.experimental.rxinteraction.ArenaClass.UN_CHOSEN;

public class ChosenClassFAB extends FrameLayout {

    private static final String TAG = ChosenClassFAB.class.getSimpleName();

    @Inject BehaviorSubject<ArenaClass> classChoiceSubject;

    @Nullable private Subscription clearStateSubscription;
    @Nullable private Subscription createStateSubscription;

    @Nullable private ArenaClass chosenClass;

    public ChosenClassFAB(Context context) {
        super(context);
    }

    public ChosenClassFAB(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChosenClassFAB(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @OnClick(R.id.chosen_class_button)
    public void submit(View view) {
        if (chosenClass != null) {
            Builder builder = new Builder(getContext());
            builder.setTitle(chosenClass.toString())
                   .setMessage(chosenClass.getHeroPowerDescription())
                   .setNegativeButton("Ok", null);
            AlertDialog dialog = builder.create();

            dialog.show();
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
                chosenClass = arenaClass;
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
