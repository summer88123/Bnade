package com.summer.bnade.realmrank;

import com.summer.bnade.data.BnadeRepo;
import com.summer.lib.model.entity.AuctionRealm;

import java.util.Collections;
import java.util.Comparator;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

class RealmRankTransformer {
    private BnadeRepo mRepo;
    private Subject<AuctionRealm.SortType> subject = BehaviorSubject.create();
    private Observable<RealmRankUIModel> publisher =
            subject.flatMap(ignore -> mRepo.getAuctionRealm()
                    .map(RealmRankUIModel::success)
                    .onErrorReturn(e -> RealmRankUIModel.failure(e.getMessage()))
                    .startWith(RealmRankUIModel.progress()))
                    .publish()
                    .autoConnect(2);

    @Inject
    RealmRankTransformer(BnadeRepo repo) {
        this.mRepo = repo;
    }

    ObservableTransformer<AuctionRealm.SortType, RealmRankUIModel> load() {
        return upstream -> {
            upstream.subscribe(subject);
            return publisher.observeOn(AndroidSchedulers.mainThread());
        };
    }

    ObservableTransformer<AuctionRealm.SortType, RealmRankUIModel> sort() {
        return upstream ->
                Observable.combineLatest(upstream, publisher.filter(RealmRankUIModel::isSuccess),
                        (sortType, model) -> {
                            Collections.sort(model.getList(), getComparator(sortType));
                            return model;
                        })
                        .observeOn(AndroidSchedulers.mainThread());
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
