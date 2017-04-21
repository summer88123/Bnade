package com.summer.bnade.select;

import com.summer.bnade.base.BasePresenter;
import com.summer.bnade.data.BnadeRepo;
import com.summer.lib.model.entity.Realm;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by kevin.bai on 2017/4/20.
 */

class RealmSelectPresenter extends BasePresenter<RealmSelectContract.View> implements RealmSelectContract
        .Presenter {

    @Inject
    RealmSelectPresenter(RealmSelectContract.View view, BnadeRepo repo) {
        super(view, repo);
    }

    @Override
    public void filter(CharSequence s) {
        mRepo.getRealmsByName(s)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Realm>>() {
                    @Override
                    public void accept(@NonNull List<Realm> realms) throws Exception {
                        mView.show(realms);
                    }
                });
    }

    @Override
    public void load() {
        mRepo.getAllRealm(true)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Realm>>() {
                    @Override
                    public void accept(@NonNull List<Realm> realms) throws Exception {
                        mView.show(realms);
                    }
                });
    }
}
