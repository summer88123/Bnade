package com.summer.bnade.player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.summer.bnade.R;
import com.summer.bnade.base.BaseFragment;
import com.summer.bnade.select.RealmSelectActivity;
import com.summer.bnade.utils.Content;
import com.summer.lib.model.entity.Auction;
import com.summer.lib.model.entity.Realm;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PlayerItemFragment extends BaseFragment<PlayerItemContract.Presenter> implements PlayerItemContract.View {
    public static final String TAG = PlayerItemFragment.class.getSimpleName();
    @BindView(R.id.btn_realm_select)
    Button mBtnRealmSelect;
    @BindView(R.id.searchView)
    SearchView mSearchView;
    @BindView(R.id.list)
    RecyclerView mList;
    Unbinder unbinder;

    private PlayerItemAdapter mAdapter;

    @SuppressWarnings("unused")
    public static PlayerItemFragment getInstance(FragmentManager fm) {
        PlayerItemFragment fragment = (PlayerItemFragment) fm.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new PlayerItemFragment();
        }
        return fragment;
    }

    @OnClick(R.id.btn_realm_select)
    public void onClick() {
        startActivityForResult(new Intent(getContext(), RealmSelectActivity.class), Content.REQUEST_SELECT_REALM);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new PlayerItemAdapter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auction_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        mList.setAdapter(mAdapter);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mPresenter.search(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Content.REQUEST_SELECT_REALM && resultCode == Activity.RESULT_OK && data != null) {
            Realm realm = data.getParcelableExtra(Content.EXTRA_DATA);
            mBtnRealmSelect.setText(realm.getConnected());
            mPresenter.selectRealm(realm);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showList(List<Auction> auctions) {
        mAdapter.update(auctions);
    }

    @Override
    public void showToastNoRealm() {
        showToast(R.string.toast_player_item_no_select_realm);
    }
}
