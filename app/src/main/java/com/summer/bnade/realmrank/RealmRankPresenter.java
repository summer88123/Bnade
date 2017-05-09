package com.summer.bnade.realmrank;

import com.summer.bnade.base.BasePresenter;
import com.summer.bnade.data.BnadeRepo;
import com.summer.lib.model.entity.AuctionRealm;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by kevin.bai on 2017/4/16.
 */

public class RealmRankPresenter extends BasePresenter<RealmRankContract.View> implements RealmRankContract.Presenter {

    @Inject
    RealmRankPresenter(RealmRankContract.View view, BnadeRepo repo) {
        super(view, repo);
    }

    @Override
    public void load(final AuctionRealm.SortType sortType) {
        mRepo.getAuctionRealm(false)
                .compose(sortData(sortType))
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
                        mView.setRefreshing(false);
                        mView.show(auctionRealms, sortType);
                    }
                }, mErrorHandler);
    }

    @Override
    public void sort(final AuctionRealm.SortType sortType) {
        mRepo.getAuctionRealm(true)
                .flatMapObservable(new Function<List<AuctionRealm>, ObservableSource<AuctionRealm>>() {
                    @Override
                    public ObservableSource<AuctionRealm> apply(@NonNull List<AuctionRealm> auctionRealms) throws
                            Exception {
                        return Observable.fromIterable(auctionRealms);
                    }
                })
                .toSortedList(getComparator(sortType))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<AuctionRealm>>() {
                    @Override
                    public void accept(@NonNull List<AuctionRealm> auctionRealms) throws Exception {
                        mView.show(auctionRealms, sortType);
                    }
                }, mErrorHandler);
    }

    private Comparator<? super AuctionRealm> getComparator(final AuctionRealm.SortType sortType) {
        return new Comparator<AuctionRealm>() {
            @Override
            public int compare(AuctionRealm l, AuctionRealm r) {
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
            }
        };
    }

    private SingleTransformer<List<AuctionRealm>, List<AuctionRealm>> sortData(final AuctionRealm.SortType sortType) {
        return new SingleTransformer<List<AuctionRealm>, List<AuctionRealm>>() {
            @Override
            public SingleSource<List<AuctionRealm>> apply(@NonNull Single<List<AuctionRealm>> upstream) {
                return upstream.map(new Function<List<AuctionRealm>, List<AuctionRealm>>() {
                    @Override
                    public List<AuctionRealm> apply(@NonNull List<AuctionRealm> auctionRealms) throws Exception {
                        Collections.sort(auctionRealms, getComparator(sortType));
                        return auctionRealms;
                    }
                });
            }
        };
    }
}
