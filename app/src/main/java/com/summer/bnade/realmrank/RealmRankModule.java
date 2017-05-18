package com.summer.bnade.realmrank;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin.bai on 2017/4/16.
 */
@Module
public class RealmRankModule {
    private final RealmRankFragment mFragment;

    public RealmRankModule(RealmRankFragment fragment) {
        mFragment = fragment;
    }

    @Provides
    RealmRankContract.Presenter providePresenter(RealmRankPresenter presenter) {
        return presenter;
    }

    @Provides
    RealmRankContract.View provideView() {
        return mFragment;
    }

    @Provides
    RealmRankAdapter provideAdapter() {
        return new RealmRankAdapter();
    }
}
