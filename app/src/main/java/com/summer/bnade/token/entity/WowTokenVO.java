package com.summer.bnade.token.entity;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

/**
 * Created by kevin.bai on 2017/4/9.
 */

public class WowTokenVO {
    private int currentGold;
    private long lastModified;
    private int minGold;
    private int maxGold;
    private List<Entry> tokens;

    public List<Entry> getTokens() {
        return tokens;
    }

    public void setTokens(List<Entry> tokens) {
        this.tokens = tokens;
    }

    public int getCurrentGold() {
        return currentGold;
    }

    public void setCurrentGold(int currentGold) {
        this.currentGold = currentGold;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public int getMinGold() {
        return minGold;
    }

    public void setMinGold(int minGold) {
        this.minGold = minGold;
    }

    public int getMaxGold() {
        return maxGold;
    }

    public void setMaxGold(int maxGold) {
        this.maxGold = maxGold;
    }
}
