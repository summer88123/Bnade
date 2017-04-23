package com.summer.bnade.data;

import com.summer.bnade.data.error.EmptyDataException;
import com.summer.lib.model.api.BnadeApi;
import com.summer.lib.model.entity.AuctionItem;
import com.summer.lib.model.entity.AuctionRealm;
import com.summer.lib.model.entity.Hot;
import com.summer.lib.model.entity.Item;
import com.summer.lib.model.entity.Realm;
import com.summer.lib.model.entity.WowTokens;
import com.summer.lib.model.utils.RealmHelper;

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

    @Inject
    BnadeRepo(BnadeApi api, RealmHelper realmHelper) {
        this.api = api;
        this.mRealmHelper = realmHelper;
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

    public Single<List<Realm>> getRealmsByName(CharSequence s) {
        return mRealmHelper.getRealmsByName(s.toString()).toList();
    }

    public Single<List<WowTokens>> getWowTokens() {
        return api.getAhWowtokens();
    }

    public Single<List<Hot>> getHot() {
        return api.getHot();
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

    public Single<List<Realm>> getAllRealm(boolean hasAllItem) {
        return mRealmHelper.getAllRealm(hasAllItem).toList();
    }
}
