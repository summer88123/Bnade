package com.summer.lib.model.di;

import android.app.Application;

import com.summer.lib.base.BaseActivity;

/**
 * Created by kevin.bai on 2017/4/4.
 */

public class ComponentHolder {
    private static ApplicationComponent component;

    public static void initComponent(Application application){
        component = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(application))
                .bnadeApiModule(new BnadeApiModule())
                .build();
    }

    public static ApplicationComponent getComponent() {
        return component;
    }

    public static void setComponent(ApplicationComponent component) {
        ComponentHolder.component = component;
    }
}
