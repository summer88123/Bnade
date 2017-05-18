package com.summer.bnade.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kevin.bai on 2017/5/16.
 */

class BaseActivityLifeCycleCallback implements Application.ActivityLifecycleCallbacks {

    private Map<Activity, Unbinder> cache = new HashMap<>();

    private final BaseFragmentLifecycleCallbacks mBaseFragmentLifecycleCallbacks;

    BaseActivityLifeCycleCallback(BaseFragmentLifecycleCallbacks baseFragmentLifecycleCallbacks) {
        mBaseFragmentLifecycleCallbacks = baseFragmentLifecycleCallbacks;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activity instanceof IActivityCreated && activity instanceof AppCompatActivity) {
            IActivityCreated created = (IActivityCreated) activity;
            AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
            appCompatActivity.getSupportFragmentManager()
                    .registerFragmentLifecycleCallbacks(mBaseFragmentLifecycleCallbacks, true);
            activity.setContentView(created.layout());
            cache.put(activity, ButterKnife.bind(activity));
            created.setUpView();
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activity instanceof IActivityCreated) {
            cache.remove(activity).unbind();
        }
    }
}