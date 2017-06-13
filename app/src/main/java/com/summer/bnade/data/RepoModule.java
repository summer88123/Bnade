package com.summer.bnade.data;

import android.app.Activity;
import android.content.SharedPreferences;

import com.summer.bnade.base.BnadeApplication;
import com.summer.lib.model.api.BnadeApi;
import com.summer.lib.model.utils.RealmHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin.bai on 2017/4/30.
 */
@Module
public class RepoModule {

    @Singleton
    @Provides
    SharedPreferences provideSharedPreferences(BnadeApplication app) {
        return app.getSharedPreferences("app", Activity.MODE_PRIVATE);
    }

    @Singleton
    @Provides
    HistoryRealmRepo provideHistoryRealmRepo(SharedPreferences sp, RealmHelper realmHelper) {
        return new HistoryRealmRepo(sp, realmHelper);
    }

    @Singleton
    @Provides
    HistorySearchRepo provideHistorySearchRepo(SharedPreferences sp) {
        return new HistorySearchRepo(sp);
    }

    @Singleton
    @Provides
    BnadeRepo provideBnadeRepo(BnadeApi api, RealmHelper helper){
        return new BnadeRepo(api, helper);
    }
}
