package com.summer.bnade.data;

import android.content.SharedPreferences;

import com.summer.lib.model.di.PreActivity;
import com.summer.lib.model.utils.RealmHelper;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin.bai on 2017/4/30.
 */
@Module
public class RepoModule {
    @PreActivity
    @Provides
    public HistoryRealmRepo provideHistoryRealmRepo(SharedPreferences sp, RealmHelper realmHelper) {
        return new HistoryRealmRepo(sp, realmHelper);
    }

    @PreActivity
    @Provides
    public HistorySearchRepo provideHistorySearchRepo(SharedPreferences sp) {
        return new HistorySearchRepo(sp);
    }
}
