package com.experimental.rxinteraction.ui.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.experimental.rxinteraction.ArenaApplication;
import com.experimental.rxinteraction.ArenaClass;
import com.experimental.rxinteraction.R;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

public class ArenaFinishedLayout extends LinearLayout {
    private static final String TAG = ArenaFinishedLayout.class.getSimpleName();

    @Inject BehaviorSubject<ArenaClass> classChoiceSubject;

    @InjectView(R.id.class_portrait) ImageView classPortrait;
    @InjectView(R.id.class_name) TextView classDescription;

    public ArenaFinishedLayout(Context context) {
        super(context);
    }

    public ArenaFinishedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArenaFinishedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.inject(this);
        ArenaApplication.inject(this);

        Subscription subscription = classChoiceSubject.subscribe(handleClassDetails(), handleError());

        subscription.unsubscribe();
    }

    private Action1<Throwable> handleError() {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(TAG, throwable.getMessage(), throwable);
            }
        };
    }

    private Action1<? super ArenaClass> handleClassDetails() {
        return new Action1<ArenaClass>() {
            @Override
            public void call(ArenaClass arenaClass) {
                classPortrait.setImageResource(arenaClass.getDrawable());
                classDescription.setText(arenaClass.toString());
            }
        };
    }
}
