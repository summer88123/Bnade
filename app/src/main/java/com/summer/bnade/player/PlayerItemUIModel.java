package com.summer.bnade.player;

import com.summer.bnade.base.BaseUIModel;
import com.summer.lib.model.entity.Auction;

import java.util.List;

/**
 * Created by kevin.bai on 2017/6/13.
 */

class PlayerItemUIModel extends BaseUIModel {
    private List<Auction> auctions;

    private PlayerItemUIModel(boolean inProgress, boolean success, String errorMsg) {
        super(inProgress, success, errorMsg);
    }

    private PlayerItemUIModel(List<Auction> auctions) {
        super(false, true, null);
        this.auctions = auctions;
    }

    static PlayerItemUIModel progress() {
        return new PlayerItemUIModel(true, false, null);
    }

    static PlayerItemUIModel success(List<Auction> list) {
        return new PlayerItemUIModel(list);
    }

    static PlayerItemUIModel failure(String errorMsg) {
        return new PlayerItemUIModel(false, false, errorMsg);
    }

    public List<Auction> getAuctions() {
        return auctions;
    }
}
