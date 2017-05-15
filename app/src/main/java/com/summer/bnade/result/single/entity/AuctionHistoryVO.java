package com.summer.bnade.result.single.entity;

import com.github.mikephil.charting.data.CombinedData;
import com.summer.lib.model.entity.Gold;

/**
 * Created by kevin.bai on 2017/5/15.
 */

public class AuctionHistoryVO {
    private HistoryItem oneDay;
    private HistoryItem lastWeek;
    private HistoryItem history;
    private CombinedData dataOneDay;
    private CombinedData dataHistory;

    public HistoryItem getOneDay() {
        return oneDay;
    }

    public void setOneDay(HistoryItem oneDay) {
        this.oneDay = oneDay;
    }

    public HistoryItem getLastWeek() {
        return lastWeek;
    }

    public void setLastWeek(HistoryItem lastWeek) {
        this.lastWeek = lastWeek;
    }

    public HistoryItem getHistory() {
        return history;
    }

    public void setHistory(HistoryItem history) {
        this.history = history;
    }

    public CombinedData getDataOneDay() {
        return dataOneDay;
    }

    public void setDataOneDay(CombinedData dataOneDay) {
        this.dataOneDay = dataOneDay;
    }

    public CombinedData getDataHistory() {
        return dataHistory;
    }

    public void setDataHistory(CombinedData dataHistory) {
        this.dataHistory = dataHistory;
    }

    public static class HistoryItem {
        private Gold avg;
        private Gold max;
        private Gold min;
        private int avgCount;

        public HistoryItem(Gold avg, Gold max, Gold min, int avgCount) {
            this.avg = avg;
            this.max = max;
            this.min = min;
            this.avgCount = avgCount;
        }

        public Gold getAvg() {
            return avg;
        }

        public void setAvg(Gold avg) {
            this.avg = avg;
        }

        public Gold getMax() {
            return max;
        }

        public void setMax(Gold max) {
            this.max = max;
        }

        public Gold getMin() {
            return min;
        }

        public void setMin(Gold min) {
            this.min = min;
        }

        public int getAvgCount() {
            return avgCount;
        }

        public void setAvgCount(int avgCount) {
            this.avgCount = avgCount;
        }
    }
}
