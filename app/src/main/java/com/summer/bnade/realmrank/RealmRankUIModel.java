package com.summer.bnade.realmrank;

import com.summer.bnade.base.BaseUIModel;
import com.summer.lib.model.entity.AuctionRealm;

import java.util.List;

/**
 * Created by kevin.bai on 2017/5/24.
 */

class RealmRankUIModel extends BaseUIModel{
    private List<AuctionRealm> list;

    private RealmRankUIModel(boolean inProgress, boolean success) {
        super(inProgress, success, null);
    }

    private RealmRankUIModel(List<AuctionRealm> list) {
        this(false, true);
        this.list = list;
    }

    private RealmRankUIModel(String errorMsg) {
        super(false, false, errorMsg);
    }

    static RealmRankUIModel progress() {
        return new RealmRankUIModel(true, false);
    }

    static RealmRankUIModel success(List<AuctionRealm> list) {
        return new RealmRankUIModel(list);
    }

    static RealmRankUIModel failure(String errorMsg) {
        return new RealmRankUIModel(errorMsg);
    }

    List<AuctionRealm> getList() {
        return list;
    }
}
