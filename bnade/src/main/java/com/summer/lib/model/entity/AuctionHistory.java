package com.summer.lib.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kevin.bai on 2017/5/4.
 */

public class AuctionHistory implements Parcelable {
    public static final Parcelable.Creator<AuctionHistory> CREATOR = new Parcelable.Creator<AuctionHistory>() {
        @Override
        public AuctionHistory createFromParcel(Parcel source) {
            return new AuctionHistory(source);
        }

        @Override
        public AuctionHistory[] newArray(int size) {
            return new AuctionHistory[size];
        }
    };
    private Gold minBuyout;
    private int count;
    private long lastModifited;

    public AuctionHistory() {
    }

    protected AuctionHistory(Parcel in) {
        this.minBuyout = in.readParcelable(Gold.class.getClassLoader());
        this.count = in.readInt();
        this.lastModifited = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.minBuyout, flags);
        dest.writeInt(this.count);
        dest.writeLong(this.lastModifited);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getLastModifited() {
        return lastModifited;
    }

    public void setLastModifited(long lastModifited) {
        this.lastModifited = lastModifited;
    }

    public Gold getMinBuyout() {
        return minBuyout;
    }

    public void setMinBuyout(Gold minBuyout) {
        this.minBuyout = minBuyout;
    }
}
