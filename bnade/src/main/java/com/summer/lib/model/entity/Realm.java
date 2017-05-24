package com.summer.lib.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kevin.bai on 2017/4/16.
 */

public class Realm implements Parcelable {
    public static final Parcelable.Creator<Realm> CREATOR = new Parcelable.Creator<Realm>() {
        @Override
        public Realm createFromParcel(Parcel source) {
            return new Realm(source);
        }

        @Override
        public Realm[] newArray(int size) {
            return new Realm[size];
        }
    };
    private long id;
    private String connected;

    private Realm(long id) {
        this.id = id;
        this.connected = "未知服务器";
    }

    protected Realm(Parcel in) {
        this.id = in.readLong();
        this.connected = in.readString();
    }

    public static Realm unKnowInstance(long id) {
        return new Realm(id);
    }

    @Override
    public int hashCode() {
        return (int) (getId() ^ (getId() >>> 32));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Realm)) return false;

        Realm realm = (Realm) o;

        return getId() == realm.getId();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.connected);
    }

    public String getConnected() {
        return connected;
    }

    public void setConnected(String connected) {
        this.connected = connected;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
