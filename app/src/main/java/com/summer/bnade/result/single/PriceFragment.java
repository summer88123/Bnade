package com.summer.bnade.result.single;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;

import com.summer.bnade.R;
import com.summer.bnade.base.BaseFragment;
import com.summer.lib.model.entity.AuctionRealmItem;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class PriceFragment extends BaseFragment {
    private static final String TAG = PriceFragment.class.getSimpleName();
    @BindView(R.id.list_view)
    RecyclerView mListView;
    @Inject
    ItemResultAdapter mAdapter;

    public static PriceFragment getInstance(FragmentManager fm) {
        PriceFragment fragment = (PriceFragment) fm.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new PriceFragment();
        }
        return fragment;
    }

    @Override
    public int title() {
        return R.string.fragment_title_current_price;
    }

    @Override
    public int layout() {
        return R.layout.fragment_price;
    }

    @Override
    public void setUpView() {
        mListView.setAdapter(mAdapter);
    }

    public void updateList(List<AuctionRealmItem> list) {
        mAdapter.update(list);
    }
}
