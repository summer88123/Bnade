package com.summer.bnade.data;

import android.content.SharedPreferences;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;

/**
 * Created by kevin.bai on 2017/4/16.
 */

public class HistorySearchRepo {
    private static final String HISTORY_KEY = "KEY_HISTORY_SEARCH";
    private Set<String> cache;
    private SharedPreferences mSp;
    private SharedPreferences.Editor mEditor;

    HistorySearchRepo(SharedPreferences sp) {
        this.mSp = sp;
        this.mEditor = mSp.edit();
    }

    public Completable add(final String history) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                initCache();
                cache.add(history);
                save();
            }
        });
    }

    public Completable clear() {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                initCache();
                cache.clear();
                save();
            }
        });
    }

    public Observable<String> getHistories() {
        return Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                initCache();
                return Observable.fromIterable(cache);
            }
        });

    }

    private void initCache() {
        if (cache == null) {
            this.cache = mSp.getStringSet(HISTORY_KEY, new LinkedHashSet<String>());
        }
    }

    private void save() {
        mEditor.putStringSet(HISTORY_KEY, cache).apply();
    }
}
