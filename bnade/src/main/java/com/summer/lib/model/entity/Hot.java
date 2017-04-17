package com.summer.lib.model.entity;

/**
 * Created by kevin.bai on 2017/4/3.
 */

public class Hot extends Item{
    public static final int MONTH = 3;
    public static final int WEEK = 2;
    public static final int DAY = 1;

    private int queride;
    private int type;

    public int getQueride() {
        return queride;
    }

    public void setQueride(int queride) {
        this.queride = queride;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
