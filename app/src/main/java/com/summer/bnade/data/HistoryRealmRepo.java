package com.summer.bnade.data;

import android.content.SharedPreferences;

import com.summer.lib.model.entity.Realm;
import com.summer.lib.model.utils.RealmHelper;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kevin.bai on 2017/4/30.
 */

public class HistoryRealmRepo {
    private static final String HISTORY_REALM_KEY = "KEY_HISTORY_REALM";
    private static final String LAST_REALM_KEY = "KEY_LAST_REALM";
    private static final String NONE = "NONE";
    private final SharedPreferences.Editor mEditor;
    private final RealmHelper mRealmHelper;
    private final SharedPreferences sp;
    private Set<String> cache;

    public HistoryRealmRepo(SharedPreferences sp, RealmHelper realmHelper) {
        this.mRealmHelper = realmHelper;
        this.sp = sp;
        this.mEditor = sp.edit();
    }

    public Completable add(final Realm realm) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                initCache();
                cache.add(realm.getConnected());
                mEditor.putString(LAST_REALM_KEY, realm.getConnected()).apply();
                save();
            }
        }).subscribeOn(Schedulers.io());
    }

    public Maybe<Realm> last() {
        return Single.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return sp.getString(LAST_REALM_KEY, NONE);
            }
        }).flatMapMaybe(new Function<String, MaybeSource<Realm>>() {
            @Override
            public MaybeSource<Realm> apply(@NonNull String s) throws Exception {
                return mRealmHelper.getRealmsByName(s).firstElement();
            }
        }).subscribeOn(Schedulers.io());
    }

    public Completable clear() {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                initCache();
                cache.clear();
                save();
            }
        }).subscribeOn(Schedulers.io());
    }

    public Observable<Realm> getAll() {
        return Observable
                .defer(new Callable<ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> call() throws Exception {
                        initCache();
                        return Observable.fromIterable(cache);
                    }
                })
                .flatMap(new Function<String, ObservableSource<Realm>>() {
                    @Override
                    public ObservableSource<Realm> apply(@NonNull String s) throws Exception {
                        return mRealmHelper.getRealmsByName(s);
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public Completable remove(final Realm item) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                initCache();
                cache.remove(item.getConnected());
                save();
            }
        }).subscribeOn(Schedulers.io());
    }

    private void initCache() {
        if (cache == null) {
            cache = sp.getStringSet(HISTORY_REALM_KEY, new LinkedHashSet<String>());
        }
    }

    private void save() {
        mEditor.putStringSet(HISTORY_REALM_KEY, cache).commit();
    }
}
