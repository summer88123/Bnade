package com.summer.bnade.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.summer.bnade.R;
import com.summer.bnade.base.mvp.BaseView;

/**
 * Created by kevin.bai on 2017/4/14.
 */

public abstract class BaseFragment<P> extends Fragment implements IActivityCreated, BaseView<P> {
    protected P mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(layout(), container, false);
    }
    @StringRes
    public int title(){
        return R.string.app_name;
    }

    @Override
    public void setPresenter(P p) {
        this.mPresenter = p;
    }

    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void showToast(@StringRes int resId) {
        Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
    }
}
