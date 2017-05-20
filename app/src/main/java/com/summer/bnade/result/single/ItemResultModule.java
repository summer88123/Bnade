package com.summer.bnade.result.single;


import com.summer.lib.model.entity.Item;
import com.summer.lib.model.entity.Realm;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin.bai on 2017/4/14.
 */
@Module
class ItemResultModule {
    private final ItemResultActivity mView;
    private Item item;
    private Realm realm;

    ItemResultModule(ItemResultActivity view, Item item, Realm realm) {
        this.item = item;
        this.realm = realm;
        this.mView = view;
    }

    @Provides
    ItemResultPageAdapter providePageAdapter() {
        return new ItemResultPageAdapter(mView, mView.getSupportFragmentManager(), item, realm);
    }

}
