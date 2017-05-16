package com.summer.bnade.player;

import com.summer.bnade.base.mvp.BasePresenter;
import com.summer.bnade.data.BnadeRepo;
import com.summer.lib.model.entity.Auction;
import com.summer.lib.model.entity.Realm;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by kevin.bai on 2017/4/23.
 */

public class PlayerItemPresenter extends BasePresenter<PlayerItemContract.View> implements PlayerItemContract
        .Presenter {

    @Inject
    PlayerItemPresenter(PlayerItemContract.View view, BnadeRepo repo) {
        super(view, repo);
    }

    @Override
    public void search(String player, Realm realm) {
        mRepo.getAuctionRealmOwner(realm.getId(), player)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Auction>>() {
                    @Override
                    public void accept(@NonNull List<Auction> auctions) throws Exception {
                        mView.showList(auctions);
                    }
                }, mErrorHandler);
    }
}
