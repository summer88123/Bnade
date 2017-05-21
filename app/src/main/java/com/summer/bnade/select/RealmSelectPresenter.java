package com.summer.bnade.select;

import android.text.TextUtils;

import com.summer.bnade.base.mvp.BasePresenter;
import com.summer.bnade.data.BnadeRepo;
import com.summer.bnade.data.HistoryRealmRepo;
import com.summer.bnade.select.entity.TypedRealm;
import com.summer.lib.model.entity.Realm;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * Created by kevin.bai on 2017/4/20.
 */

class RealmSelectPresenter extends BasePresenter<RealmSelectContract.View> implements RealmSelectContract
        .Presenter {

    private final HistoryRealmRepo mRealmRepo;

    private Function<Realm, TypedRealm> mapUsed = TypedRealm::USED;

    private Function<Realm, TypedRealm> mapNormal = TypedRealm::NORMAL;

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
                    .subscribe(mView::show);
        }
    }

    @Override
    public void load() {
        Observable.concat(mRealmRepo.getAll().map(mapUsed), mRepo.getAllRealm(false).map(mapNormal))
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mView::show);
    }

    @Override
    public void remove(Realm item) {
        mRealmRepo.remove(item).subscribe();
    }
}
