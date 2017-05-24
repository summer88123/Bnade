package com.summer.bnade.realmrank;

import com.summer.lib.model.entity.AuctionRealm;

import java.util.List;

/**
 * Created by kevin.bai on 2017/5/24.
 */

public class RealmRankUIModel {
    private boolean inProgress;
    private boolean success;
    private String errorMsg;
    private List<AuctionRealm> list;

    private RealmRankUIModel(boolean inProgress, boolean success) {
        this.inProgress = inProgress;
        this.success = success;
    }

    private RealmRankUIModel(List<AuctionRealm> list) {
        this(false, true);
        this.list = list;
    }

    private RealmRankUIModel(String errorMsg) {
        this(false, false);
        this.errorMsg = errorMsg;
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

    String getErrorMsg() {
        return errorMsg;
    }

    List<AuctionRealm> getList() {
        return list;
    }

    boolean isInProgress() {
        return inProgress;
    }

    boolean isSuccess() {
        return success;
    }
}
