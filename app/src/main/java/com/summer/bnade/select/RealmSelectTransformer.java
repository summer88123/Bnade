package com.summer.bnade.select;

import android.text.TextUtils;

import com.summer.bnade.data.BnadeRepo;
import com.summer.bnade.data.HistoryRealmRepo;
import com.summer.bnade.select.entity.TypedRealm;
import com.summer.lib.model.entity.Realm;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin.bai on 2017/4/20.
 */

class RealmSelectTransformer {
    private final HistoryRealmRepo mRealmRepo;
    private final BnadeRepo mRepo;

    private Observable<RealmSelectUIModel> all;

    @Inject
    RealmSelectTransformer(BnadeRepo repo, HistoryRealmRepo realmRepo) {
        this.mRepo = repo;
        this.mRealmRepo = realmRepo;
        all = Observable.concat(mRealmRepo.getAll().map(TypedRealm::USED), mRepo.getAllRealm(false)
                .map(TypedRealm::NORMAL))
                .toList().toObservable()
                .map(RealmSelectUIModel::success)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public ObservableTransformer<CharSequence, RealmSelectUIModel> filter() {
        return upstream -> upstream.flatMap(s -> {
            if (TextUtils.isEmpty(s)) {
                return all;
            } else {
                return mRepo.getRealmsByName(s)
                        .map(TypedRealm::NORMAL)
                        .toList().toObservable()
                        .map(RealmSelectUIModel::success)
                        .observeOn(AndroidSchedulers.mainThread());
            }
        });
    }


    public ObservableTransformer<Object, RealmSelectUIModel> load() {
        return upstream -> upstream.flatMap(ignore -> all);
    }

    void remove(Realm item) {
        mRealmRepo.remove(item).subscribe();
    }

}
