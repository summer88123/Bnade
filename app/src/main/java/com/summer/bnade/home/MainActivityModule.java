package com.summer.bnade.home;

import android.support.v4.app.FragmentManager;

import com.summer.bnade.base.BaseFragment;
import com.summer.bnade.personal.PersonalFragment;
import com.summer.bnade.player.PlayerItemFragment;
import com.summer.bnade.realm.RealmFragment;
import com.summer.bnade.search.SearchFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin.bai on 2017/10/7.
 */
@Module
public class MainActivityModule {
    @Provides
    FragmentManager provideFragmentManager(MainActivity activity) {
        return activity.getSupportFragmentManager();
    }

    @Provides
    @Search
    BaseFragment provideSearchFragment(FragmentManager fm) {
        return SearchFragment.getInstance(fm);
    }
    @Provides
    @Realm
    BaseFragment provideRealmFragment(FragmentManager fm) {
        return RealmFragment.getInstance(fm);
    }
    @Provides
    @PlayerItem
    BaseFragment providePlayerItemFragment(FragmentManager fm) {
        return PlayerItemFragment.getInstance(fm);
    }
    @Provides
    @Personal
    BaseFragment providePersonalFragment(FragmentManager fm) {
        return PersonalFragment.getInstance(fm);
    }
}
