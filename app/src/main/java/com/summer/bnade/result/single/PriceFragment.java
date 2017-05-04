package com.summer.bnade.result.single;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.summer.bnade.R;
import com.summer.bnade.search.entity.SearchResultVO;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PriceFragment extends PageAdapter.PageFragment {
    private static final String TAG = PriceFragment.class.getSimpleName();
    @BindView(R.id.list_view)
    RecyclerView mListView;
    @Inject
    ItemResultAdapter mAdapter;

    Unbinder unbinder;

    public static PriceFragment getInstance(FragmentManager fm) {
        PriceFragment fragment = (PriceFragment) fm.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new PriceFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_price, container, false);
        unbinder = ButterKnife.bind(this, view);
        mListView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    int title() {
        return R.string.fragment_title_current_price;
    }

    public void updateList(SearchResultVO vo) {
        mAdapter.update(vo.getAuctionRealmItems());
    }
}
