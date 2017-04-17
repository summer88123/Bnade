package com.summer.bnade.home.di;

import android.support.v4.app.FragmentManager;

import com.summer.bnade.home.MainActivity;
import com.summer.bnade.realmrank.RealmRankContract;
import com.summer.bnade.realmrank.RealmRankModule;
import com.summer.bnade.search.SearchContract;
import com.summer.bnade.search.SearchFragment;
import com.summer.bnade.search.SearchModule;
import com.summer.bnade.token.WowTokenContract;
import com.summer.bnade.token.WowTokenModule;
import com.summer.lib.model.di.ApplicationComponent;
import com.summer.lib.model.di.PreActivity;

import dagger.Component;

/**
 * Created by kevin.bai on 2017/4/4.
 */
@PreActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                MainModule.class,
                WowTokenModule.class,
                SearchModule.class,
                RealmRankModule.class,
        })
public interface MainComponent {
    void inject(MainActivity activity);

    void inject(SearchFragment fragment);

    WowTokenContract.View wowTokenView();

    SearchContract.View searchView();

    RealmRankContract.View realmRankView();

    FragmentManager fragmentManager();
}
