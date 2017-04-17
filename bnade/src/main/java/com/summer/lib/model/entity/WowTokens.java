package com.summer.lib.model.entity;

/**
 * Created by kevin.bai on 2017/4/3.
 */

public class WowTokens {
    private long lastModified;
    private int gold;

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}
