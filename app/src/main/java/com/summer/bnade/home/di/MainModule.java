package com.summer.bnade.home.di;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin.bai on 2017/4/11.
 */
@Module
public class MainModule {
    private final AppCompatActivity mActivity;

    public MainModule(AppCompatActivity activity) {
        mActivity = activity;
    }

    @Provides
    public FragmentManager provideFragmentManager() {
        return mActivity.getSupportFragmentManager();
    }
}
