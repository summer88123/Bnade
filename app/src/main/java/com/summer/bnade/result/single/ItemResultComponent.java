package com.summer.bnade.result.single;

import com.summer.bnade.base.di.AppComponent;
import com.summer.bnade.base.di.PreActivity;

import dagger.Component;

/**
 * Created by kevin.bai on 2017/4/14.
 */
@PreActivity
@Component(dependencies = {AppComponent.class}, modules = {ItemResultModule.class})
interface ItemResultComponent {
    void inject(ItemResultActivity activity);
}
