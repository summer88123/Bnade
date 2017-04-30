package com.summer.bnade.select.entity;

import com.summer.lib.model.entity.Realm;

import java.util.List;

/**
 * Created by kevin.bai on 2017/5/1.
 */

public class RealmSelectVO {
    List<Realm> realms;
    List<String> histories;

    public RealmSelectVO(List<Realm> realms, List<String> histories) {
        this.realms = realms;
        this.histories = histories;
    }

    public List<Realm> getRealms() {
        return realms;
    }

    public List<String> getHistories() {
        return histories;
    }
}
