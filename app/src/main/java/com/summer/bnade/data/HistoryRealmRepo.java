package com.summer.bnade.data;

import android.content.SharedPreferences;

import com.summer.lib.model.entity.Realm;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kevin.bai on 2017/4/30.
 */

public class HistoryRealmRepo {
    private static final String HISTORY_REALM_KEY = "KEY_HISTORY_REALM";
    private final SharedPreferences sp;
    private final SharedPreferences.Editor mEditor;
    private final Set<String> cache;

    public HistoryRealmRepo(SharedPreferences sp){
        this.sp = sp;
        this.mEditor = sp.edit();
        cache = sp.getStringSet(HISTORY_REALM_KEY, new LinkedHashSet<String>());
    }

    public Completable add(final Realm realm) {

        return Completable.defer(new Callable<CompletableSource>() {
            @Override
            public CompletableSource call() throws Exception {
                cache.add(realm.getConnected());
                save();
                return Completable.complete();
            }
        });
    }

    private void save() {
        mEditor.putStringSet(HISTORY_REALM_KEY, cache).commit();
    }

    public Completable clear(){
        return Completable.defer(new Callable<CompletableSource>() {
            @Override
            public CompletableSource call() throws Exception {
                cache.clear();
                save();
                return Completable.complete();
            }
        });
    }

    public Single<List<String>> getAll(){
        return Single.fromCallable(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                return new ArrayList<>(cache);
            }
        }).subscribeOn(Schedulers.io());
    }
}
