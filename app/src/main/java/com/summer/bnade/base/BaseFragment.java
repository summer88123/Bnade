package com.summer.bnade.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import javax.annotation.Nonnull;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by kevin.bai on 2017/4/14.
 */

public abstract class BaseFragment<M extends BaseUIModel> extends Fragment implements IActivityCreated,
        LifecycleProvider<FragmentEvent> {
    private final BehaviorSubject<FragmentEvent> subject = BehaviorSubject.create();

    @Override
    public void setUpView() {
        // nothing
    }

    @Override
    public void injectComponent() {
        // nothing
    }

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
        showToast(model.getErrorMsg());
    }

    protected void onSuccess(M model) {
        // show success
    }

    protected void onProgress(boolean inProgress) {
        // show progress
    }


    @Nonnull
    @Override
    @CheckResult
    public Observable<FragmentEvent> lifecycle() {
        return subject.hide();
    }

    @Nonnull
    @Override
    @CheckResult
    public <T> LifecycleTransformer<T> bindUntilEvent(@Nonnull FragmentEvent fragmentEvent) {
        return RxLifecycle.bindUntilEvent(subject, fragmentEvent);
    }

    @Nonnull
    @CheckResult
    protected Observable<FragmentEvent> untilEmit(@Nonnull FragmentEvent fragmentEvent) {
        return lifecycle().filter(event -> event.equals(fragmentEvent));
    }

    @Nonnull
    @Override
    @CheckResult
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindFragment(subject);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        subject.onNext(FragmentEvent.ATTACH);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subject.onNext(FragmentEvent.CREATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(layout(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        subject.onNext(FragmentEvent.CREATE_VIEW);
    }


    @Override
    public void onStart() {
        super.onStart();
        subject.onNext(FragmentEvent.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        subject.onNext(FragmentEvent.RESUME);
    }

    @Override
    public void onPause() {
        super.onPause();
        subject.onNext(FragmentEvent.PAUSE);
    }

    @Override
    public void onStop() {
        super.onStop();
        subject.onNext(FragmentEvent.STOP);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        subject.onNext(FragmentEvent.DESTROY_VIEW);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subject.onNext(FragmentEvent.DESTROY);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        subject.onNext(FragmentEvent.DETACH);
    }

    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void showToast(@StringRes int resId) {
        Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
    }
}
