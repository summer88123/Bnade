package com.summer.lib.model.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by kevin.bai on 2017/4/4.
 */

public class AuctionItem implements Parcelable, Comparable<AuctionItem> {
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
    private long realmId;
    private Realm realm;
    private Gold minBuyOut;
    private String name;
    private int total;
    private long lastUpdateTime;
    private LastTime lastTime;

    public AuctionItem() {
    }

    protected AuctionItem(Parcel in) {
        this.realmId = in.readLong();
        this.realm = in.readParcelable(Realm.class.getClassLoader());
        this.minBuyOut = in.readParcelable(Gold.class.getClassLoader());
        this.name = in.readString();
        this.total = in.readInt();
        this.lastUpdateTime = in.readLong();
        int tmpLastTime = in.readInt();
        this.lastTime = tmpLastTime == -1 ? null : LastTime.values()[tmpLastTime];
    }

    @Override
    public int compareTo(@NonNull AuctionItem o) {
        return this.minBuyOut.compareTo(o.minBuyOut);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.realmId);
        dest.writeParcelable(this.realm, flags);
        dest.writeParcelable(this.minBuyOut, flags);
        dest.writeString(this.name);
        dest.writeInt(this.total);
        dest.writeLong(this.lastUpdateTime);
        dest.writeInt(this.lastTime == null ? -1 : this.lastTime.ordinal());
    }

    public LastTime getLastTime() {
        return lastTime;
    }

    public void setLastTime(LastTime lastTime) {
        this.lastTime = lastTime;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Gold getMinBuyOut() {
        return minBuyOut;
    }

    public void setMinBuyOut(Gold minBuyOut) {
        this.minBuyOut = minBuyOut;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
