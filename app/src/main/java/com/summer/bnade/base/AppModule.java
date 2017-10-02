package com.summer.bnade.base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.summer.lib.model.di.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin.bai on 2017/4/30.
 */
@Module
public class AppModule {

    @Singleton
    @Provides
    SharedPreferences provideSharedPreferences(BnadeApplication app) {
        return app.getSharedPreferences("app", Activity.MODE_PRIVATE);
    }

    @Provides
    @Application
    Context provideContext(BnadeApplication app) {
        return app.getApplicationContext();
    }
}
