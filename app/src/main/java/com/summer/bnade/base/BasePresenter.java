package com.summer.bnade.base;

import com.summer.bnade.data.BnadeRepo;
import com.summer.bnade.utils.RxUtil;

/**
 * Created by kevin.bai on 2017/4/16.
 */

public class BasePresenter<V extends BaseView> {
    protected V mView;
    protected BnadeRepo mRepo;
    protected RxUtil.BaseErrorHandler mErrorHandler;

    public BasePresenter(V view, BnadeRepo repo) {
        this.mView = view;
        this.mRepo = repo;
        mView.setPresenter(this);
        mErrorHandler = new RxUtil.BaseErrorHandler(mView);
    }
}
