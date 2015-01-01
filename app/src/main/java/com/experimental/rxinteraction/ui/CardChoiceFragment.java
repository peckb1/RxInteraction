package com.experimental.rxinteraction.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.experimental.rxinteraction.ArenaApplication;
import com.experimental.rxinteraction.ArenaCard;
import com.experimental.rxinteraction.BuildConfig;
import com.experimental.rxinteraction.R;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

public class CardChoiceFragment extends Fragment {


    private static final String TAG = ClassChoiceFragment.class.getSimpleName();

    @Inject Observable<List<ArenaCard>> chosenCardsObservable;

    @Nullable Subscription cardSelectedSubscription;

    private LinearLayout currentCardChoicesLayout;
    private LayoutInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((ArenaApplication) getActivity().getApplication()).component().inject(this);

        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_choose_arena_card, container, false);
        currentCardChoicesLayout = (LinearLayout) view.findViewById(R.id.current_card_choices);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (cardSelectedSubscription == null || cardSelectedSubscription.isUnsubscribed()) {
            chosenCardsObservable.filter(nonEmptyList()).subscribe(handleNewCardSelected(), handleFailure());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        cleanUpSubscription(cardSelectedSubscription);
    }

    private void cleanUpSubscription(@Nullable Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private Action1<Throwable> handleFailure() {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, throwable.getMessage(), throwable);
                }
            }
        };
    }

    private Action1<? super List<ArenaCard>> handleNewCardSelected() {
        return new Action1<List<ArenaCard>>() {
            @Override
            public void call(List<ArenaCard> arenaCards) {
                ViewGroup parent = (ViewGroup) currentCardChoicesLayout.getParent();
                int index = parent.indexOfChild(currentCardChoicesLayout);
                parent.removeView(currentCardChoicesLayout);
                currentCardChoicesLayout = (LinearLayout) inflater.inflate(R.layout.card_choices_layout, parent, false);
                parent.addView(currentCardChoicesLayout, index);
            }
        };
    }

    private Func1<? super List<ArenaCard>, Boolean> nonEmptyList() {
        return new Func1<List<ArenaCard>, Boolean>() {
            @Override
            public Boolean call(List<ArenaCard> arenaCards) {
                return !arenaCards.isEmpty();
            }
        };
    }
}
