package com.summer.bnade.base;

import com.summer.bnade.base.di.AppComponent;
import com.summer.bnade.base.di.DaggerAppComponent;
import com.summer.bnade.utils.Utils;

import javax.inject.Inject;

import dagger.android.support.DaggerApplication;

/**
 * Created by kevin.bai on 2017/4/30.
 */

public class BnadeApplication extends DaggerApplication {
    private AppComponent component;

    @Inject
    BaseActivityLifeCycleCallback mLifeCycleCallback;

    @Override
    public void onCreate() {
        component = (AppComponent) DaggerAppComponent
                .builder()
                .create(this);
        super.onCreate();
        Utils.init(this);
        registerActivityLifecycleCallbacks(mLifeCycleCallback);
    }

    @Override
    public AppComponent applicationInjector() {
        return component;
    }
}
