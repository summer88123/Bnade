package com.summer.lib.model.repo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.WorkerThread;

import com.summer.lib.model.di.Application;
import com.summer.lib.model.di.BnadeModule;
import com.summer.lib.model.entity.Realm;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kevin.bai on 2017/4/16.
 */

public class RealmRepo {

    private List<Realm> mRealms;
    private Gson mGson;
    private AssetManager as;

    @Inject
    RealmRepo(@Application Context context, @Named(BnadeModule.BNADE) Gson gson) {
        this.mGson = gson;
        this.as = context.getAssets();
    }

    @WorkerThread
    private synchronized List<Realm> getRealms() throws IOException {
        if (mRealms == null) {
            this.mRealms = mGson.fromJson(new InputStreamReader(as.open("realm.json")), new TypeToken<List<Realm>>() {
            }.getType());
        }
        return mRealms;
    }

    public Observable<Realm> getAllRealm(final boolean hasAllItem) {
        return Observable
                .defer(new Callable<ObservableSource<Realm>>() {
                    @Override
                    public ObservableSource<Realm> call() throws Exception {
                        return Observable.fromIterable(Collections.unmodifiableList(getRealms()));
                    }
                })
                .compose(new ObservableTransformer<Realm, Realm>() {
                    @Override
                    public ObservableSource<Realm> apply(@NonNull Observable<Realm> upstream) {
                        if (hasAllItem) {
                            return upstream;
                        }
                        return upstream.skip(1);
                    }
                }).subscribeOn(Schedulers.io());
    }

    public Observable<Realm> getRealmsByName(final String name) {
        return getAllRealm(false)
                .filter(new Predicate<Realm>() {
                    @Override
                    public boolean test(@NonNull Realm realm) throws Exception {
                        return realm.getName().equals(name);
                    }
                });
    }

    public Single<Realm> getRealmById(final long id) {
        return getAllRealm(false)
                .filter(new Predicate<Realm>() {
                    @Override
                    public boolean test(@NonNull Realm realm) throws Exception {
                        return realm.getId() == id;
                    }
                })
                .single(Realm.unKnowInstance(id));
    }
}
