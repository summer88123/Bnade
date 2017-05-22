package com.summer.bnade.token.entity;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

/**
 * Created by kevin.bai on 2017/4/9.
 */

public class WowTokenVO {

    private final String errorMsg;
    private final boolean inProgress;
    private final boolean success;
    private int currentGold;
    private long lastModified;
    private int minGold;
    private int maxGold;
    private List<Entry> oneDayTokens;
    private List<Entry> allTokens;

    private WowTokenVO(boolean inProgress, boolean success, String errorMsg) {
        this.inProgress = inProgress;
        this.success = success;
        this.errorMsg = errorMsg;
    }

    private WowTokenVO(boolean inProgress, boolean success) {
        this.inProgress = inProgress;
        this.success = success;
        this.errorMsg = null;
    }

    public static WowTokenVO inProgress() {
        return new WowTokenVO(true, false);
    }

    public static WowTokenVO success() {
        return new WowTokenVO(false, true);
    }

    public static WowTokenVO failure(String errorMsg) {
        return new WowTokenVO(false, false, errorMsg);
    }

    public List<Entry> getAllTokens() {
        return allTokens;
    }

    public void setAllTokens(List<Entry> allTokens) {
        this.allTokens = allTokens;
    }

    public int getCurrentGold() {
        return currentGold;
    }

    public void setCurrentGold(int currentGold) {
        this.currentGold = currentGold;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public int getMaxGold() {
        return maxGold;
    }

    public void setMaxGold(int maxGold) {
        this.maxGold = maxGold;
    }

    public int getMinGold() {
        return minGold;
    }

    public void setMinGold(int minGold) {
        this.minGold = minGold;
    }

    public List<Entry> getOneDayTokens() {
        return oneDayTokens;
    }

    public void setOneDayTokens(List<Entry> oneDayTokens) {
        this.oneDayTokens = oneDayTokens;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public boolean isSuccess() {
        return success;
    }
}
