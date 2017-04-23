package com.summer.lib.model.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.summer.lib.model.entity.Auction;
import com.summer.lib.model.entity.AuctionItem;
import com.summer.lib.model.entity.WowTokens;
import com.summer.lib.model.gson.AuctionItemParser;
import com.summer.lib.model.gson.AuctionParser;
import com.summer.lib.model.gson.WowTokensParser;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin.bai on 2017/4/3.
 */
@Module
public class ApplicationModule {
    final Application mApp;

    public ApplicationModule(Application app) {
        mApp = app;
    }

    @Singleton
    @Provides
    public Context provideContext() {
        return mApp;
    }

    @Singleton
    @Provides
    public Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapter(AuctionItem.class, AuctionItemParser.create())
                .registerTypeAdapter(WowTokens.class, WowTokensParser.create())
                .registerTypeAdapter(Auction.class, AuctionParser.create())
                .create();
    }

    @Provides
    public SharedPreferences provideSharedPreferences() {
        return mApp.getSharedPreferences("app", Activity.MODE_PRIVATE);
    }

}
