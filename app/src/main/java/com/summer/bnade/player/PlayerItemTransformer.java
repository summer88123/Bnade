package com.summer.bnade.player;

import com.summer.bnade.data.BnadeRepo;

import javax.inject.Inject;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin.bai on 2017/4/23.
 */

public class PlayerItemTransformer {
    private BnadeRepo mRepo;

    @Inject
    PlayerItemTransformer(BnadeRepo repo) {
        this.mRepo = repo;
    }

    public ObservableTransformer<PlayerItemAction, PlayerItemUIModel> search() {
        return upstream -> upstream.flatMap(action ->
                mRepo.getAuctionRealmOwner(action.getSelect().getId(), action.getQuery())
                        .map(PlayerItemUIModel::success)
        ).onErrorReturn(e -> PlayerItemUIModel.failure(e.getMessage()))
                .startWith(PlayerItemUIModel.progress())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
