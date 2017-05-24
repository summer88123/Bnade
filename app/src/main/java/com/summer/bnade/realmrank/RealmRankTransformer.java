package com.summer.bnade.realmrank;

import com.summer.bnade.data.BnadeRepo;
import com.summer.lib.model.entity.AuctionRealm;

import java.util.Collections;
import java.util.Comparator;

import javax.inject.Inject;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;

class RealmRankTransformer {
    private BnadeRepo mRepo;

    @Inject
    RealmRankTransformer(BnadeRepo repo) {
        this.mRepo = repo;
    }

    ObservableTransformer<AuctionRealm.SortType, RealmRankUIModel> load() {
        return upstream -> upstream.flatMap(sortType -> mRepo.getAuctionRealm(false)
                .map(list -> {
                    Collections.sort(list, getComparator(sortType));
                    return list;
                })
                .map(RealmRankUIModel::success)
                .observeOn(AndroidSchedulers.mainThread())
                .startWith(RealmRankUIModel.progress())
                .onErrorReturn(e -> RealmRankUIModel.failure(e.getMessage())));
    }

    ObservableTransformer<AuctionRealm.SortType, RealmRankUIModel> sort() {
        return upstream -> upstream.flatMap(sortType -> mRepo.getAuctionRealm(true)
                .map(list -> {
                    Collections.sort(list, getComparator(sortType));
                    return list;
                })
                .map(RealmRankUIModel::success)
                .observeOn(AndroidSchedulers.mainThread()));
    }

    private Comparator<? super AuctionRealm> getComparator(final AuctionRealm.SortType sortType) {
        return (l, r) -> {
            switch (sortType) {
                case TotalUp:
                    return l.getAuctionQuantity() - r.getAuctionQuantity();
                case PlayerUp:
                    return l.getPlayerQuantity() - r.getPlayerQuantity();
                case PlayerDown:
                    return r.getPlayerQuantity() - l.getPlayerQuantity();
                case ItemUp:
                    return l.getItemQuantity() - r.getItemQuantity();
                case ItemDown:
                    return r.getItemQuantity() - l.getItemQuantity();
                case TimeUp:
                    return (int) (l.getLastModified() - r.getLastModified());
                case TimeDown:
                    return (int) (r.getLastModified() - l.getLastModified());
                case TotalDown:
                default:
                    return r.getAuctionQuantity() - l.getAuctionQuantity();
            }
        };
    }
}
