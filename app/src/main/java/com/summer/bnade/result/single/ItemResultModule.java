package com.summer.bnade.result.single;


import com.summer.bnade.di.PreActivity;

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
    ItemResultContract.View provideResultView() {
        return mView;
    }

    @Provides
    ItemResultContract.Presenter providePresenter(ItemResultPresenter presenter) {
        return presenter;
    }

    @PreActivity
    @Provides
    PriceFragment providePriceFragment() {
        return PriceFragment.getInstance(mView.getSupportFragmentManager());
    }

    @PreActivity
    @Provides
    HistoryFragment provideHistoryFragment() {
        return HistoryFragment.getInstance(mView.getSupportFragmentManager());
    }

    @Provides
    ItemResultAdapter provideRealmItemAdapter() {
        return new ItemResultAdapter();
    }

    @Provides
    PageAdapter providePageAdapter(PriceFragment priceFragment, HistoryFragment historyFragment) {
        return new PageAdapter(mView, mView.getSupportFragmentManager(), priceFragment, historyFragment);
    }

}
