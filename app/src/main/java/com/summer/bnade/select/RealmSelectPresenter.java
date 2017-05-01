package com.summer.bnade.select;

import android.text.TextUtils;

import com.summer.bnade.base.BasePresenter;
import com.summer.bnade.data.BnadeRepo;
import com.summer.bnade.data.HistoryRealmRepo;
import com.summer.bnade.select.entity.TypedRealm;
import com.summer.lib.model.entity.Realm;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by kevin.bai on 2017/4/20.
 */

class RealmSelectPresenter extends BasePresenter<RealmSelectContract.View> implements RealmSelectContract
        .Presenter {

    private final HistoryRealmRepo mRealmRepo;

    private Function<Realm, TypedRealm> mapUsed = new Function<Realm, TypedRealm>() {
        @Override
        public TypedRealm apply(@NonNull Realm realm) throws Exception {
            return TypedRealm.USED(realm);
        }
    };

    private Function<Realm, TypedRealm> mapNormal = new Function<Realm, TypedRealm>() {
        @Override
        public TypedRealm apply(@NonNull Realm realm) throws Exception {
            return TypedRealm.NORMAL(realm);
        }
    };

    @Inject
    RealmSelectPresenter(RealmSelectContract.View view, BnadeRepo repo, HistoryRealmRepo realmRepo) {
        super(view, repo);
        this.mRealmRepo = realmRepo;
    }

    @Override
    public void filter(CharSequence s) {
        if (TextUtils.isEmpty(s)) {
            load();
        } else {
            mRepo.getRealmsByName(s)
                    .map(mapNormal)
                    .toList()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<TypedRealm>>() {
                        @Override
                        public void accept(@NonNull List<TypedRealm> realms) throws Exception {
                            mView.show(realms);
                        }
                    });
        }
    }

    @Override
    public void load() {
        Observable.merge(mRealmRepo.getAll().map(mapUsed), mRepo.getAllRealm(false).map(mapNormal))
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TypedRealm>>() {
                    @Override
                    public void accept(@NonNull List<TypedRealm> list) throws Exception {
                        mView.show(list);
                    }
                });
    }
}
