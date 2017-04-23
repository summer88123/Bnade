package com.summer.bnade.player;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin.bai on 2017/4/23.
 */
@Module
public class PlayerItemModule {
    private final PlayerItemContract.View mView;

    public PlayerItemModule(PlayerItemContract.View view) {
        mView = view;
    }

    @Provides
    PlayerItemContract.View provideView() {
        return mView;
    }
}
