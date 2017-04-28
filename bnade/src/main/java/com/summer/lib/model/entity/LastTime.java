package com.summer.lib.model.entity;

/**
 * Created by kevin.bai on 2017/4/28.
 */

public enum LastTime {
    SHORT("短"),
    MEDIUM("中"),
    LONG("长"),
    VERY_LONG("非常长");

    String result;

    LastTime(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
