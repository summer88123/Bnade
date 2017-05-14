package com.summer.bnade.di;

import android.app.Application;

import com.summer.bnade.data.RepoModule;
import com.summer.lib.model.di.BnadeModule;

/**
 * Created by kevin.bai on 2017/4/4.
 */

public class ComponentHolder {
    private static AppComponent component;

    private ComponentHolder() {
        throw new RuntimeException("不能实例化ComponentHolder");
    }

    public static void initComponent(Application application) {
        component = DaggerAppComponent
                .builder()
                .bnadeModule(new BnadeModule())
                .repoModule(new RepoModule(application))
                .build();
    }

    public static AppComponent getComponent() {
        return component;
    }

}
