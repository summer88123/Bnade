package com.summer.bnade.search;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin.bai on 2017/4/12.
 */
@Module
public class SearchModule {
    private final SearchFragment mView;


    public SearchModule(SearchFragment view) {
        mView = view;
    }

    @Provides
    SearchContract.View provideSearchView() {
        return mView;
    }

    @Provides
    SearchContract.Presenter provideSearchPresenter(SearchPresenter presenter){
        return presenter;
    }

    @Provides
    FuzzyItemAdapter provideFuzzyItemAdapter() {
        return new FuzzyItemAdapter();
    }

    @Provides
    HistoryAdapter provideHistoryAdapter(SearchPresenter presenter){
        return new HistoryAdapter(presenter);
    }

    @Provides
    HotSearchAdapter provideHotSearchAdapter(SearchPresenter presenter){
        return new HotSearchAdapter(presenter);
    }
}
