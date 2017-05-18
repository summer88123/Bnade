package com.summer.bnade.token;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin.bai on 2017/4/9.
 */
@Module
public class WowTokenModule {
    private final WowTokenContract.View mView;

    public WowTokenModule(WowTokenContract.View view) {
        mView = view;
    }

    @Provides
    WowTokenContract.Presenter providePresenter(WowTokenPresenter presenter) {
        return presenter;
    }

    @Provides
    WowTokenContract.View provideWowTokenView(){
        return mView;
    }
}
