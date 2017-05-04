package com.summer.bnade.result.single;

import com.summer.bnade.di.AppComponent;
import com.summer.bnade.di.PreActivity;

import dagger.Component;

/**
 * Created by kevin.bai on 2017/4/14.
 */
@PreActivity
@Component(dependencies = {AppComponent.class}, modules = ItemResultModule.class)
interface ItemResultComponent {
    void inject(ItemResultActivity activity);

    void inject(PriceFragment fragment);
}
