package com.summer.bnade.result.single;

import com.summer.lib.model.di.ApplicationComponent;
import com.summer.lib.model.di.PreActivity;

import dagger.Component;

/**
 * Created by kevin.bai on 2017/4/14.
 */
@PreActivity
@Component(dependencies = {ApplicationComponent.class}, modules = ItemResultModule.class)
interface ItemResultComponent {
    void inject(ItemResultActivity activity);
}
