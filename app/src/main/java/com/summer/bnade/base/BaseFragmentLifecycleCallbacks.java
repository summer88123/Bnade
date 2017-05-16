package com.summer.bnade.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kevin.bai on 2017/5/16.
 */

public class BaseFragmentLifecycleCallbacks extends FragmentManager.FragmentLifecycleCallbacks {

    private Map<Fragment, Unbinder> cache = new HashMap<>();

    @Override
    public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState);
        if (f instanceof IActivityCreated) {
            IActivityCreated created = (IActivityCreated) f;
            cache.put(f, ButterKnife.bind(f, v));
            created.setUpView();
        }

    }

    @Override
    public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
        super.onFragmentViewDestroyed(fm, f);
        if (f instanceof IActivityCreated) {
            cache.remove(f).unbind();
        }
    }
}
