package com.summer.bnade.result.single;

import android.support.v4.util.Pair;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
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

import io.reactivex.SingleTransformer;

/**
 * Created by kevin.bai on 2017/4/28.
 */

class ItemResultTransformer {

    private Comparator<AuctionHistory> goldComparator = (o1, o2) -> (int) (o1.getMinBuyout().getMoney() - o2
            .getMinBuyout().getMoney());

    private Comparator<AuctionHistory> timeComparator = (o1, o2) -> (int) (o1.getLastModifited() - o2
            .getLastModifited());

    private BnadeRepo mRepo;

    @Inject
    ItemResultTransformer(BnadeRepo repo) {
        this.mRepo = repo;
    }

    SingleTransformer<Pair<Item, Realm>, AuctionHistoryVO> history() {
        return upstream -> upstream
                .flatMap(itemRealmPair -> mRepo.getAuctionPastAndHistory(itemRealmPair.first, itemRealmPair.second))
                .map(listListPair -> {
                    AuctionHistoryVO result = new AuctionHistoryVO();
                    computeHistory(listListPair.second, result);
                    computePast(listListPair.first, result);
                    return result;
                });
    }

    SingleTransformer<Pair<Item, Realm>, List<AuctionRealmItem>> price() {
        return upstream -> upstream
                .flatMap(itemRealmPair -> mRepo.getAuctionRealmItem(itemRealmPair.first, itemRealmPair.second))
                .map(this::polySameRealmItem);
    }

    private void computeHistory(List<AuctionHistory> auctionHistories, AuctionHistoryVO result) {

        Collections.sort(auctionHistories, goldComparator);
        long oneDay = 3600 * 24 * 1000L;
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
        long temp = 0;
        List<AuctionHistory> list = new ArrayList<>(listHistories.size());
        for (AuctionHistory listHistory : listHistories) {
            if (temp == 0 || listHistory.getLastModifited() - temp > oneDay) {
                list.add(listHistory);
                temp = listHistory.getLastModifited();
            }
        }
        result.setDataHistory(getCombinedData(list));

    }

    private void computePast(List<AuctionHistory> auctionPast, AuctionHistoryVO result) {

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

    }

    @android.support.annotation.NonNull
    private CombinedData getCombinedData(List<AuctionHistory> auctionHistories) {
        CombinedData data = new CombinedData();
        data.setData(ChartHelper.generateLineData(auctionHistories, (index, auctionItem) -> new Entry(auctionItem
                .getLastModifited(), auctionItem.getMinBuyout()
                .getMoney(), auctionItem)));
        data.setData(ChartHelper.generateBarData(auctionHistories, (integer, auctionItem) -> new BarEntry(auctionItem
                .getLastModifited(), auctionItem
                .getCount(), auctionItem)));
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

        Collections.sort(result, (o1, o2) -> {
            long result1 = o1.getUnitBuyout().getMoney() - o2.getUnitBuyout().getMoney();
            if (result1 == 0) {
                result1 = o1.getUnitBidPrice().getMoney() - o2.getUnitBidPrice().getMoney();
            }
            if (result1 > 0) return 1;
            else return result1 == 0 ? 0 : -1;
        });
        return result;
    }
}
