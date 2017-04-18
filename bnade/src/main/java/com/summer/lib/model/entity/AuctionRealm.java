package com.summer.lib.model.entity;

/**
 * Created by kevin.bai on 2017/4/4.
 */

public class AuctionRealm {
    public enum SortType {
        TotalUp, TotalDown, PlayerUp, PlayerDown, ItemUp, ItemDown, TimeUp, TimeDown
    }

    public static final String PVP = "pvp";
    public static final String PVE = "pve";

    private long id;
    private String type;
    private Realm realm;
    private int auctionQuantity;
    private int playerQuantity;
    private int itemQuantity;
    private long lastModified;

    public Realm getRealm() {
        return realm;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAuctionQuantity() {
        return auctionQuantity;
    }

    public void setAuctionQuantity(int auctionQuantity) {
        this.auctionQuantity = auctionQuantity;
    }

    public int getPlayerQuantity() {
        return playerQuantity;
    }

    public void setPlayerQuantity(int playerQuantity) {
        this.playerQuantity = playerQuantity;
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
}
