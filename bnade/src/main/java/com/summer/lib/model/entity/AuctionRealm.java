package com.summer.lib.model.entity;

/**
 * Created by kevin.bai on 2017/4/4.
 */

public class AuctionRealm {
    public static final String PVP = "pvp";
    public static final String PVE = "pve";
    private long id;
    private String type;
    private Realm realm;
    private int auctionQuantity;
    private int playerQuantity;
    private int itemQuantity;
    private long lastModified;

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + getRealm().hashCode();
        result = 31 * result + getAuctionQuantity();
        result = 31 * result + getPlayerQuantity();
        result = 31 * result + getItemQuantity();
        result = 31 * result + (int) (getLastModified() ^ (getLastModified() >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuctionRealm)) return false;

        AuctionRealm that = (AuctionRealm) o;

        if (getId() != that.getId()) return false;
        if (getAuctionQuantity() != that.getAuctionQuantity()) return false;
        if (getPlayerQuantity() != that.getPlayerQuantity()) return false;
        if (getItemQuantity() != that.getItemQuantity()) return false;
        if (getLastModified() != that.getLastModified()) return false;
        return getRealm().equals(that.getRealm());
    }

    public int getAuctionQuantity() {
        return auctionQuantity;
    }

    public void setAuctionQuantity(int auctionQuantity) {
        this.auctionQuantity = auctionQuantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public int getPlayerQuantity() {
        return playerQuantity;
    }

    public void setPlayerQuantity(int playerQuantity) {
        this.playerQuantity = playerQuantity;
    }

    public Realm getRealm() {
        return realm;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public enum SortType {
        TotalUp, TotalDown, PlayerUp, PlayerDown, ItemUp, ItemDown, TimeUp, TimeDown
    }
}
