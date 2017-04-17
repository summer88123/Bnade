package com.summer.lib.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kevin.bai on 2017/4/4.
 */

public class AuctionItem implements Parcelable {
    private long realmId;
    private Realm realm;
    private long minBuyOut;
    private String name;
    private int total;
    private long lastUpdateTime;
    private String lastTime;

    public Realm getRealm() {
        return realm;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    public long getRealmId() {
        return realmId;
    }

    public void setRealmId(long realmId) {
        this.realmId = realmId;
    }

    public long getMinBuyOut() {
        return minBuyOut;
    }

    public void setMinBuyOut(long minBuyOut) {
        this.minBuyOut = minBuyOut;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public AuctionItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.realmId);
        dest.writeParcelable(this.realm, flags);
        dest.writeLong(this.minBuyOut);
        dest.writeString(this.name);
        dest.writeInt(this.total);
        dest.writeLong(this.lastUpdateTime);
        dest.writeString(this.lastTime);
    }

    protected AuctionItem(Parcel in) {
        this.realmId = in.readLong();
        this.realm = in.readParcelable(Realm.class.getClassLoader());
        this.minBuyOut = in.readLong();
        this.name = in.readString();
        this.total = in.readInt();
        this.lastUpdateTime = in.readLong();
        this.lastTime = in.readString();
    }

    public static final Creator<AuctionItem> CREATOR = new Creator<AuctionItem>() {
        @Override
        public AuctionItem createFromParcel(Parcel source) {
            return new AuctionItem(source);
        }

        @Override
        public AuctionItem[] newArray(int size) {
            return new AuctionItem[size];
        }
    };
}
