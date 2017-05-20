package com.summer.bnade.result.single;

import android.support.v4.util.Pair;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.summer.bnade.base.mvp.BasePresenter;
import com.summer.bnade.data.BnadeRepo;
import com.summer.bnade.result.single.entity.AuctionHistoryVO;
import com.summer.bnade.utils.ChartHelper;
import com.summer.lib.model.entity.AuctionHistory;
import com.summer.lib.model.entity.AuctionRealmItem;
import com.summer.lib.model.entity.Gold;
import com.summer.lib.model.entity.Item;
import com.summer.lib.model.entity.Realm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * Created by kevin.bai on 2017/4/28.
 */

class ItemResultPresenter extends BasePresenter<ItemResultContract.View> implements
        ItemResultContract.Presenter {

    private Comparator<AuctionHistory> goldComparator = new Comparator<AuctionHistory>() {
        @Override
        public int compare(AuctionHistory o1, AuctionHistory o2) {
            return (int) (o1.getMinBuyout().getMoney() - o2.getMinBuyout().getMoney());
        }
    };

    private Comparator<AuctionHistory> timeComparator = new Comparator<AuctionHistory>() {
        @Override
        public int compare(AuctionHistory o1, AuctionHistory o2) {
            return (int) (o1.getLastModifited() - o2.getLastModifited());
        }
    };

    @Inject
    ItemResultPresenter(ItemResultContract.View view, BnadeRepo repo) {
        super(view, repo);
    }

    @Override
    public SingleTransformer<Pair<Item, Realm>, AuctionHistoryVO> history() {
        return new SingleTransformer<Pair<Item, Realm>, AuctionHistoryVO>() {
            @Override
            public SingleSource<AuctionHistoryVO> apply(@NonNull Single<Pair<Item, Realm>> upstream) {
                return upstream
                        .flatMap(new Function<Pair<Item, Realm>, SingleSource<Pair<List<AuctionHistory>,
                                List<AuctionHistory>>>>() {
                            @Override
                            public SingleSource<Pair<List<AuctionHistory>, List<AuctionHistory>>> apply(@NonNull
                                                                                                                Pair<Item, Realm> itemRealmPair) throws Exception {
                                return mRepo.getAuctionPastAndHistory(itemRealmPair.first, itemRealmPair.second);
                            }
                        })
                        .map(new Function<Pair<List<AuctionHistory>, List<AuctionHistory>>, AuctionHistoryVO>() {
                            @Override
                            public AuctionHistoryVO apply(@NonNull Pair<List<AuctionHistory>, List<AuctionHistory>>
                                                                  listListPair) throws Exception {
                                AuctionHistoryVO result = new AuctionHistoryVO();
                                computeHistory(listListPair.second, result);
                                computePast(listListPair.first, result);
                                return result;
                            }
                        });
            }
        };
    }

    @Override
    public SingleTransformer<Pair<Item, Realm>, List<AuctionRealmItem>> price() {
        return new SingleTransformer<Pair<Item, Realm>, List<AuctionRealmItem>>() {
            @Override
            public SingleSource<List<AuctionRealmItem>> apply(@NonNull Single<Pair<Item, Realm>>
                                                                      upstream) {
                return upstream
                        .flatMap(new Function<Pair<Item, Realm>, SingleSource<? extends List<AuctionRealmItem>>>() {


                            @Override
                            public SingleSource<? extends List<AuctionRealmItem>> apply(@NonNull Pair<Item, Realm>
                                                                                                itemRealmPair) throws
                                    Exception {
                                return mRepo.getAuctionRealmItem(itemRealmPair.first, itemRealmPair.second);
                            }
                        })
                        .map(new Function<List<AuctionRealmItem>, List<AuctionRealmItem>>() {
                            @Override
                            public List<AuctionRealmItem> apply(@NonNull List<AuctionRealmItem> auctionRealmItems)
                                    throws Exception {
                                return polySameRealmItem(auctionRealmItems);
                            }
                        });
            }
        };
    }

    private AuctionHistoryVO computeHistory(List<AuctionHistory> auctionHistories, AuctionHistoryVO result) {

        Collections.sort(auctionHistories, goldComparator);

        long week = 7 * 3600 * 24 * 1000L;
        long current = System.currentTimeMillis();
        LinkedList<AuctionHistory> listWeek = new LinkedList<>();
        LinkedList<AuctionHistory> listHistories = new LinkedList<>();

        long avgWeek = 0;
        long avgHistory = 0;
        int avgCountWeek = 0;
        int avgCountHistory = 0;

        for (AuctionHistory auctionHistory : auctionHistories) {
            long last = auctionHistory.getLastModifited();
            if (current - last < week) {
                listWeek.add(auctionHistory);
                avgWeek += auctionHistory.getMinBuyout().getMoney();
                avgCountWeek += auctionHistory.getCount();
            }
            listHistories.add(auctionHistory);
            avgHistory += auctionHistory.getMinBuyout().getMoney();
            avgCountHistory += auctionHistory.getCount();
        }
        result.setLastWeek(getHistoryItem(listWeek, avgWeek, avgCountWeek));
        result.setHistory(getHistoryItem(listHistories, avgHistory, avgCountHistory));

        Collections.sort(listHistories, timeComparator);

        result.setDataHistory(getCombinedData(listHistories));

        return result;
    }

    private AuctionHistoryVO computePast(List<AuctionHistory> auctionPast, AuctionHistoryVO result) {

        Collections.sort(auctionPast, goldComparator);

        long oneDay = 3600 * 24 * 1000L;
        long current = System.currentTimeMillis();
        LinkedList<AuctionHistory> listOneDay = new LinkedList<>();

        long avgOneDay = 0;
        int avgCountOneDay = 0;

        for (AuctionHistory auctionHistory : auctionPast) {
            long last = auctionHistory.getLastModifited();
            if (current - last < oneDay) {
                listOneDay.add(auctionHistory);
                avgOneDay += auctionHistory.getMinBuyout().getMoney();
                avgCountOneDay += auctionHistory.getCount();
            }
        }
        result.setOneDay(getHistoryItem(listOneDay, avgOneDay, avgCountOneDay));

        Collections.sort(listOneDay, timeComparator);

        result.setDataOneDay(getCombinedData(listOneDay));

        return result;
    }

    @android.support.annotation.NonNull
    private CombinedData getCombinedData(List<AuctionHistory> auctionHistories) {
        CombinedData data = new CombinedData();
        data.setData(ChartHelper.generateLineData(auctionHistories, new BiFunction<Integer, AuctionHistory, Entry>() {
            @Override
            public Entry apply(Integer index, @NonNull AuctionHistory auctionItem) throws
                    Exception {
                return new Entry(auctionItem.getLastModifited(), auctionItem.getMinBuyout()
                        .getMoney(), auctionItem);
            }
        }));
        data.setData(ChartHelper.generateBarData(auctionHistories, new BiFunction<Integer, AuctionHistory, BarEntry>() {
            @Override
            public BarEntry apply(@NonNull Integer integer, @NonNull AuctionHistory auctionItem)
                    throws Exception {
                return new BarEntry(auctionItem.getLastModifited(), auctionItem
                        .getCount(), auctionItem);
            }
        }));
        return data;
    }

    @android.support.annotation.NonNull
    private AuctionHistoryVO.HistoryItem getHistoryItem(LinkedList<AuctionHistory> list, long sumGold, int sumCount) {
        return new AuctionHistoryVO.HistoryItem(new Gold(sumGold / list.size()), list.getLast()
                .getMinBuyout(), list.getFirst().getMinBuyout(), sumCount / list.size());
    }

    @android.support.annotation.NonNull
    private List<AuctionRealmItem> polySameRealmItem(List<AuctionRealmItem> realmItems) {
        List<AuctionRealmItem> result = new ArrayList<>();
        for (AuctionRealmItem realmItem : realmItems) {
            int index = result.indexOf(realmItem);
            if (index != -1) {
                result.get(index).add(realmItem);
            } else {
                result.add(realmItem);
            }
        }

        Collections.sort(result, new Comparator<AuctionRealmItem>() {
            @Override
            public int compare(AuctionRealmItem o1, AuctionRealmItem o2) {
                long result = o1.getUnitBuyout().getMoney() - o2.getUnitBuyout().getMoney();
                if (result == 0) {
                    result = o1.getUnitBidPrice().getMoney() - o2.getUnitBidPrice().getMoney();
                }
                if (result > 0) return 1;
                else return result == 0 ? 0 : -1;
            }
        });
        return result;
    }
}
