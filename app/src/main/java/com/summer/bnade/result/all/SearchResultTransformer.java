package com.summer.bnade.result.all;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.jakewharton.rxbinding2.support.v7.widget.SearchViewQueryTextEvent;
import com.summer.bnade.data.BnadeRepo;
import com.summer.bnade.result.all.entity.ResultVO;
import com.summer.bnade.utils.ChartHelper;
import com.summer.lib.model.entity.AuctionItem;
import com.summer.lib.model.entity.Gold;
import com.summer.lib.model.entity.Item;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;

/**
 * Created by kevin.bai on 2017/4/14.
 */

class SearchResultTransformer {
    private final BnadeRepo mRepo;

    @Inject
    SearchResultTransformer(BnadeRepo repo) {
        this.mRepo = repo;
    }

    public ObservableTransformer<SearchViewQueryTextEvent, List<AuctionItem>> filter(final Item item) {
        return observable -> observable.flatMap(
                searchViewQueryTextEvent -> mRepo.getAuction(item)
                        .flatMapObservable(Observable::fromIterable)
                        .filter(auctionItem -> auctionItem.getRealm().getConnected()
                                .contains(searchViewQueryTextEvent.queryText()))
                        .toList()
                        .toObservable()
        );
    }

    public SingleTransformer<Item, ResultVO> list() {
        return single -> single.flatMap(mRepo::getAuction)
                .map(auctionItems -> {
                    List<Entry> lines = ChartHelper
                            .generateData(auctionItems, (integer, auctionItem) -> new Entry(integer, auctionItem
                                    .getMinBuyOut()
                                    .getMoney(), auctionItem));
                    List<BarEntry> bars = ChartHelper
                            .generateData(auctionItems, (integer, auctionItem) -> new BarEntry(integer, auctionItem
                                    .getTotal(), auctionItem));
                    BigDecimal sum = new BigDecimal(0);
                    for (AuctionItem auctionItem : auctionItems) {
                        sum = sum.add(new BigDecimal(auctionItem.getMinBuyOut().getMoney()));
                    }
                    Gold avg = new Gold(sum
                            .divide(new BigDecimal(auctionItems.size()), 0, BigDecimal.ROUND_CEILING)
                            .longValue());
                    return new ResultVO(auctionItems, lines, bars, avg);
                });
    }
}
