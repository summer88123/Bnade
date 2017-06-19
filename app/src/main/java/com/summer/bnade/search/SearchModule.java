package com.summer.bnade.search;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin.bai on 2017/4/12.
 */
@Module
public class SearchModule {

    @Provides
    SearchContract.Presenter providePresenter(SearchPresenter presenter) {
        return presenter;
    }

    @Provides
    SearchContract.View provideSearchView(SearchFragment view) {
        return view;
    }

    @Provides
    OnTabClickListener provideHistoryAdapter(SearchFragment view){
        return view;
    }

}
