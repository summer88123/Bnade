package com.summer.bnade.select;

import dagger.Module;

/**
 * Created by kevin.bai on 2017/4/20.
 */
@Module
class RealmSelectModule {
    private final RealmSelectFragment mView;

    RealmSelectModule(RealmSelectFragment view) {
        mView = view;
    }

    RealmAdapter provideAdapter(RealmSelectTransformer presenter){
        return new RealmAdapter(mView, presenter);
    }

}
