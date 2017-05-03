package com.summer.lib.model.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.summer.lib.model.api.BnadeApi;
import com.summer.lib.model.utils.RealmHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by kevin.bai on 2017/4/3.
 */
@Singleton
@Component(modules = {ApplicationModule.class, BnadeApiModule.class})
public interface ApplicationComponent {

    Context context();

    BnadeApi bnadeApi();

    SharedPreferences sharedPreferences();

    RealmHelper realmHelper();
}
