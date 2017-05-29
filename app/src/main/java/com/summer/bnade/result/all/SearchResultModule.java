package com.summer.bnade.result.all;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin.bai on 2017/4/14.
 */
@Module
class SearchResultModule {
    private final SearchResultActivity mView;

    SearchResultModule(SearchResultActivity view) {
        this.mView = view;
    }

    @Provides
    SearchResultActivity provideResultView() {
        return mView;
    }

    @Provides
    SearchResultAdapter provideAdapter() {
        return new SearchResultAdapter(mView.item);
    }

}
