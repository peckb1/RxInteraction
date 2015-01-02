package com.experimental.rxinteraction.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.experimental.rxinteraction.ArenaApplication;
import com.experimental.rxinteraction.ArenaCard;
import com.experimental.rxinteraction.BuildConfig;
import com.experimental.rxinteraction.R;
import com.experimental.rxinteraction.adapters.ChosenCardAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

public class ChosenCardDialog extends Dialog {

    private static final String TAG = ChosenCardDialog.class.getSimpleName();

    @Inject Observable<List<ArenaCard>> chosenCardsObservable;

    @InjectView(R.id.chosen_cards_list) ListView chosenCardsList;

    Subscription chosenCardsSubscription;

    public ChosenCardDialog(Context context) {
        super(context);
    }

    @OnClick(R.id.chosen_cards_ok_button)
    public void submit(View view) {
        cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Chosen Cards");
        setContentView(R.layout.chosen_cards_dialog);

        ButterKnife.inject(this);
        ArenaApplication.inject(this);

        if (chosenCardsSubscription == null || chosenCardsSubscription.isUnsubscribed()) {
            chosenCardsObservable.subscribe(handleCards(), handleError());
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

    private Action1<? super List<ArenaCard>> handleCards() {
        return new Action1<List<ArenaCard>>() {
            @Override
            public void call(List<ArenaCard> arenaCards) {
                chosenCardsList.setAdapter(new ChosenCardAdapter(getContext(), arenaCards));
            }
        };
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        cleanUpSubscription(chosenCardsSubscription);
        super.setOnDismissListener(listener);
    }

    private void cleanUpSubscription(@Nullable Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
