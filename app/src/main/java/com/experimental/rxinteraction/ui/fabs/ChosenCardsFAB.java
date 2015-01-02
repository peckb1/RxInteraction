package com.experimental.rxinteraction.ui.fabs;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.experimental.rxinteraction.ArenaApplication;
import com.experimental.rxinteraction.ArenaCard;
import com.experimental.rxinteraction.BuildConfig;
import com.experimental.rxinteraction.R;
import com.experimental.rxinteraction.ui.dialogs.ChosenCardDialog;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

public class ChosenCardsFAB extends FrameLayout {

    private static final String TAG = ChosenCardsFAB.class.getSimpleName();

    @Inject Observable<List<ArenaCard>> chosenCardEventsObservable;

    @InjectView(R.id.card_choice_overall_progress_text) TextView progressText;

    @Nullable Subscription chosenCardsSubscription;

    public ChosenCardsFAB(Context context) {
        super(context);
    }

    public ChosenCardsFAB(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChosenCardsFAB(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @OnClick(R.id.chosen_card_button)
    public void submit(View view) {
        ChosenCardDialog dialog = new ChosenCardDialog(getContext());
        dialog.show();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ArenaApplication.inject(this);
        ButterKnife.inject(this);

        if (chosenCardsSubscription == null || chosenCardsSubscription.isUnsubscribed()) {
            chosenCardsSubscription = chosenCardEventsObservable.subscribe(handleChosenCards(), handleError());
        }
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

    private Action1<? super List<ArenaCard>> handleChosenCards() {
        return new Action1<List<ArenaCard>>() {
            @Override
            public void call(List<ArenaCard> cards) {
                progressText.setText(String.format("%d / 30", cards.size()));
            }
        };
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cleanUpSubscription(chosenCardsSubscription);
    }

    private void cleanUpSubscription(@Nullable Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
