package com.summer.bnade.result.all;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin.bai on 2017/4/14.
 */
@Module
class SearchResultModule {
    final SearchResultContract.View mView;

    public SearchResultModule(SearchResultContract.View view) {
        this.mView = view;
    }

    @Provides
    SearchResultContract.View provideResultView() {
        return mView;
    }

    @Provides
    SearchResultContract.Presenter providePresenter(SearchResultPresenter presenter) {
        return presenter;
    }

    @Provides
    SearchResultAdapter provideAdapter() {
        return new SearchResultAdapter();
    }

}
