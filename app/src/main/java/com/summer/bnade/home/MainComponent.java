package com.summer.bnade.home;

import com.summer.bnade.base.di.AppComponent;
import com.summer.bnade.base.di.PreActivity;
import com.summer.bnade.player.PlayerItemFragment;
import com.summer.bnade.player.PlayerItemModule;
import com.summer.bnade.realmrank.RealmRankFragment;
import com.summer.bnade.realmrank.RealmRankModule;
import com.summer.bnade.search.SearchContract;
import com.summer.bnade.search.SearchFragment;
import com.summer.bnade.search.SearchModule;
import com.summer.bnade.token.WowTokenFragment;
import com.summer.bnade.token.WowTokenModule;

import dagger.Component;

/**
 * Created by kevin.bai on 2017/4/4.
 */
@PreActivity
@Component(
        dependencies = AppComponent.class,
        modules = {
                WowTokenModule.class,
                SearchModule.class,
                RealmRankModule.class,
                PlayerItemModule.class,
        })
public interface MainComponent {
    void inject(MainActivity activity);

    void inject(SearchFragment fragment);

    WowTokenFragment wowTokenView();

    SearchContract.View searchView();

    RealmRankFragment realmRankView();

    PlayerItemFragment playerItemView();
}
