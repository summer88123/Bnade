package com.summer.bnade.base;

import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import javax.annotation.Nonnull;

import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by kevin.bai on 2017/4/4.
 */

public abstract class BaseActivity<M extends BaseUIModel> extends DaggerAppCompatActivity implements IActivityCreated,
        LifecycleProvider<ActivityEvent> {

    BehaviorSubject<ActivityEvent> subject = BehaviorSubject.create();

    @Override
    public void setUpObservable() {
        // nothing
    }

    protected Consumer<M> showAs() {
        return model -> {
            onProgress(model.isInProgress());
            if (!model.isInProgress()) {
                if (model.isSuccess()) {
                    onSuccess(model);
                } else {
                    onFailure(model);
                }
            }
        };
    }

    protected void onFailure(M model) {
        // show failure
    }

    protected void onSuccess(M model) {
        // show success
    }

    protected void onProgress(boolean inProgress) {
        // show progress
    }

    @Nonnull
    @CheckResult
    protected Observable<ActivityEvent> untilEmit(@Nonnull ActivityEvent activityEvent) {
        return lifecycle().filter(event -> event.equals(activityEvent));
    }

    @Nonnull
    @Override
    public Observable<ActivityEvent> lifecycle() {
        return subject.hide();
    }

    @Nonnull
    @Override
    public <T> LifecycleTransformer<T> bindUntilEvent(@Nonnull ActivityEvent activityEvent) {
        return RxLifecycle.bindUntilEvent(subject, activityEvent);
    }

    @Nonnull
    @Override
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(subject);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subject.onNext(ActivityEvent.CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        subject.onNext(ActivityEvent.START);
    }

    @Override
    protected void onStop() {
        super.onStop();
        subject.onNext(ActivityEvent.STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subject.onNext(ActivityEvent.DESTROY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        subject.onNext(ActivityEvent.PAUSE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        subject.onNext(ActivityEvent.RESUME);
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showToast(@StringRes int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

}
