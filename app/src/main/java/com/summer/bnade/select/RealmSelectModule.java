package com.summer.bnade.select;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin.bai on 2017/4/20.
 */
@Module
class RealmSelectModule {
    private final RealmSelectContract.View mView;

    RealmSelectModule(RealmSelectContract.View view) {
        mView = view;
    }

    @Provides
    RealmSelectContract.View provideView(){
        return mView;
    }

}
