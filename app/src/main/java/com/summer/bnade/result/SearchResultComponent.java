package com.summer.bnade.result;

import com.summer.lib.model.di.ApplicationComponent;
import com.summer.lib.model.di.PreActivity;

import dagger.Component;

/**
 * Created by kevin.bai on 2017/4/14.
 */
@PreActivity
@Component(dependencies = {ApplicationComponent.class}, modules = SearchResultModule.class)
interface SearchResultComponent {
    void inject(SearchResultActivity activity);
}
