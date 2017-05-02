package com.summer.bnade.search.entity;

import com.summer.lib.model.entity.Hot;

import java.util.List;

/**
 * Created by kevin.bai on 2017/4/13.
 */

public class SearchVO {
    private List<Hot> hotList;
    private List<String> histories;

    public SearchVO(List<Hot> hotList, List<String> histories) {
        this.hotList = hotList;
        this.histories = histories;
    }

    public List<String> getHistories() {
        return histories;
    }

    public List<Hot> getHotList() {
        return hotList;
    }

}
