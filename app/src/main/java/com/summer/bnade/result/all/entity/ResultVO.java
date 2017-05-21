package com.summer.bnade.result.all.entity;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.summer.lib.model.entity.AuctionItem;
import com.summer.lib.model.entity.Gold;

import java.util.List;

/**
 * Created by kevin.bai on 2017/5/21.
 */

public class ResultVO {
    private List<AuctionItem> auctionItems;

    private List<Entry> lines;
    private List<BarEntry> bars;

    private Gold avg;

    public ResultVO(List<AuctionItem> auctionItems, List<Entry> lines, List<BarEntry> bars, Gold avg) {
        this.auctionItems = auctionItems;
        this.lines = lines;
        this.bars = bars;
        this.avg = avg;
    }

    public List<AuctionItem> getAuctionItems() {
        return auctionItems;
    }

    public List<Entry> getLines() {
        return lines;
    }

    public List<BarEntry> getBars() {
        return bars;
    }

    public Gold getAvg() {
        return avg;
    }
}
