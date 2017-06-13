package com.summer.bnade.base;

import com.summer.bnade.base.di.ComponentHolder;
import com.summer.bnade.utils.Utils;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

/**
 * Created by kevin.bai on 2017/4/30.
 */

public class BnadeApplication extends DaggerApplication {

    @Inject
    BaseActivityLifeCycleCallback mLifeCycleCallback;

    @Override
    public void onCreate() {
        ComponentHolder.initComponent(this);
        super.onCreate();
        Utils.init(this);
        registerActivityLifecycleCallbacks(mLifeCycleCallback);
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return ComponentHolder.getComponent();
    }
}
