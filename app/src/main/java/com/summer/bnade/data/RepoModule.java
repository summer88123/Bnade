package com.summer.bnade.data;

import android.content.SharedPreferences;

import com.summer.lib.model.di.PreActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin.bai on 2017/4/30.
 */
@Module
public class RepoModule {
    @PreActivity
    @Provides
    public HistoryRealmRepo provideHistoryRealmRepo(SharedPreferences sp) {
        return new HistoryRealmRepo(sp);
    }

    @PreActivity
    @Provides
    public HistorySearchRepo provideHistorySearchRepo(SharedPreferences sp) {
        return new HistorySearchRepo(sp);
    }
}
