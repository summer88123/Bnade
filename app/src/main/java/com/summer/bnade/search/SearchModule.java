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
    SearchContract.Presenter providePresenter(SearchPresenter presenter) {
        return presenter;
    }

    @Provides
    SearchContract.View provideSearchView() {
        return mView;
    }

    @Provides
    FuzzyItemAdapter provideFuzzyItemAdapter() {
        return new FuzzyItemAdapter();
    }

    @Provides
    HistoryAdapter provideHistoryAdapter(SearchContract.View view){
        return new HistoryAdapter(view);
    }

    @Provides
    HotSearchAdapter provideHotSearchAdapter(SearchContract.View view){
        return new HotSearchAdapter(view);
    }
}
