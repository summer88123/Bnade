package com.summer.bnade.result.single;


import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin.bai on 2017/4/14.
 */
@Module
class ItemResultModule {
    private final ItemResultActivity mView;

    ItemResultModule(ItemResultActivity view) {
        this.mView = view;
    }

    @Provides
    ItemResultPageAdapter providePageAdapter() {
        return new ItemResultPageAdapter(mView, mView.getSupportFragmentManager(), mView.item, mView.realm);
    }

}
