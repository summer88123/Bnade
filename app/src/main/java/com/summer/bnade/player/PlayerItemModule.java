package com.summer.bnade.player;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin.bai on 2017/4/23.
 */
@Module
public class PlayerItemModule {
    private final PlayerItemFragment mView;

    public PlayerItemModule(PlayerItemFragment view) {
        mView = view;
    }

    @Provides
    PlayerItemFragment provideView() {
        return mView;
    }
}
