package com.summer.bnade.base.di;

import com.summer.bnade.base.BaseModule;
import com.summer.bnade.base.BnadeApplication;
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

    public static void initComponent(BnadeApplication application) {
        component = DaggerAppComponent
                .builder()
                .baseModule(new BaseModule())
                .bnadeModule(new BnadeModule())
                .repoModule(new RepoModule(application))
                .build();
        component.inject(application);
    }

    public static AppComponent getComponent() {
        return component;
    }

}
