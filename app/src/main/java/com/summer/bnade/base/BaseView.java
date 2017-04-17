package com.summer.bnade.base;

/**
 * Created by kevin.bai on 2017/4/11.
 */

public interface BaseView<P> {
    void setPresenter(P p);

    void showToast(String message);
}
