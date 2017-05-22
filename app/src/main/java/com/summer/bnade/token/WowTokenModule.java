package com.summer.bnade.token;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin.bai on 2017/4/9.
 */
@Module
public class WowTokenModule {
    private final WowTokenFragment mView;

    public WowTokenModule(WowTokenFragment view) {
        mView = view;
    }

    @Provides
    WowTokenFragment provideWowTokenView(){
        return mView;
    }
}
