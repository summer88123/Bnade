package com.summer.lib.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by kevin.bai on 2017/4/3.
 */

public class Spell implements Parcelable {
    private long id;
    private String name;
    private String icon;
    private List<Reagent> reagent;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<Reagent> getReagent() {
        return reagent;
    }

    public void setReagent(List<Reagent> reagent) {
        this.reagent = reagent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.icon);
        dest.writeTypedList(this.reagent);
    }

    public Spell() {
    }

    protected Spell(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.icon = in.readString();
        this.reagent = in.createTypedArrayList(Reagent.CREATOR);
    }

    public static final Parcelable.Creator<Spell> CREATOR = new Parcelable.Creator<Spell>() {
        @Override
        public Spell createFromParcel(Parcel source) {
            return new Spell(source);
        }

        @Override
        public Spell[] newArray(int size) {
            return new Spell[size];
        }
    };
}
