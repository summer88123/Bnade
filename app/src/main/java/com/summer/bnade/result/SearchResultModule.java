package com.summer.bnade.result;

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
    SearchResultAdapter provideAdapter() {
        return new SearchResultAdapter();
    }

    @Provides
    SearchRealmItemResultAdapter provideRealmItemAdapter(){
        return new SearchRealmItemResultAdapter();
    }

}
