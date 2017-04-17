package com.summer.bnade.search.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.mikephil.charting.data.CombinedData;
import com.summer.lib.model.entity.AuctionItem;
import com.summer.lib.model.entity.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin.bai on 2017/4/14.
 */

public class SearchResultVO implements Parcelable {
    private Item item;
    private long avgBuyout;
    private List<AuctionItem> auctionItems;
    private CombinedData combinedData;

    private List<String> names;

    public CombinedData getCombinedData() {
        return combinedData;
    }

    public void setCombinedData(CombinedData combinedData) {
        this.combinedData = combinedData;
    }

    public SearchResultVO(Item item, List<AuctionItem> auctionItems) {
        this.item = item;
        this.auctionItems = auctionItems;
    }

    public SearchResultVO(List<String> names) {
        this.names = names;
    }

    public List<String> getNames() {
        return names;
    }

    public long getAvgBuyout() {
        return avgBuyout;
    }

    public void setAvgBuyout(long avgBuyout) {
        this.avgBuyout = avgBuyout;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public List<AuctionItem> getAuctionItems() {
        return auctionItems;
    }

    public void setAuctionItems(List<AuctionItem> auctionItems) {
        this.auctionItems = auctionItems;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.item, flags);
        dest.writeList(this.auctionItems);
    }

    protected SearchResultVO(Parcel in) {
        this.item = in.readParcelable(Item.class.getClassLoader());
        this.auctionItems = new ArrayList<AuctionItem>();
        in.readList(this.auctionItems, AuctionItem.class.getClassLoader());
    }

    public static final Parcelable.Creator<SearchResultVO> CREATOR = new Parcelable.Creator<SearchResultVO>() {
        @Override
        public SearchResultVO createFromParcel(Parcel source) {
            return new SearchResultVO(source);
        }

        @Override
        public SearchResultVO[] newArray(int size) {
            return new SearchResultVO[size];
        }
    };
}
