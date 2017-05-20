package com.summer.bnade.player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.summer.bnade.R;
import com.summer.bnade.base.BaseViewFragment;
import com.summer.bnade.home.MainComponent;
import com.summer.bnade.home.Provider;
import com.summer.bnade.select.RealmSelectActivity;
import com.summer.bnade.utils.Content;
import com.summer.bnade.widget.RealmSelectButton;
import com.summer.lib.model.entity.Auction;
import com.summer.lib.model.entity.Realm;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class PlayerItemFragment extends BaseViewFragment<PlayerItemContract.Presenter> implements PlayerItemContract
        .View {
    public static final String TAG = PlayerItemFragment.class.getSimpleName();
    @BindView(R.id.select_btn)
    RealmSelectButton mBtnRealmSelect;
    @BindView(R.id.searchView)
    SearchView mSearchView;
    @BindView(R.id.list)
    RecyclerView mList;

    @Inject
    PlayerItemAdapter mAdapter;

    @SuppressWarnings("unused")
    public static PlayerItemFragment getInstance(FragmentManager fm) {
        PlayerItemFragment fragment = (PlayerItemFragment) fm.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new PlayerItemFragment();
        }
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Content.REQUEST_SELECT_REALM && resultCode == Activity.RESULT_OK && data != null) {
            Realm realm = data.getParcelableExtra(Content.EXTRA_DATA);
            mBtnRealmSelect.setRealm(realm);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Provider) {
            MainComponent component = (MainComponent) ((Provider) context).provide();
            component.inject(this);
        }
    }

    @Override
    public void showList(List<Auction> auctions) {
        mAdapter.update(auctions);
    }

    @Override
    public int layout() {
        return R.layout.fragment_auction_list;
    }

    @Override
    public void setUpView() {
        mList.setAdapter(mAdapter);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Realm current = mBtnRealmSelect.getRealm();
                if (current == null) {
                    showToast(R.string.toast_player_item_no_select_realm);
                } else {
                    mPresenter.search(s, current);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @OnClick(R.id.select_btn)
    public void onClick() {
        startActivityForResult(new Intent(getContext(), RealmSelectActivity.class), Content.REQUEST_SELECT_REALM);
    }

}
