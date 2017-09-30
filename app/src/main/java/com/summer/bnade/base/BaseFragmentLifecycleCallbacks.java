package com.summer.bnade.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.Icepick;

/**
 * Created by kevin.bai on 2017/5/16.
 */
@Singleton
public class BaseFragmentLifecycleCallbacks extends FragmentManager.FragmentLifecycleCallbacks {

    private Map<Fragment, Unbinder> cache = new HashMap<>();

    @Inject
    BaseFragmentLifecycleCallbacks() {
    }

    @Override
    public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        super.onFragmentCreated(fm, f, savedInstanceState);
        Icepick.restoreInstanceState(f, savedInstanceState);
    }

    @Override
    public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState);
        if (f instanceof IActivityCreated) {
            IActivityCreated created = (IActivityCreated) f;
            cache.put(f, ButterKnife.bind(f, v));
            created.injectComponent();
            created.setUpView();
            created.setUpObservable();
        }

    }

    @Override
    public void onFragmentSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
        super.onFragmentSaveInstanceState(fm, f, outState);
        Icepick.saveInstanceState(f, outState);
    }

    @Override
    public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
        super.onFragmentViewDestroyed(fm, f);
        if (f instanceof IActivityCreated) {
            cache.remove(f).unbind();
        }
    }
}
