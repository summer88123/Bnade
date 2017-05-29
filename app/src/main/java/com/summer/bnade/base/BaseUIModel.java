package com.summer.bnade.base;

/**
 * Created by kevin.bai on 2017/5/29.
 */

public class BaseUIModel {
    private final boolean success;
    private final boolean inProgress;
    private final String errorMsg;

    public BaseUIModel(boolean inProgress, boolean success, String errorMsg) {
        this.inProgress = inProgress;
        this.success = success;
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
