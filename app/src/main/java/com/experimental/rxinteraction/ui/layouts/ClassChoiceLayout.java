package com.experimental.rxinteraction.ui.layouts;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.experimental.rxinteraction.ArenaApplication;
import com.experimental.rxinteraction.R;
import com.experimental.rxinteraction.ArenaClass;
import com.experimental.rxinteraction.util.ClassChoiceProvider;
import com.google.common.base.Optional;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.subjects.BehaviorSubject;

public class ClassChoiceLayout extends LinearLayout {

    @Inject ClassChoiceProvider classChoiceProvider;
    @Inject BehaviorSubject<ArenaClass> classChoiceSubject;

    @InjectView(R.id.class_portrait) ImageView classPortrait;
    @InjectView(R.id.class_name) TextView classDescription;

    @Nullable ArenaClass arenaClass;

    public ClassChoiceLayout(Context context) {
        super(context);
    }

    public ClassChoiceLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClassChoiceLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.class_portrait)
    public void submit(View view) {
        if (arenaClass != null) {
            classChoiceSubject.onNext(arenaClass);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.inject(this);
        ArenaApplication.inject(this);

        Optional<ArenaClass> nextClass = classChoiceProvider.getNextClass();
        if (nextClass.isPresent()) {
            arenaClass = nextClass.get();
            classPortrait.setImageResource(arenaClass.getDrawable());
            classDescription.setText(arenaClass.toString());
        }
    }
}
