package com.summer.bnade.home;

import com.summer.bnade.di.AppComponent;
import com.summer.bnade.di.PreActivity;
import com.summer.bnade.player.PlayerItemContract;
import com.summer.bnade.player.PlayerItemModule;
import com.summer.bnade.realmrank.RealmRankContract;
import com.summer.bnade.realmrank.RealmRankFragment;
import com.summer.bnade.realmrank.RealmRankModule;
import com.summer.bnade.search.SearchContract;
import com.summer.bnade.search.SearchFragment;
import com.summer.bnade.search.SearchModule;
import com.summer.bnade.token.WowTokenContract;
import com.summer.bnade.token.WowTokenModule;

import dagger.Component;

/**
 * Created by kevin.bai on 2017/4/4.
 */
@PreActivity
@Component(
        dependencies = AppComponent.class,
        modules = {
                MainModule.class,
                WowTokenModule.class,
                SearchModule.class,
                RealmRankModule.class,
                PlayerItemModule.class,
        })
public interface MainComponent {
    void inject(MainActivity activity);

    void inject(SearchFragment fragment);

    void inject(RealmRankFragment fragment);

    WowTokenContract.View wowTokenView();

    SearchContract.View searchView();

    RealmRankContract.View realmRankView();

    PlayerItemContract.View playerItemView();

}
