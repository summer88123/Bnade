package com.summer.lib.model.entity;

import android.os.Parcel;

/**
 * Created by kevin.bai on 2017/4/3.
 */

public class Reagent extends Item {
    private int quality;
    private int count;

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.quality);
        dest.writeInt(this.count);
    }

    public Reagent() {
    }

    protected Reagent(Parcel in) {
        super(in);
        this.quality = in.readInt();
        this.count = in.readInt();
    }

    public static final Creator<Reagent> CREATOR = new Creator<Reagent>() {
        @Override
        public Reagent createFromParcel(Parcel source) {
            return new Reagent(source);
        }

        @Override
        public Reagent[] newArray(int size) {
            return new Reagent[size];
        }
    };
}
