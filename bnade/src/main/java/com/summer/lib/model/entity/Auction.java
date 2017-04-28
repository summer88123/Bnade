package com.summer.lib.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kevin.bai on 2017/4/23.
 */

public class Auction implements Parcelable {
    private long itemId;
    private String name;
    private Item item;
    private String realmName;
    private Gold bidPrece;
    private Gold buyOut;
    private int count;
    private LastTime lastTime;
    private long lastModified;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealmName() {
        return realmName;
    }

    public void setRealmName(String realmName) {
        this.realmName = realmName;
    }

    public Gold getBidPrece() {
        return bidPrece;
    }

    public void setBidPrece(Gold bidPrece) {
        this.bidPrece = bidPrece;
    }

    public Gold getBuyOut() {
        return buyOut;
    }

    public void setBuyOut(Gold buyOut) {
        this.buyOut = buyOut;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public LastTime getLastTime() {
        return lastTime;
    }

    public void setLastTime(LastTime lastTime) {
        this.lastTime = lastTime;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public Auction() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.itemId);
        dest.writeString(this.name);
        dest.writeParcelable(this.item, flags);
        dest.writeString(this.realmName);
        dest.writeParcelable(this.bidPrece, flags);
        dest.writeParcelable(this.buyOut, flags);
        dest.writeInt(this.count);
        dest.writeInt(this.lastTime == null ? -1 : this.lastTime.ordinal());
        dest.writeLong(this.lastModified);
    }

    protected Auction(Parcel in) {
        this.itemId = in.readLong();
        this.name = in.readString();
        this.item = in.readParcelable(Item.class.getClassLoader());
        this.realmName = in.readString();
        this.bidPrece = in.readParcelable(Gold.class.getClassLoader());
        this.buyOut = in.readParcelable(Gold.class.getClassLoader());
        this.count = in.readInt();
        int tmpLastTime = in.readInt();
        this.lastTime = tmpLastTime == -1 ? null : LastTime.values()[tmpLastTime];
        this.lastModified = in.readLong();
    }

    public static final Creator<Auction> CREATOR = new Creator<Auction>() {
        @Override
        public Auction createFromParcel(Parcel source) {
            return new Auction(source);
        }

        @Override
        public Auction[] newArray(int size) {
            return new Auction[size];
        }
    };
}
