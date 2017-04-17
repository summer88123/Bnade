package com.summer.bnade.realmrank;

import com.summer.bnade.base.BasePresenter;
import com.summer.bnade.data.BnadeRepo;
import com.summer.lib.model.entity.AuctionRealm;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by kevin.bai on 2017/4/16.
 */

public class RealmRankPresenter extends BasePresenter<RealmRankContract.View> implements RealmRankContract.Presenter {

    @Inject
    public RealmRankPresenter(RealmRankContract.View view, BnadeRepo repo, RealmRankAdapter adapter) {
        super(view, repo);
        mView.setDependency(adapter);
    }

    @Override
    public void load() {
        mRepo.getAuctionRealm()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        mView.setRefreshing(true);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<AuctionRealm>>() {
                    @Override
                    public void accept(@NonNull List<AuctionRealm> auctionRealms) throws Exception {
                        mView.show(auctionRealms);
                    }
                });
    }
}
