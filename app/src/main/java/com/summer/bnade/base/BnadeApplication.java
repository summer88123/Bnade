package com.summer.bnade.base;

import android.app.Application;

import com.summer.bnade.base.di.ComponentHolder;
import com.summer.bnade.utils.Utils;

import javax.inject.Inject;

/**
 * Created by kevin.bai on 2017/4/30.
 */

public class BnadeApplication extends Application {

    @Inject
    BaseActivityLifeCycleCallback mLifeCycleCallback;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        ComponentHolder.initComponent(this);
        registerActivityLifecycleCallbacks(mLifeCycleCallback);
    }
}
