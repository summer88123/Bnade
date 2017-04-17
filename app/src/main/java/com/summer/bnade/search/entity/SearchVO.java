package com.summer.bnade.search.entity;

import com.summer.lib.model.entity.Hot;

import java.util.List;
import java.util.Map;

/**
 * Created by kevin.bai on 2017/4/13.
 */

public class SearchVO {
    private int currentType;
    private Map<Integer, List<Hot>> map;
    private List<String> histories;

    public List<String> getHistories() {
        return histories;
    }

    public void setHistories(List<String> histories) {
        this.histories = histories;
    }

    public List<Hot> getHotList() {
        if (map == null) {
            return null;
        }
        return map.get(currentType);
    }

    public void setCurrentType(int currentType) {
        this.currentType = currentType;
    }

    public void setMap(Map<Integer, List<Hot>> map) {
        this.map = map;
    }
}
