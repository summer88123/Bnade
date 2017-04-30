package com.summer.bnade.select;

import com.summer.bnade.data.RepoModule;
import com.summer.lib.model.di.ApplicationComponent;
import com.summer.lib.model.di.PreActivity;

import dagger.Component;

/**
 * Created by kevin.bai on 2017/4/20.
 */
@PreActivity
@Component(dependencies = {ApplicationComponent.class},
        modules = {RealmSelectModule.class, RepoModule.class})
interface RealmSelectComponent {
    void inject(RealmSelectActivity activity);

    void inject(RealmSelectFragment fragment);
}
