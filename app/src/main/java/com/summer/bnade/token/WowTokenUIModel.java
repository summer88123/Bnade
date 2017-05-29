package com.summer.bnade.token;

import com.github.mikephil.charting.data.Entry;
import com.summer.bnade.base.BaseUIModel;

import java.util.List;

/**
 * Created by kevin.bai on 2017/4/9.
 */

public class WowTokenUIModel extends BaseUIModel {

    private int currentGold;
    private long lastModified;
    private int minGold;
    private int maxGold;
    private List<Entry> oneDayTokens;
    private List<Entry> allTokens;

    private WowTokenUIModel(boolean inProgress, boolean success, String errorMsg) {
        super(inProgress, success, errorMsg);
    }

    private WowTokenUIModel(boolean inProgress, boolean success) {
        this(inProgress, success, null);
    }

    static WowTokenUIModel inProgress() {
        return new WowTokenUIModel(true, false);
    }

    static WowTokenUIModel success() {
        return new WowTokenUIModel(false, true);
    }

    static WowTokenUIModel failure(String errorMsg) {
        return new WowTokenUIModel(false, false, errorMsg);
    }

    List<Entry> getAllTokens() {
        return allTokens;
    }

    void setAllTokens(List<Entry> allTokens) {
        this.allTokens = allTokens;
    }

    int getCurrentGold() {
        return currentGold;
    }

    void setCurrentGold(int currentGold) {
        this.currentGold = currentGold;
    }

    long getLastModified() {
        return lastModified;
    }

    void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    int getMaxGold() {
        return maxGold;
    }

    void setMaxGold(int maxGold) {
        this.maxGold = maxGold;
    }

    int getMinGold() {
        return minGold;
    }

    void setMinGold(int minGold) {
        this.minGold = minGold;
    }

    List<Entry> getOneDayTokens() {
        return oneDayTokens;
    }

    void setOneDayTokens(List<Entry> oneDayTokens) {
        this.oneDayTokens = oneDayTokens;
    }

}
