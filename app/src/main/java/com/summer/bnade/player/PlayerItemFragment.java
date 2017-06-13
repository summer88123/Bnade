package com.summer.bnade.player;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;
import com.summer.bnade.R;
import com.summer.bnade.base.BaseFragment;
import com.summer.bnade.select.RealmSelectActivity;
import com.summer.bnade.utils.Content;
import com.summer.bnade.widget.RealmSelectButton;
import com.summer.lib.model.entity.Realm;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

public class PlayerItemFragment extends BaseFragment<PlayerItemUIModel> {
    public static final String TAG = PlayerItemFragment.class.getSimpleName();
    @BindView(R.id.select_btn)
    RealmSelectButton mBtnRealmSelect;
    @BindView(R.id.searchView)
    SearchView mSearchView;
    @BindView(R.id.list)
    RecyclerView mList;

    @Inject
    PlayerItemTransformer mPresenter;
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
    protected void onSuccess(PlayerItemUIModel model) {
        mAdapter.update(model.getAuctions());
    }

    @Override
    public int layout() {
        return R.layout.fragment_auction_list;
    }

    @Override
    public void setUpView() {
        mList.setAdapter(mAdapter);
    }

    @Override
    public void setUpObservable() {
        RxSearchView.queryTextChangeEvents(mSearchView)
                .map(event -> new PlayerItemAction(event.queryText(), mBtnRealmSelect.getRealm()))
                .flatMap(action -> {
                    if (action.getSelect() == null) {
                        return Observable
                                .error(new RuntimeException(getString(R.string.toast_player_item_no_select_realm)));
                    } else {
                        return Observable.just(action);
                    }
                })
                .compose(mPresenter.search())
                .compose(bindToLifecycle())
                .subscribe(showAs());
    }

    @OnClick(R.id.select_btn)
    public void onClick() {
        startActivityForResult(new Intent(getContext(), RealmSelectActivity.class), Content.REQUEST_SELECT_REALM);
    }

}
