package com.summer.bnade.data;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import java.util.LinkedHashSet;
import java.util.Set;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by kevin.bai on 2017/4/16.
 */

public class HistorySearchRepo {
    private static final String HISTORY_KEY = "KEY_HISTORY_SEARCH";
    private Set<String> cache;
    private SharedPreferences mSp;
    private SharedPreferences.Editor mEditor;

    @SuppressLint("CommitPrefEdits")
    HistorySearchRepo(SharedPreferences sp) {
        this.mSp = sp;
        this.mEditor = mSp.edit();
    }

    public Completable add(final String history) {
        return Completable.fromAction(() -> {
            initCache();
            cache.add(history);
            save();
        });
    }

    public Completable clear() {
        return Completable.fromAction(() -> {
            initCache();
            cache.clear();
            save();
        });
    }

    public Observable<String> getHistories() {
        return Observable.defer(() -> {
            initCache();
            return Observable.fromIterable(cache);
        });

    }

    private void initCache() {
        if (cache == null) {
            this.cache = mSp.getStringSet(HISTORY_KEY, new LinkedHashSet<>());
        }
    }

    private void save() {
        mEditor.putStringSet(HISTORY_KEY, cache).apply();
    }
}
