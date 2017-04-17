package com.summer.lib.base;

import android.app.Application;

import com.summer.lib.model.di.ComponentHolder;

/**
 * Created by kevin.bai on 2017/4/16.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ComponentHolder.initComponent(this);
    }
}
