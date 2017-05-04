package com.summer.bnade.base;

import android.app.Application;

import com.summer.bnade.di.ComponentHolder;

/**
 * Created by kevin.bai on 2017/4/30.
 */

public class BnadeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ComponentHolder.initComponent(this);
    }
}
