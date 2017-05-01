package com.summer.bnade.data;

import android.content.SharedPreferences;

import com.summer.lib.model.entity.Realm;
import com.summer.lib.model.utils.RealmHelper;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by kevin.bai on 2017/4/30.
 */

public class HistoryRealmRepo {
    private static final String HISTORY_REALM_KEY = "KEY_HISTORY_REALM";
    private final SharedPreferences sp;
    private final SharedPreferences.Editor mEditor;
    private final Set<String> cache;
    private final RealmHelper mRealmHelper;

    public HistoryRealmRepo(SharedPreferences sp, RealmHelper realmHelper) {
        this.mRealmHelper = realmHelper;
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

    public Completable clear() {
        return Completable.defer(new Callable<CompletableSource>() {
            @Override
            public CompletableSource call() throws Exception {
                cache.clear();
                save();
                return Completable.complete();
            }
        });
    }

    public Observable<Realm> getAll() {
        return Observable.fromIterable(new ArrayList<>(cache))
                .flatMap(new Function<String, ObservableSource<Realm>>() {
                    @Override
                    public ObservableSource<Realm> apply(@NonNull String s) throws Exception {
                        return mRealmHelper.getRealmsByName(s);
                    }
                });
    }
}
