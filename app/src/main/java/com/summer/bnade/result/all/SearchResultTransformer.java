package com.summer.bnade.result.all;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.jakewharton.rxbinding2.support.v7.widget.SearchViewQueryTextEvent;
import com.summer.bnade.data.BnadeRepo;
import com.summer.bnade.utils.ChartHelper;
import com.summer.lib.model.entity.AuctionItem;
import com.summer.lib.model.entity.Gold;
import com.summer.lib.model.entity.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by kevin.bai on 2017/4/14.
 */

class SearchResultTransformer {
    private BnadeRepo mRepo;
    private BehaviorSubject<Item> subject = BehaviorSubject.create();
    private Observable<List<AuctionItem>> publisher = subject
            .flatMap(item -> mRepo.getAuction(item)
                    .toObservable())
            .observeOn(AndroidSchedulers.mainThread())
            .publish().autoConnect(2);


    @Inject
    SearchResultTransformer(BnadeRepo repo) {
        this.mRepo = repo;
    }

    public ObservableTransformer<SearchViewQueryTextEvent, List<AuctionItem>> filter() {
        return observable -> Observable.combineLatest(observable, publisher,
                (event, list) -> {
                    List<AuctionItem> result = new ArrayList<>();
                    CharSequence filter = event.queryText();
                    for (AuctionItem auctionItem : list) {
                        if (auctionItem.getRealm().getName().contains(filter)) {
                            result.add(auctionItem);
                        }
                    }
                    return result;
                });
    }

    public ObservableTransformer<Item, ResultUIModel> list() {
        return upstream -> {
            upstream.subscribe(subject);
            return publisher.map(auctionItems -> {
                List<Entry> lines = ChartHelper
                        .generateData(auctionItems, (integer, auctionItem) -> new Entry(integer, auctionItem
                                .getMinBuyOut().getMoney(), auctionItem));
                List<BarEntry> bars = ChartHelper
                        .generateData(auctionItems, (integer, auctionItem) -> new BarEntry(integer,
                                auctionItem.getTotal(), auctionItem));
                BigDecimal sum = new BigDecimal(0);
                for (AuctionItem auctionItem : auctionItems) {
                    sum = sum.add(new BigDecimal(auctionItem.getMinBuyOut().getMoney()));
                }
                Gold avg = new Gold(sum
                        .divide(new BigDecimal(auctionItems.size()), 0, BigDecimal.ROUND_CEILING)
                        .longValue());
                return ResultUIModel.success(auctionItems, lines, bars, avg);
            });
        };
    }
}
