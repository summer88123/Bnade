package com.summer.bnade.select;

import com.summer.bnade.base.di.AppComponent;
import com.summer.bnade.base.di.PreActivity;

import dagger.Component;

/**
 * Created by kevin.bai on 2017/4/20.
 */
@PreActivity
@Component(dependencies = {AppComponent.class},
        modules = {RealmSelectModule.class})
interface RealmSelectComponent {
    void inject(RealmSelectActivity activity);

    void inject(RealmSelectFragment fragment);
}
