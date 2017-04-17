package com.summer.bnade.base;

import com.summer.bnade.data.BnadeRepo;

/**
 * Created by kevin.bai on 2017/4/16.
 */

public class BasePresenter<View extends BaseView> {
    protected View mView;
    protected BnadeRepo mRepo;

    public BasePresenter(View view, BnadeRepo repo) {
        this.mView = view;
        this.mRepo = repo;
        mView.setPresenter(this);
    }
}
