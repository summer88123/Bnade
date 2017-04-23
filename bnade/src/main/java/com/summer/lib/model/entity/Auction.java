package com.summer.lib.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kevin.bai on 2017/4/23.
 */

public class Auction implements Parcelable {
    private long itemId;
    private String name;
    private String realmName;
    private long bidPrece;
    private long buyOut;
    private int count;
    private String lastTime;
    private long lastModified;

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

    public long getBidPrece() {
        return bidPrece;
    }

    public void setBidPrece(long bidPrece) {
        this.bidPrece = bidPrece;
    }

    public long getBuyOut() {
        return buyOut;
    }

    public void setBuyOut(long buyOut) {
        this.buyOut = buyOut;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.itemId);
        dest.writeString(this.name);
        dest.writeString(this.realmName);
        dest.writeLong(this.bidPrece);
        dest.writeLong(this.buyOut);
        dest.writeInt(this.count);
        dest.writeString(this.lastTime);
        dest.writeLong(this.lastModified);
    }

    public Auction() {
    }

    protected Auction(Parcel in) {
        this.itemId = in.readLong();
        this.name = in.readString();
        this.realmName = in.readString();
        this.bidPrece = in.readLong();
        this.buyOut = in.readLong();
        this.count = in.readInt();
        this.lastTime = in.readString();
        this.lastModified = in.readLong();
    }

    public static final Parcelable.Creator<Auction> CREATOR = new Parcelable.Creator<Auction>() {
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
