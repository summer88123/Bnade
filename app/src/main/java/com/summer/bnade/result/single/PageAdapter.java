package com.summer.bnade.result.single;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by kevin.bai on 2017/5/3.
 */

class PageAdapter extends FragmentPagerAdapter {
    private PageFragment[] mFragments;
    private Context mContext;

    PageAdapter(Context context, FragmentManager fm, PageFragment... fragments) {
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

    public static abstract class PageFragment extends Fragment {
        @StringRes
        abstract int title();
    }
}
