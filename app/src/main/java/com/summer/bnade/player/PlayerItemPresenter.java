package com.summer.bnade.player;

import com.summer.bnade.base.mvp.BasePresenter;
import com.summer.bnade.data.BnadeRepo;
import com.summer.lib.model.entity.Realm;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

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
                .subscribe(auctions -> mView.showList(auctions), mErrorHandler);
    }
}
