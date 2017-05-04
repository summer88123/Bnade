package com.summer.bnade.result.all;

import com.summer.bnade.di.AppComponent;
import com.summer.bnade.di.PreActivity;

import dagger.Component;

/**
 * Created by kevin.bai on 2017/4/14.
 */
@PreActivity
@Component(dependencies = {AppComponent.class}, modules = SearchResultModule.class)
interface SearchResultComponent {
    void inject(SearchResultActivity activity);
}
