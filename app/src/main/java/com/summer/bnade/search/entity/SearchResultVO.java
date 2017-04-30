package com.summer.bnade.search.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.mikephil.charting.data.CombinedData;
import com.summer.lib.model.entity.AuctionItem;
import com.summer.lib.model.entity.AuctionRealmItem;
import com.summer.lib.model.entity.Item;

import java.util.List;

/**
 * Created by kevin.bai on 2017/4/14.
 */

public class SearchResultVO implements Parcelable {
    private Item item;
    private List<AuctionRealmItem> auctionRealmItems;
    private long avgBuyout;
    private List<AuctionItem> auctionItems;
    private CombinedData combinedData;
    private List<String> names;

    public SearchResultVO(Item item) {
        this.item = item;
    }

    public SearchResultVO(Item item, List<AuctionItem> auctionItems) {
        this.item = item;
        this.auctionItems = auctionItems;
    }

    public SearchResultVO(List<String> names) {
        this.names = names;
    }

    public List<AuctionItem> getAuctionItems() {
        return auctionItems;
    }

    public void setAuctionItems(List<AuctionItem> auctionItems) {
        this.auctionItems = auctionItems;
    }

    public List<AuctionRealmItem> getAuctionRealmItems() {
        return auctionRealmItems;
    }

    public void setAuctionRealmItems(List<AuctionRealmItem> auctionRealmItems) {
        this.auctionRealmItems = auctionRealmItems;
    }

    public long getAvgBuyout() {
        return avgBuyout;
    }

    public void setAvgBuyout(long avgBuyout) {
        this.avgBuyout = avgBuyout;
    }

    public CombinedData getCombinedData() {
        return combinedData;
    }

    public void setCombinedData(CombinedData combinedData) {
        this.combinedData = combinedData;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public List<String> getNames() {
        return names;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.item, flags);
        dest.writeTypedList(this.auctionRealmItems);
        dest.writeTypedList(this.auctionItems);
    }

    protected SearchResultVO(Parcel in) {
        this.item = in.readParcelable(Item.class.getClassLoader());
        this.auctionRealmItems = in.createTypedArrayList(AuctionRealmItem.CREATOR);
        this.auctionItems = in.createTypedArrayList(AuctionItem.CREATOR);
    }

    public static final Creator<SearchResultVO> CREATOR = new Creator<SearchResultVO>() {
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
