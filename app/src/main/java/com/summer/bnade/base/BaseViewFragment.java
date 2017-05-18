package com.summer.bnade.base;

import com.summer.bnade.base.mvp.BaseView;

import javax.inject.Inject;

/**
 * Created by kevin.bai on 2017/4/14.
 */

public abstract class BaseViewFragment<P> extends BaseFragment implements BaseView<P> {
    @Inject
    protected P mPresenter;

    @Override
    public void setPresenter(P p) {
        this.mPresenter = p;
    }

}
