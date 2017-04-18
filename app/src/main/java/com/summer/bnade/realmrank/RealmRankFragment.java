package com.summer.bnade.realmrank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.summer.bnade.R;
import com.summer.bnade.base.BaseFragment;
import com.summer.bnade.utils.DefaultViewUtil;
import com.summer.lib.model.entity.AuctionRealm;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * interface.
 */
public class RealmRankFragment extends BaseFragment implements RealmRankContract.View, SwipeRefreshLayout
        .OnRefreshListener {
    public static final String TAG = RealmRankFragment.class.getSimpleName();
    @BindView(R.id.list)
    RecyclerView mList;
    Unbinder unbinder;
    @BindView(R.id.tv_total_count)
    TextView mTvTotalCount;
    @BindView(R.id.tv_user_count)
    TextView mTvUserCount;
    @BindView(R.id.tv_item_kind)
    TextView mTvItemKind;
    @BindView(R.id.tv_update_time)
    TextView mTvUpdateTime;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindViews({R.id.tv_total_count, R.id.tv_user_count, R.id.tv_item_kind, R.id.tv_update_time})
    List<TextView> labels;


    private RealmRankContract.Presenter mPresenter;
    private RealmRankAdapter mAdapter;
    private AuctionRealm.SortType lastType;

    @SuppressWarnings("unused")
    public static RealmRankFragment newInstance() {
        return new RealmRankFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mList.setAdapter(mAdapter);
        DefaultViewUtil.defaultRefresh(mRefreshLayout);
        mRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.load();
    }

    @Override
    public void setPresenter(RealmRankContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void show(List<AuctionRealm> list, AuctionRealm.SortType sortType) {
        updateSortType(sortType);
        this.mAdapter.update(list);
    }

    private void updateSortType(AuctionRealm.SortType sortType) {
        this.lastType = sortType;
        // TODO: 2017/4/19 切换动画
    }

    @Override
    public void setDependency(RealmRankAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        mRefreshLayout.setRefreshing(refreshing);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        mPresenter.load();
    }

    @OnClick({R.id.tv_total_count, R.id.tv_user_count, R.id.tv_item_kind, R.id.tv_update_time})
    public void onViewClicked(View view) {
        AuctionRealm.SortType last = lastType;
        AuctionRealm.SortType current;
        switch (view.getId()) {
            case R.id.tv_user_count:
                current = last == AuctionRealm.SortType.PlayerDown
                        ? AuctionRealm.SortType.PlayerUp
                        : AuctionRealm.SortType.PlayerDown;
                break;
            case R.id.tv_item_kind:
                current = last == AuctionRealm.SortType.ItemDown
                        ? AuctionRealm.SortType.ItemUp
                        : AuctionRealm.SortType.ItemDown;
                break;
            case R.id.tv_update_time:
                current = last == AuctionRealm.SortType.TimeDown
                        ? AuctionRealm.SortType.TimeUp
                        : AuctionRealm.SortType.TimeDown;
                break;
            case R.id.tv_total_count:
            default:
                current = last == AuctionRealm.SortType.TotalDown
                        ? AuctionRealm.SortType.TotalUp
                        : AuctionRealm.SortType.TotalDown;
                break;
        }
        mPresenter.sort(current);
    }
}
