package com.summer.bnade.di;

import com.summer.bnade.base.BnadeApplication;
import com.summer.bnade.data.BnadeRepo;
import com.summer.bnade.data.HistoryRealmRepo;
import com.summer.bnade.data.HistorySearchRepo;
import com.summer.bnade.data.RepoModule;
import com.summer.lib.model.di.BnadeModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * 处理全局单例的依赖对象
 * Created by kevin.bai on 2017/5/4.
 */
@Singleton
@Component(modules = {RepoModule.class, BnadeModule.class})
public interface AppComponent {
    void inject(BnadeApplication application);

    BnadeRepo bnadeRepo();

    HistorySearchRepo historySearchRepo();

    HistoryRealmRepo historyRealmRepo();
}
