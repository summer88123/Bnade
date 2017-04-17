package com.summer.lib.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.summer.lib.model.api.BnadeApi;

import java.util.List;

/**
 * Created by kevin.bai on 2017/4/3.
 */

public class Item implements Parcelable {
    private long id;
    private String name;
    private String icon;
    private int itemLevel;
    private List<String> bonusList;
    private List<Spell> createdBy;

    public long getId() {
        return id;
    }

    public String getUrl() {
        return BnadeApi.BASE_ICON_URL + icon + ".jpg";
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

    public int getItemLevel() {
        return itemLevel;
    }

    public void setItemLevel(int itemLevel) {
        this.itemLevel = itemLevel;
    }

    public List<String> getBonusList() {
        return bonusList;
    }

    public void setBonusList(List<String> bonusList) {
        this.bonusList = bonusList;
    }

    public List<Spell> getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(List<Spell> createdBy) {
        this.createdBy = createdBy;
    }

    public Item() {
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
        dest.writeInt(this.itemLevel);
        dest.writeStringList(this.bonusList);
        dest.writeTypedList(this.createdBy);
    }

    protected Item(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.icon = in.readString();
        this.itemLevel = in.readInt();
        this.bonusList = in.createStringArrayList();
        this.createdBy = in.createTypedArrayList(Spell.CREATOR);
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
