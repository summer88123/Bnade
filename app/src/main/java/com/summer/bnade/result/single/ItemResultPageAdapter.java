package com.summer.bnade.result.single;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.summer.bnade.R;
import com.summer.lib.model.entity.Item;
import com.summer.lib.model.entity.Realm;

import javax.inject.Inject;

/**
 * Created by kevin.bai on 2017/5/3.
 */

class ItemResultPageAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private final Item item;
    private final Realm realm;

    @Inject
    ItemResultPageAdapter(ItemResultActivity activity) {
        super(activity.getSupportFragmentManager());
        this.mContext = activity.getApplicationContext();
        this.item = activity.item;
        this.realm = activity.realm;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return PriceFragment.getInstance(item, realm);
        } else {
            return HistoryFragment.getInstance(item, realm);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        int id;
        if (position == 0) {
            id = R.string.fragment_title_current_price;
        } else {
            id = R.string.fragment_title_history_trand;
        }
        return mContext.getString(id);
    }

}
