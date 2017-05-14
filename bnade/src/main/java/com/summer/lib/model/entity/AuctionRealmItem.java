package com.summer.lib.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kevin.bai on 2017/4/28.
 */

public class AuctionRealmItem implements Parcelable {
    public static final Parcelable.Creator<AuctionRealmItem> CREATOR = new Parcelable.Creator<AuctionRealmItem>() {
        @Override
        public AuctionRealmItem createFromParcel(Parcel source) {
            return new AuctionRealmItem(source);
        }

        @Override
        public AuctionRealmItem[] newArray(int size) {
            return new AuctionRealmItem[size];
        }
    };
    private String playerName;
    private String realmName;
    private Gold bidPrice;
    private Gold buyout;
    private int count;
    private LastTime lastTime;


    private Gold unitBidPrice;
    private Gold unitBuyout;

    public AuctionRealmItem() {
    }

    protected AuctionRealmItem(Parcel in) {
        this.playerName = in.readString();
        this.realmName = in.readString();
        this.bidPrice = in.readParcelable(Gold.class.getClassLoader());
        this.buyout = in.readParcelable(Gold.class.getClassLoader());
        this.count = in.readInt();
        int tmpLastTime = in.readInt();
        this.lastTime = tmpLastTime == -1 ? null : LastTime.values()[tmpLastTime];
    }

    @Override
    public int hashCode() {
        int result = getPlayerName().hashCode();
        result = 31 * result + getRealmName().hashCode();
        result = 31 * result + getLastTime().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuctionRealmItem)) return false;

        AuctionRealmItem that = (AuctionRealmItem) o;

        if (!getPlayerName().equals(that.getPlayerName())) return false;
        if (!getRealmName().equals(that.getRealmName())) return false;
        if (!getUnitBidPrice().equals(that.getUnitBidPrice())) return false;
        if (!getUnitBuyout().equals(that.getUnitBuyout())) return false;
        return getLastTime() == that.getLastTime();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.playerName);
        dest.writeString(this.realmName);
        dest.writeParcelable(this.bidPrice, flags);
        dest.writeParcelable(this.buyout, flags);
        dest.writeInt(this.count);
        dest.writeInt(this.lastTime == null ? -1 : this.lastTime.ordinal());
    }

    public void add(AuctionRealmItem realmItem) {
        count = realmItem.count + count;
        bidPrice = new Gold(bidPrice.getMoney() + realmItem.getBidPrice().getMoney());
        buyout = new Gold(buyout.getMoney() + realmItem.getBuyout().getMoney());
    }

    public Gold getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(Gold bidPrice) {
        this.bidPrice = bidPrice;
    }

    public Gold getBuyout() {
        return buyout;
    }

    public void setBuyout(Gold buyout) {
        this.buyout = buyout;
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

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getRealmName() {
        return realmName;
    }

    public void setRealmName(String realmName) {
        this.realmName = realmName;
    }

    public Gold getUnitBidPrice() {
        if (unitBidPrice == null) {
            unitBidPrice = new Gold(bidPrice.getMoney() / count);
        }
        return unitBidPrice;
    }

    public Gold getUnitBuyout() {
        if (unitBuyout == null) {
            unitBuyout = new Gold(buyout.getMoney() / count);
        }
        return unitBuyout;
    }
}
