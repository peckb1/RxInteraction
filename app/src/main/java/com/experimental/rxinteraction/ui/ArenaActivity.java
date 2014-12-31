package com.experimental.rxinteraction.ui;

import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.experimental.rxinteraction.ArenaApplication;
import com.experimental.rxinteraction.BuildConfig;
import com.experimental.rxinteraction.R;
import com.experimental.rxinteraction.ArenaClass;
import com.experimental.rxinteraction.util.ClassChoiceProvider;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

import static com.experimental.rxinteraction.ArenaClass.UN_CHOSEN;

public class ArenaActivity extends ActionBarActivity {

    private static final String TAG = ArenaActivity.class.getSimpleName();

    @Inject BehaviorSubject<ArenaClass> classChoiceSubject;
    @Inject ClassChoiceProvider classChoiceProvider;

    @Nullable Subscription classChoiceSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arena);
        ((ArenaApplication) getApplication()).component().inject(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ClassChoiceFragment())
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(classChoiceSubscription == null || classChoiceSubscription.isUnsubscribed()) {
            classChoiceSubscription = classChoiceSubject.filter(validChoice())
                                                        .subscribe(handleSuccessfulClassChoice(),
                                                                   handleClassChoiceFailures());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cleanUpSubscription(classChoiceSubscription);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_arena, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_reset_draft: {
                classChoiceProvider.reset();
                classChoiceSubject.onNext(UN_CHOSEN);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new ClassChoiceFragment())
                        .commit();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private Func1<? super ArenaClass, Boolean> validChoice() {
        return new Func1<ArenaClass, Boolean>() {
            @Override
            public Boolean call(ArenaClass arenaClass) {
                return arenaClass != UN_CHOSEN;
            }
        };
    }

    private Action1<Throwable> handleClassChoiceFailures() {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, throwable.getMessage(), throwable);
                }
            }
        };
    }

    private Action1<? super ArenaClass> handleSuccessfulClassChoice() {
        return new Action1<ArenaClass>() {
            @Override
            public void call(ArenaClass arenaClass) {
                if (isFinishing()) {
                    return;
                }

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new CardChoiceFragment())
                        .commit();
            }
        };
    }

    private void cleanUpSubscription(@Nullable Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
