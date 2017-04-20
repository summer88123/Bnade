package com.summer.bnade.realmrank;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
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

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * interface.
 */
public class RealmRankFragment extends BaseFragment<RealmRankContract.Presenter> implements RealmRankContract.View,
        SwipeRefreshLayout
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
    @BindDrawable(R.drawable.ic_arrow_drop_up_black_24dp)
    Drawable up;
    @BindDrawable(R.drawable.ic_arrow_drop_down_black_24dp)
    Drawable down;

    Drawable tintUp;
    Drawable tintDown;


    private RealmRankAdapter mAdapter;
    private AuctionRealm.SortType lastType;

    private ButterKnife.Action<TextView> DisableRightDrawable = new ButterKnife.Action<TextView>() {
        @Override
        public void apply(@NonNull TextView view, int index) {
            view.setCompoundDrawables(null, null, null, null);
        }
    };

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
        tintUp = DrawableCompat.wrap(up);
        DrawableCompat.setTint(tintUp, ContextCompat.getColor(getContext(), R.color.colorAccent));
        tintDown = DrawableCompat.wrap(down);
        DrawableCompat.setTint(tintDown, ContextCompat.getColor(getContext(), R.color.colorAccent));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.load();
    }

    @Override
    public void show(List<AuctionRealm> list, AuctionRealm.SortType sortType) {
        updateSortType(sortType);
        this.mAdapter.update(list);
    }

    private void updateSortType(AuctionRealm.SortType sortType) {
        this.lastType = sortType;
        ButterKnife.apply(labels, DisableRightDrawable);
        switch (sortType) {
            case TotalUp:
                mTvTotalCount.setCompoundDrawablesWithIntrinsicBounds(null, null, tintUp, null);
                break;
            case TotalDown:
                mTvTotalCount.setCompoundDrawablesWithIntrinsicBounds(null, null, tintDown, null);
                break;
            case PlayerUp:
                mTvUserCount.setCompoundDrawablesWithIntrinsicBounds(null, null, tintUp, null);
                break;
            case PlayerDown:
                mTvUserCount.setCompoundDrawablesWithIntrinsicBounds(null, null, tintDown, null);
                break;
            case ItemUp:
                mTvItemKind.setCompoundDrawablesWithIntrinsicBounds(null, null, tintUp, null);
                break;
            case ItemDown:
                mTvItemKind.setCompoundDrawablesWithIntrinsicBounds(null, null, tintDown, null);
                break;
            case TimeUp:
                mTvUpdateTime.setCompoundDrawablesWithIntrinsicBounds(null, null, tintUp, null);
                break;
            case TimeDown:
                mTvUpdateTime.setCompoundDrawablesWithIntrinsicBounds(null, null, tintDown, null);
                break;
        }
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
