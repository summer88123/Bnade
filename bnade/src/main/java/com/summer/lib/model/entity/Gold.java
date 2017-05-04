package com.summer.lib.model.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by kevin.bai on 2017/4/23.
 */

public class Gold implements Parcelable, Comparable<Gold> {
    private static final NumberFormat format = new DecimalFormat("00");
    private static final NumberFormat goldFormat = new DecimalFormat("0");
    private final long money;

    private long gold = -1;
    private int silver = -1;
    private int copper = -1;

    public Gold(long money) {
        this.money = money;
    }

    @Override
    public int compareTo(@NonNull Gold o) {
        return (int) (money - o.money);
    }

    public String getGold() {
        if (gold < 0) {
            gold = money / 10000;
        }
        return goldFormat.format(gold);
    }

    public String getSilver() {
        if (silver < 0) {
            silver = (int) (money % 10000 / 100);
        }
        return format.format(silver);
    }

    public String getCopper() {
        if (copper < 0) {
            copper = (int) (money % 100);
        }
        return format.format(copper);
    }

    public long getMoney() {
        return money;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.money);
        dest.writeLong(this.gold);
        dest.writeInt(this.silver);
        dest.writeInt(this.copper);
    }

    protected Gold(Parcel in) {
        this.money = in.readLong();
        this.gold = in.readLong();
        this.silver = in.readInt();
        this.copper = in.readInt();
    }

    public static final Parcelable.Creator<Gold> CREATOR = new Parcelable.Creator<Gold>() {
        @Override
        public Gold createFromParcel(Parcel source) {
            return new Gold(source);
        }

        @Override
        public Gold[] newArray(int size) {
            return new Gold[size];
        }
    };
}
