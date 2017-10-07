package com.summer.bnade.base;

import android.support.annotation.LayoutRes;

/**
 * Created by kevin.bai on 2017/5/16.
 */

public interface IActivityCreated {

    /**
     * 用于获取当前Activity的layout布局
     */
    @LayoutRes int layout();

    /**
     * 初始化view设置
     */
    void setUpView();

    void setUpObservable();
}
