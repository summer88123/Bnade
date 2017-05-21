package com.summer.bnade.result.all;

import com.summer.lib.model.entity.Item;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin.bai on 2017/4/14.
 */
@Module
class SearchResultModule {
    private final SearchResultActivity mView;
    private final Item item;

    SearchResultModule(SearchResultActivity view, Item item) {
        this.item = item;
        this.mView = view;
    }

    @Provides
    SearchResultActivity provideResultView() {
        return mView;
    }

    @Provides
    SearchResultAdapter provideAdapter() {
        return new SearchResultAdapter(item);
    }

}
