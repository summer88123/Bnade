package com.summer.bnade.select;

import android.text.TextUtils;

import com.summer.bnade.base.BasePresenter;
import com.summer.bnade.data.BnadeRepo;
import com.summer.bnade.data.HistoryRealmRepo;
import com.summer.bnade.select.entity.RealmSelectVO;
import com.summer.lib.model.entity.Realm;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * Created by kevin.bai on 2017/4/20.
 */

class RealmSelectPresenter extends BasePresenter<RealmSelectContract.View> implements RealmSelectContract
        .Presenter {

    private final HistoryRealmRepo mRealmRepo;

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
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Realm>>() {
                        @Override
                        public void accept(@NonNull List<Realm> realms) throws Exception {
                            mView.show(realms);
                        }
                    });
        }
    }

    @Override
    public void load() {
        mRepo.getAllRealm(false)
                .zipWith(mRealmRepo.getAll(), new BiFunction<List<Realm>, List<String>, RealmSelectVO>() {
                    @Override
                    public RealmSelectVO apply(@NonNull List<Realm> realms, @NonNull List<String> strings) throws
                            Exception {
                        return new RealmSelectVO(realms, strings);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RealmSelectVO>() {
                    @Override
                    public void accept(@NonNull RealmSelectVO vo) throws Exception {
                        mView.show(vo);
                    }
                });
    }

    @Override
    public void selectHistory(String realm) {
        mRepo.getRealm(realm)
                .subscribe(new Consumer<Realm>() {
                    @Override
                    public void accept(@NonNull Realm realm) throws Exception {
                        mView.selected(realm);
                    }
                });
    }
}
