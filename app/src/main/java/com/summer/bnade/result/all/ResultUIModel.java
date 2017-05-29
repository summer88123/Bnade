package com.summer.bnade.result.all;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.summer.bnade.base.BaseUIModel;
import com.summer.lib.model.entity.AuctionItem;
import com.summer.lib.model.entity.Gold;

import java.util.List;

class ResultUIModel extends BaseUIModel{
    private List<AuctionItem> auctionItems;

    private List<Entry> lines;
    private List<BarEntry> bars;

    private Gold avg;

    private ResultUIModel(List<AuctionItem> auctionItems, List<Entry> lines, List<BarEntry> bars, Gold avg) {
        this(false, true, null);
        this.auctionItems = auctionItems;
        this.lines = lines;
        this.bars = bars;
        this.avg = avg;
    }

    private ResultUIModel(boolean inProgress, boolean success, String errorMsg) {
        super(inProgress, success, errorMsg);
    }

    static ResultUIModel success(List<AuctionItem> auctionItems, List<Entry> lines, List<BarEntry> bars, Gold avg) {
        return new ResultUIModel(auctionItems, lines, bars, avg);
    }

    List<AuctionItem> getAuctionItems() {
        return auctionItems;
    }

    List<Entry> getLines() {
        return lines;
    }

    List<BarEntry> getBars() {
        return bars;
    }

    Gold getAvg() {
        return avg;
    }
}
