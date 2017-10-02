package com.summer.bnade.data;

import android.content.SharedPreferences;

import com.summer.lib.model.entity.Realm;
import com.summer.lib.model.repo.RealmRepo;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kevin.bai on 2017/4/30.
 */
@Singleton
public class HistoryRealmRepo {
    private static final String HISTORY_REALM_KEY = "KEY_HISTORY_REALM";
    private static final String LAST_REALM_KEY = "KEY_LAST_REALM";
    private static final String NONE = "NONE";
    private final SharedPreferences.Editor mEditor;
    private final RealmRepo mRealmRepo;
    private final SharedPreferences sp;
    private Set<String> cache;

    @Inject
    public HistoryRealmRepo(SharedPreferences sp, RealmRepo realmRepo) {
        this.mRealmRepo = realmRepo;
        this.sp = sp;
        this.mEditor = sp.edit();
    }

    public Completable add(final Realm realm) {
        return Completable.fromAction(() -> {
            initCache();
            cache.add(realm.getName());
            mEditor.putString(LAST_REALM_KEY, realm.getName()).apply();
            save();
        }).subscribeOn(Schedulers.io());
    }

    public Maybe<Realm> last() {
        return Single.fromCallable(() -> sp.getString(LAST_REALM_KEY, NONE))
                .flatMapMaybe(s -> mRealmRepo.getRealmsByName(s).firstElement()).subscribeOn(Schedulers.io());
    }

    public Completable clearLast() {
        return Completable.fromAction(() -> mEditor.remove(LAST_REALM_KEY).apply());
    }

    public Completable clear() {
        return Completable.fromAction(() -> {
            initCache();
            cache.clear();
            save();
        }).subscribeOn(Schedulers.io());
    }

    public Observable<Realm> getAll() {
        return Observable
                .defer(() -> {
                    initCache();
                    return Observable.fromIterable(cache);
                })
                .flatMap(mRealmRepo::getRealmsByName)
                .subscribeOn(Schedulers.io());
    }

    public Completable remove(final Realm item) {
        return Completable.fromAction(() -> {
            initCache();
            cache.remove(item.getName());
            save();
        }).subscribeOn(Schedulers.io());
    }

    private void initCache() {
        if (cache == null) {
            cache = sp.getStringSet(HISTORY_REALM_KEY, new LinkedHashSet<>());
        }
    }

    private void save() {
        mEditor.putStringSet(HISTORY_REALM_KEY, cache).commit();
    }
}
