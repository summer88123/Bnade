package com.summer.bnade.base;

import com.summer.bnade.base.mvp.BaseView;

import javax.inject.Inject;

/**
 * Created by kevin.bai on 2017/5/3.
 */

public abstract class BaseViewActivity<P> extends BaseActivity implements BaseView<P> {
    @Inject
    protected P mPresenter;
    @Override
    public void setPresenter(P p) {
    }
}
