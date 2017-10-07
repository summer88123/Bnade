package com.summer.bnade.realm.rank;

import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout;
import com.jakewharton.rxbinding2.view.RxView;
import com.summer.bnade.R;
import com.summer.bnade.base.BaseFragment;
import com.summer.bnade.utils.DefaultViewUtil;
import com.summer.lib.model.entity.AuctionRealm;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import icepick.Icicle;
import io.reactivex.Observable;

/**
 * interface.
 */
public class RealmRankFragment extends BaseFragment<RealmRankUIModel> {
    public static final String TAG = RealmRankFragment.class.getSimpleName();
    @BindView(R.id.list)
    RecyclerView mList;
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
    @BindDrawable(value = R.drawable.ic_arrow_drop_up_black_24dp, tint = R.attr.colorAccent)
    Drawable tintUp;
    @BindDrawable(value = R.drawable.ic_arrow_drop_down_black_24dp, tint = R.attr.colorAccent)
    Drawable tintDown;

    @Inject
    RealmRankAdapter mAdapter;
    @Inject
    RealmRankTransformer mPresenter;

    @Icicle
    AuctionRealm.SortType current = AuctionRealm.SortType.TotalDown;

    private ButterKnife.Action<TextView> DisableRightDrawable = (view, index) -> view
            .setCompoundDrawables(null, null, null, null);

    @SuppressWarnings("unused")
    public static RealmRankFragment getInstance(FragmentManager fm) {
        RealmRankFragment fragment = (RealmRankFragment) fm.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new RealmRankFragment();
        }
        return fragment;
    }

    @Override
    protected void onProgress(boolean inProgress) {
        mRefreshLayout.setRefreshing(inProgress);
    }

    @Override
    protected void onSuccess(RealmRankUIModel model) {
        mAdapter.update(model.getList());
    }

    @Override
    public int layout() {
        return R.layout.fragment_item_list;
    }

    @Override
    public void setUpView() {
        mList.setAdapter(mAdapter);
        DefaultViewUtil.defaultRefresh(mRefreshLayout);
    }

    @Override
    public void setUpObservable() {
        Observable<AuctionRealm.SortType> userCountObservable = RxView.clicks(mTvUserCount)
                .map(o -> current == AuctionRealm.SortType.PlayerDown
                        ? AuctionRealm.SortType.PlayerUp
                        : AuctionRealm.SortType.PlayerDown);
        Observable<AuctionRealm.SortType> itemKindObservable = RxView.clicks(mTvItemKind)
                .map(o -> current == AuctionRealm.SortType.ItemDown
                        ? AuctionRealm.SortType.ItemUp
                        : AuctionRealm.SortType.ItemDown);
        Observable<AuctionRealm.SortType> updateTimeObservable = RxView.clicks(mTvUpdateTime)
                .map(o -> current == AuctionRealm.SortType.TimeDown
                        ? AuctionRealm.SortType.TimeUp
                        : AuctionRealm.SortType.TimeDown);
        Observable<AuctionRealm.SortType> totalCountObservable = RxView.clicks(mTvTotalCount)
                .map(o -> current == AuctionRealm.SortType.TotalDown
                        ? AuctionRealm.SortType.TotalUp
                        : AuctionRealm.SortType.TotalDown);

        Observable.merge(userCountObservable, itemKindObservable, updateTimeObservable, totalCountObservable)
                .startWith(current)
                .doOnNext(this::updateSortType)
                .compose(mPresenter.sort())
                .compose(bindToLifecycle())
                .subscribe(showAs());
        RxSwipeRefreshLayout.refreshes(mRefreshLayout)
                .map(o -> current)
                .mergeWith(untilEmit(FragmentEvent.START).map(ignore -> current))
                .compose(mPresenter.load())
                .compose(bindToLifecycle())
                .subscribe(showAs());
    }

    private void updateSortType(AuctionRealm.SortType sortType) {
        this.current = sortType;
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
}
