package com.summer.bnade.result.single;

import com.summer.bnade.result.all.SearchResultAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin.bai on 2017/4/14.
 */
@Module
class ItemResultModule {
    final ItemResultContract.View mView;

    public ItemResultModule(ItemResultContract.View view) {
        this.mView = view;
    }

    @Provides
    ItemResultContract.View provideResultView() {
        return mView;
    }

    @Provides
    SearchResultAdapter provideAdapter() {
        return new SearchResultAdapter();
    }

    @Provides
    ItemResultAdapter provideRealmItemAdapter(){
        return new ItemResultAdapter();
    }

}
