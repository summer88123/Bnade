package com.summer.bnade.result.single;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.summer.bnade.base.BaseFragment;

/**
 * Created by kevin.bai on 2017/5/3.
 */

class PageAdapter extends FragmentStatePagerAdapter {
    private BaseFragment[] mFragments;
    private Context mContext;

    PageAdapter(Context context, FragmentManager fm, BaseFragment... fragments) {
        super(fm);
        this.mFragments = fragments;
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(mFragments[position].title());
    }

}
