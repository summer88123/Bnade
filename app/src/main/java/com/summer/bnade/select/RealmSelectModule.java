package com.summer.bnade.select;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin.bai on 2017/4/20.
 */
@Module
public class RealmSelectModule {
    private final RealmSelectContract.View mView;

    public RealmSelectModule(RealmSelectContract.View view) {
        mView = view;
    }

    @Provides
    public RealmSelectContract.View provideView(){
        return mView;
    }

}
