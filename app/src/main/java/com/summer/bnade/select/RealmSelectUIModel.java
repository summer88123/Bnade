package com.summer.bnade.select;

import com.summer.bnade.base.BaseUIModel;
import com.summer.bnade.select.entity.TypedRealm;

import java.util.List;

/**
 * Created by kevin.bai on 2017/5/29.
 */

public class RealmSelectUIModel extends BaseUIModel {
    private List<TypedRealm> list;

    private RealmSelectUIModel(boolean inProgress, boolean success, String errorMsg) {
        super(inProgress, success, errorMsg);
    }

    private RealmSelectUIModel(List<TypedRealm> list) {
        this(false, true, null);
        this.list = list;
    }

    static RealmSelectUIModel success(List<TypedRealm> list) {
        return new RealmSelectUIModel(list);
    }

    static RealmSelectUIModel progress() {
        return new RealmSelectUIModel(true, false, null);
    }

    static RealmSelectUIModel failure(String errorMsg) {
        return new RealmSelectUIModel(false, false, errorMsg);
    }

    public List<TypedRealm> getList() {
        return list;
    }
}
