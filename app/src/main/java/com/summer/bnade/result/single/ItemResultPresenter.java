package com.summer.bnade.result.single;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.summer.bnade.base.mvp.BasePresenter;
import com.summer.bnade.data.BnadeRepo;
import com.summer.bnade.result.single.entity.AuctionHistoryVO;
import com.summer.bnade.search.entity.SearchResultVO;
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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kevin.bai on 2017/4/28.
 */

class ItemResultPresenter extends BasePresenter<ItemResultContract.View> implements
        ItemResultContract.Presenter {

    @Inject
    ItemResultPresenter(ItemResultContract.View view, BnadeRepo repo) {
        super(view, repo);
    }

    @Override
    public void load(Item item, Realm realm) {
        mRepo.search(item, realm)
                .map(new Function<SearchResultVO, SearchResultVO>() {
                    @Override
                    public SearchResultVO apply(@NonNull SearchResultVO resultVO) throws Exception {
                        resultVO.setAuctionRealmItems(polySameRealmItem(resultVO.getAuctionRealmItems()));
                        resultVO.setAuctionHistoryVO(computeHistory(resultVO.getAuctionHistories()));
                        return resultVO;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SearchResultVO>() {
                    @Override
                    public void accept(@NonNull SearchResultVO searchResultVO) throws Exception {
                        mView.show(searchResultVO);
                    }
                }, mErrorHandler);
    }

    private AuctionHistoryVO computeHistory(List<AuctionHistory> auctionHistories) {
        AuctionHistoryVO result = new AuctionHistoryVO();

        Collections.sort(auctionHistories, new Comparator<AuctionHistory>() {
            @Override
            public int compare(AuctionHistory o1, AuctionHistory o2) {
                return (int) (o1.getMinBuyout().getMoney() - o2.getMinBuyout().getMoney());
            }
        });

        long oneDay = 3600 * 24 * 1000L;
        long week = 7 * oneDay;
        long current = System.currentTimeMillis();
        LinkedList<AuctionHistory> listOneDay = new LinkedList<>();
        LinkedList<AuctionHistory> listWeek = new LinkedList<>();
        LinkedList<AuctionHistory> listHistories = new LinkedList<>();

        long avgOneDay = 0;
        long avgWeek = 0;
        long avgHistory = 0;
        int avgCountOneDay = 0;
        int avgCountWeek = 0;
        int avgCountHistory = 0;

        for (AuctionHistory auctionHistory : auctionHistories) {
            long last = auctionHistory.getLastModifited();
            if (current - last < oneDay) {
                listOneDay.add(auctionHistory);
                avgOneDay += auctionHistory.getMinBuyout().getMoney();
                avgCountOneDay += auctionHistory.getCount();
            }
            if (current - last < week) {
                listWeek.add(auctionHistory);
                avgWeek += auctionHistory.getMinBuyout().getMoney();
                avgCountWeek += auctionHistory.getCount();
            }
            listHistories.add(auctionHistory);
            avgHistory += auctionHistory.getMinBuyout().getMoney();
            avgCountHistory += auctionHistory.getCount();
        }
        result.setOneDay(getHistoryItem(listOneDay, avgOneDay, avgCountOneDay));
        result.setLastWeek(getHistoryItem(listWeek, avgWeek, avgCountWeek));
        result.setHistory(getHistoryItem(listHistories, avgHistory, avgCountHistory));

        Comparator<AuctionHistory> timeComparator = new Comparator<AuctionHistory>() {
            @Override
            public int compare(AuctionHistory o1, AuctionHistory o2) {
                return (int) (o1.getLastModifited() - o2.getLastModifited());
            }
        };

        Collections.sort(listOneDay, timeComparator);
        Collections.sort(listHistories, timeComparator);

        result.setDataOneDay(getCombinedData(listOneDay));
        result.setDataHistory(getCombinedData(listHistories));

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
