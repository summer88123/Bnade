package com.summer.bnade.search.entity;

import com.summer.bnade.base.BaseUIModel;
import com.summer.lib.model.entity.Hot;

import java.util.List;

/**
 * Created by kevin.bai on 2017/4/13.
 */

public class SearchVO extends BaseUIModel{
    private List<Hot> hotList;
    private List<String> histories;

    public SearchVO(List<Hot> hotList, List<String> histories) {
        super(false, false, null);
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
