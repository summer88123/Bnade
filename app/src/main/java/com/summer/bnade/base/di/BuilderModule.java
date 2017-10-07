package com.summer.bnade.base.di;

import com.summer.bnade.home.MainActivity;
import com.summer.bnade.home.MainActivityModule;
import com.summer.bnade.personal.PersonalFragment;
import com.summer.bnade.player.PlayerItemFragment;
import com.summer.bnade.realm.RealmFragment;
import com.summer.bnade.realm.rank.RealmRankFragment;
import com.summer.bnade.result.all.SearchResultActivity;
import com.summer.bnade.result.all.SearchResultModule;
import com.summer.bnade.result.single.HistoryFragment;
import com.summer.bnade.result.single.ItemResultActivity;
import com.summer.bnade.result.single.PriceFragment;
import com.summer.bnade.search.SearchFragment;
import com.summer.bnade.search.SearchModule;
import com.summer.bnade.select.RealmSelectActivity;
import com.summer.bnade.select.RealmSelectFragment;
import com.summer.bnade.token.WowTokenFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by kevin.bai on 2017/6/12.
 */
@Module
abstract class BuilderModule {
    
    @ContributesAndroidInjector
    abstract HistoryFragment historyFragmentInjector();

    @ContributesAndroidInjector
    abstract ItemResultActivity itemResultActivityInjector();

    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity mainActivityInjector();

    @ContributesAndroidInjector
    abstract PlayerItemFragment playerItemFragmentInjector();

    @ContributesAndroidInjector
    abstract PriceFragment priceFragmentInjector();

    @ContributesAndroidInjector
    abstract RealmRankFragment realmRankFragment();

    @ContributesAndroidInjector
    abstract RealmSelectActivity realmSelectActivity();

    @ContributesAndroidInjector
    abstract RealmSelectFragment realmSelectFragment();

    @ContributesAndroidInjector(modules = SearchModule.class)
    abstract SearchFragment searchFragmentInjector();

    @ContributesAndroidInjector(modules = SearchResultModule.class)
    abstract SearchResultActivity searchResultActivityInjector();

    @ContributesAndroidInjector
    abstract WowTokenFragment wowTokenFragmentInjector();

    @ContributesAndroidInjector
    abstract PersonalFragment personalFragmentInjector();

    @ContributesAndroidInjector
    abstract RealmFragment realmFragmentInjector();
}
