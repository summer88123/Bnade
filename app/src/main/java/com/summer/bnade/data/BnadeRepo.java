package com.summer.bnade.data;

import android.util.SparseArray;

import com.summer.bnade.data.error.EmptyDataException;
import com.summer.lib.model.api.BnadeApi;
import com.summer.lib.model.entity.Auction;
import com.summer.lib.model.entity.AuctionItem;
import com.summer.lib.model.entity.AuctionRealm;
import com.summer.lib.model.entity.AuctionRealmItem;
import com.summer.lib.model.entity.Hot;
import com.summer.lib.model.entity.Item;
import com.summer.lib.model.entity.Realm;
import com.summer.lib.model.entity.WowTokens;
import com.summer.lib.model.utils.RealmHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * Created by kevin.bai on 2017/4/9.
 */
public class BnadeRepo {
    private final BnadeApi api;
    private final RealmHelper mRealmHelper;
    private final SparseArray<List<Hot>> hotCache;

    @Inject
    BnadeRepo(BnadeApi api, RealmHelper realmHelper) {
        this.api = api;
        this.mRealmHelper = realmHelper;
        hotCache = new SparseArray<>(3);
    }

    public Single<Item> getItem(String name) {
        return api.getItem(name).map(new Function<List<Item>, Item>() {
            @Override
            public Item apply(@NonNull List<Item> items) throws Exception {
                if (!items.isEmpty()) {
                    return items.get(0);
                }
                throw new EmptyDataException();
            }
        });
    }

    public Single<List<String>> getItemNames(String name) {
        return api.getItemNames(name);
    }

    public Single<List<AuctionItem>> getAuction(long itemId) {
        return api.getAuctionItem(itemId)
                .flatMapObservable(new Function<List<AuctionItem>, ObservableSource<AuctionItem>>() {
                    @Override
                    public ObservableSource<AuctionItem> apply(@NonNull List<AuctionItem> auctionItems) throws
                            Exception {
                        return Observable.fromIterable(auctionItems);
                    }
                })
                .flatMapSingle(new Function<AuctionItem, SingleSource<AuctionItem>>() {
                    @Override
                    public SingleSource<AuctionItem> apply(@NonNull AuctionItem auctionItem) throws Exception {
                        return Single.just(auctionItem)
                                .zipWith(mRealmHelper.getRealmById(auctionItem.getRealmId()),
                                        new BiFunction<AuctionItem, Realm, AuctionItem>() {
                                            @Override
                                            public AuctionItem apply(@NonNull AuctionItem auctionItem, @NonNull Realm
                                                    realm) throws Exception {
                                                auctionItem.setRealm(realm);
                                                return auctionItem;
                                            }
                                        });
                    }
                })
                .toSortedList();
    }

    public Observable<Realm> getRealmsByName(CharSequence s) {
        return mRealmHelper.getRealmsByName(s.toString());
    }

    public Single<List<WowTokens>> getWowTokens() {
        return api.getAhWowtokens();
    }

    public Single<List<Hot>> getHot(int type) {
        return Observable.concat(getHotCache(type), getHotRemote(type)).firstOrError();
    }

    private Observable<List<Hot>> getHotRemote(final int type) {
        return api.getHot().toObservable()
                .map(new Function<List<Hot>, SparseArray<List<Hot>>>() {
                    @Override
                    public SparseArray<List<Hot>> apply(@NonNull List<Hot> hots) throws Exception {
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
                    }
                })
                .map(new Function<SparseArray<List<Hot>>, List<Hot>>() {
                    @Override
                    public List<Hot> apply(@NonNull SparseArray<List<Hot>> listSparseArray) throws Exception {
                        return listSparseArray.get(type);
                    }
                });
    }

    private Observable<List<Hot>> getHotCache(int type) {
        return hotCache.get(type) == null ? Observable.<List<Hot>>empty() : Observable.just(hotCache.get(type));
    }

    public Single<List<AuctionRealm>> getAuctionRealm() {
        return api.getAuctionRealmsSummary()
                .flatMapObservable(new Function<List<AuctionRealm>, ObservableSource<AuctionRealm>>() {
                    @Override
                    public ObservableSource<AuctionRealm> apply(@NonNull List<AuctionRealm> auctionRealms) throws
                            Exception {
                        return Observable.fromIterable(auctionRealms);
                    }
                })
                .flatMapSingle(new Function<AuctionRealm, SingleSource<AuctionRealm>>() {
                    @Override
                    public SingleSource<AuctionRealm> apply(@NonNull AuctionRealm auctionRealm) throws Exception {
                        return Single.just(auctionRealm)
                                .zipWith(mRealmHelper.getRealmById(auctionRealm.getId()),
                                        new BiFunction<AuctionRealm, Realm, AuctionRealm>() {
                                            @Override
                                            public AuctionRealm apply(@NonNull AuctionRealm auctionRealm, @NonNull
                                                    Realm realm) throws Exception {
                                                auctionRealm.setRealm(realm);
                                                return auctionRealm;
                                            }
                                        });
                    }
                })
                .toList();
    }

    public Single<List<Auction>> getAuctionRealmOwner(long realmId, String name) {
        return api.getAuctionRealmOwner(realmId, name)
                .flatMapObservable(new Function<List<Auction>, ObservableSource<Auction>>() {
                    @Override
                    public ObservableSource<Auction> apply(@NonNull List<Auction> auctions) throws Exception {
                        return Observable.fromIterable(auctions);
                    }
                })
                .flatMapSingle(new Function<Auction, SingleSource<Auction>>() {
                    @Override
                    public SingleSource<Auction> apply(@NonNull Auction auction) throws Exception {
                        return Single.just(auction).zipWith(getItem(auction.getName()),
                                new BiFunction<Auction, Item, Auction>() {
                                    @Override
                                    public Auction apply(@NonNull Auction auction, @NonNull Item item) throws
                                            Exception {
                                        auction.setItem(item);
                                        return auction;
                                    }
                                });
                    }
                })
                .toList();
    }

    public Single<List<AuctionRealmItem>> getAuctionRealmItem(long realmId, long itemId) {
        return api.getAuctionRealmItem(realmId, itemId);
    }

    public Observable<Realm> getAllRealm(boolean hasAllItem) {
        return mRealmHelper.getAllRealm(hasAllItem);
    }

    public Single<Realm> getRealm(String name) {
        return mRealmHelper.getRealmsByName(name).firstOrError();
    }
}
