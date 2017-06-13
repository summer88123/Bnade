package com.summer.bnade.base.di;

import com.summer.bnade.base.BnadeApplication;

/**
 * Created by kevin.bai on 2017/4/4.
 */

public class ComponentHolder {
    private static AppComponent component;

    private ComponentHolder() {
        throw new RuntimeException("不能实例化ComponentHolder");
    }

    public static void initComponent(BnadeApplication application) {
        component = (AppComponent) DaggerAppComponent
                .builder()
                .create(application);
    }

    public static AppComponent getComponent() {
        return component;
    }

}
