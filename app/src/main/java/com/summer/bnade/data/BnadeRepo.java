package com.summer.bnade.data;

import android.support.v4.util.Pair;
import android.util.SparseArray;

import com.summer.bnade.data.error.EmptyDataException;
import com.summer.bnade.search.entity.SearchResultVO;
import com.summer.lib.model.api.BnadeApi;
import com.summer.lib.model.entity.Auction;
import com.summer.lib.model.entity.AuctionHistory;
import com.summer.lib.model.entity.AuctionItem;
import com.summer.lib.model.entity.AuctionRealm;
import com.summer.lib.model.entity.AuctionRealmItem;
import com.summer.lib.model.entity.Hot;
import com.summer.lib.model.entity.Item;
import com.summer.lib.model.entity.Realm;
import com.summer.lib.model.entity.WowTokens;
import com.summer.lib.model.repo.RealmRepo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kevin.bai on 2017/4/9.
 */
@Singleton
public class BnadeRepo {
    private final BnadeApi api;
    // TODO 缓存需要设置失效
    private final SparseArray<List<Hot>> hotCache;
    private final RealmRepo mRealmRepo;
    private final SearchResultVO mSearchResultVO;

    @Inject
    BnadeRepo(BnadeApi api, RealmRepo realmRepo) {
        this.api = api;
        this.mRealmRepo = realmRepo;
        hotCache = new SparseArray<>(3);
        mSearchResultVO = new SearchResultVO();
    }

    public Observable<Realm> getAllRealm(boolean hasAllItem) {
        return mRealmRepo.getAllRealm(hasAllItem);
    }

    public Single<List<AuctionItem>> getAuction(Item item) {
        return api.getAuctionItem(item.getId())
                .flatMapObservable(Observable::fromIterable)
                .flatMapSingle(auctionItem -> mRealmRepo.getRealmById(auctionItem.getRealmId())
                        .map(realm -> {
                            auctionItem.setRealm(realm);
                            return auctionItem;
                        }))
                .toSortedList();
    }

    public Observable<Pair<List<AuctionHistory>, List<AuctionHistory>>> getAuctionPastAndHistory(Item item, Realm
            realm) {
        return Observable.zip(
                api.getAuctionPastRealmItem(realm.getId(), item.getId()),
                api.getAuctionHistoryRealmItem(realm.getId(), item.getId()),
                Pair::new);
    }

    public Observable<List<AuctionRealm>> getAuctionRealm() {
        return api.getAuctionRealmsSummary()
                .flatMapObservable(Observable::fromIterable)
                .flatMapSingle(auctionRealm -> Single.just(auctionRealm)
                        .zipWith(mRealmRepo.getRealmById(auctionRealm.getId()), (auctionRealm1, realm) -> {
                            auctionRealm1.setRealm(realm);
                            return auctionRealm1;
                        }))
                .toList()
                .toObservable();
    }

    public Observable<List<AuctionRealmItem>> getAuctionRealmItem(Item item, Realm realm) {
        return api.getAuctionRealmItem(realm.getId(), item.getId());
    }

    public Observable<List<Auction>> getAuctionRealmOwner(long realmId, CharSequence name) {
        return api.getAuctionRealmOwner(realmId, name)
                .flatMapObservable(Observable::fromIterable)
                .flatMapSingle(auction -> getItem(auction.getName())
                        .map(item -> {
                            auction.setItem(item);
                            return auction;
                        }))
                .toList().toObservable();
    }

    public Single<List<Hot>> getHot(int type) {
        return Observable.concat(getHotCache(type), getHotRemote(type)).firstOrError();
    }

    public Single<Item> getItem(String name) {
        return api.getItem(name).map(items -> {
            if (!items.isEmpty()) {
                return items.get(0);
            }
            throw new EmptyDataException();
        });
    }

    public Single<List<String>> getItemNames(String name) {
        return api.getItemNames(name);
    }

    public Observable<Realm> getRealmsByName(CharSequence s) {
        return mRealmRepo.getRealmsByName(s.toString());
    }

    public Single<List<WowTokens>> getWowTokens() {
        return api.getAhWowtokens();
    }

    public Single<SearchResultVO> search(Item item, Realm realm) {
        return Observable.concat(searchCache(item), searchRemote(item, realm).toObservable()).firstOrError();
    }

    private Observable<List<Hot>> getHotCache(int type) {
        return (hotCache.get(type) == null ? Observable.<List<Hot>>empty() : Observable.just(hotCache.get(type)))
                .subscribeOn(Schedulers.computation());
    }

    private Observable<List<Hot>> getHotRemote(final int type) {
        return api.getHot().toObservable()
                .map(hots -> {
                    SparseArray<List<Hot>> map = hotCache;
                    map.clear();
                    for (Hot hot : hots) {
                        List<Hot> typeList = map.get(hot.getType());
                        if (typeList == null) {
                            typeList = new ArrayList<>();
                            map.put(hot.getType(), typeList);
                        }
                        typeList.add(hot);
                    }
                    return map;
                })
                .map(listSparseArray -> listSparseArray.get(type));
    }

    private Observable<SearchResultVO> searchCache(@NonNull Item item) {
        return (item.equals(mSearchResultVO.getItem())
                ? Observable.just(mSearchResultVO) : Observable.<SearchResultVO>empty()).subscribeOn(Schedulers.io());
    }

    private Single<SearchResultVO> searchRemote(@NonNull Item item, Realm realm) {
        if (realm == null) {
            return Single.zip(getAuction(item), Single
                    .just(item), (auctionItems, item1) -> {
                mSearchResultVO.reset();
                mSearchResultVO.setItem(item1);
                mSearchResultVO.setAuctionItems(auctionItems);
                return mSearchResultVO;
            });
        }
        return Single.zip(api.getAuctionRealmItem(realm.getId(), item.getId()).firstOrError(), Single.just(item),
                api.getAuctionPastRealmItem(realm.getId(), item.getId()).firstOrError(),
                api.getAuctionHistoryRealmItem(realm.getId(), item.getId()).firstOrError(),
                (auctionRealmItems, item12, auctionHistories, auctionHistories2) -> {
                    mSearchResultVO.reset();
                    mSearchResultVO.setItem(item12);
                    mSearchResultVO.setAuctionRealmItems(auctionRealmItems);
                    mSearchResultVO.setAuctionHistories(auctionHistories2);
                    mSearchResultVO.setAuctionPast(auctionHistories);
                    return mSearchResultVO;
                });
    }

}
