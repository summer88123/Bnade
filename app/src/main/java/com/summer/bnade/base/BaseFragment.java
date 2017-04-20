package com.summer.bnade.base;

import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by kevin.bai on 2017/4/14.
 */

public abstract class BaseFragment<P> extends Fragment implements BaseView<P>{
    protected P mPresenter;

    @Override
    public void setPresenter(P p) {
        this.mPresenter = p;
    }

    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
