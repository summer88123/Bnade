package com.summer.bnade.search;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;
import com.jakewharton.rxbinding2.support.v7.widget.SearchViewQueryTextEvent;
import com.jakewharton.rxbinding2.widget.RxRadioGroup;
import com.summer.bnade.R;
import com.summer.bnade.base.BaseFragment;
import com.summer.bnade.result.all.SearchResultActivity;
import com.summer.bnade.result.single.ItemResultActivity;
import com.summer.bnade.search.entity.SearchVO;
import com.summer.bnade.select.RealmSelectActivity;
import com.summer.bnade.utils.Content;
import com.summer.bnade.widget.RealmSelectButton;
import com.summer.lib.model.entity.Hot;
import com.summer.lib.model.entity.Item;
import com.summer.lib.model.entity.Realm;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import icepick.Icicle;

import static com.summer.bnade.R.id.searchView;

public class SearchFragment extends BaseFragment<SearchVO> implements SearchContract.View, OnTabClickListener {
    public static final String TAG = SearchFragment.class.getSimpleName();
    @BindView(searchView)
    SearchView mSearchView;
    @BindView(R.id.rg_hot_type)
    RadioGroup mRgHotType;
    @BindView(R.id.rv_hot)
    RecyclerView mRvHot;
    @BindView(R.id.ib_clear_histories)
    ImageButton mIbClearHistories;
    @BindView(R.id.list_histories)
    RecyclerView mListHistories;

    ListPopupWindow mFuzzyList;
    @BindView(R.id.content)
    ConstraintLayout mContent;
    @BindView(R.id.select_btn)
    RealmSelectButton mSelectBtn;

    @Inject
    SearchPresenter mPresenter;
    @Inject
    HotSearchAdapter mHotAdapter;
    @Inject
    HistoryAdapter mHistoriesAdapter;
    @Inject
    FuzzyItemAdapter mFuzzyAdapter;

    @Icicle
    @IdRes
    int mCurrentId = R.id.rb_month;

    public static SearchFragment getInstance(FragmentManager fm) {
        SearchFragment fragment = (SearchFragment) fm.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new SearchFragment();
        }
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Content.REQUEST_SELECT_REALM && resultCode == Activity.RESULT_OK && data != null) {
            mSelectBtn.setRealm(data.getParcelableExtra(Content.EXTRA_DATA));
        }
    }

    @Override
    public void show(SearchVO searchVO) {
        updateHotSearch(searchVO.getHotList());
        updateHistories(searchVO.getHistories());
    }

    private void search(String query) {
        mPresenter.search(query, mSelectBtn.getRealm());
    }

    @Override
    public void showFuzzySearch(List<String> names) {
        mFuzzyAdapter.update(names);
        if (Build.VERSION.SDK_INT == 24) {
            int[] a = new int[2];
            mSearchView.getLocationInWindow(a);
            mFuzzyList.setHeight(getResources().getDisplayMetrics().heightPixels - a[1] - mSearchView.getHeight());
        }
        mFuzzyList.show();
    }

    @Override
    public void showRealmItemResult(Item item, Realm realm) {
        Intent intent = new Intent(getActivity(), ItemResultActivity.class);
        intent.putExtra(Content.EXTRA_DATA, item);
        intent.putExtra(Content.EXTRA_SUB_DATA, realm);
        startActivity(intent);
    }

    @Override
    public void showResult(Item item, Realm realm) {
        Intent intent = new Intent(getActivity(), SearchResultActivity.class);
        intent.putExtra(Content.EXTRA_DATA, item);
        startActivity(intent);
    }

    @Override
    public void updateHistories(List<String> histories) {
        mIbClearHistories.setVisibility(histories.isEmpty() ? View.INVISIBLE : View.VISIBLE);
        mHistoriesAdapter.update(histories);
    }

    @Override
    public void updateHotSearch(List<Hot> hotList) {
        mHotAdapter.update(hotList);
    }

    @Override
    public int layout() {
        return R.layout.fragment_search;
    }

    @Override
    public void setUpView() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager();
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);

        mRvHot.setLayoutManager(layoutManager);
        mRvHot.setAdapter(mHotAdapter);

        layoutManager = new FlexboxLayoutManager();
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        mListHistories.setLayoutManager(layoutManager);
        mListHistories.setAdapter(mHistoriesAdapter);

        mFuzzyList = new ListPopupWindow(getContext());
        mFuzzyList.setWidth(ListPopupWindow.WRAP_CONTENT);//设置宽度
        mFuzzyList.setHeight(ListPopupWindow.WRAP_CONTENT);//设置高度
        mFuzzyList.setAdapter(mFuzzyAdapter);
        mFuzzyList.setAnchorView(mSearchView);
        mFuzzyList.setModal(true);
        mFuzzyList.setOnItemClickListener((parent, view, position, id) -> {
            mFuzzyList.dismiss();
            mSearchView.setQuery(mFuzzyAdapter.getItem(position), true);
        });
        mRgHotType.check(mCurrentId);
        RxRadioGroup.checkedChanges(mRgHotType)
                .subscribe(i -> {
                    mCurrentId = i;
                    mPresenter.updateHotSearchType(getHotType(i));
                });
        RxSearchView.queryTextChangeEvents(mSearchView)
                .filter(SearchViewQueryTextEvent::isSubmitted)
                .map(event -> event.queryText().toString())
                .subscribe(this::search);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.load(getHotType(mRgHotType.getCheckedRadioButtonId()));
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.updateHistory();
    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {

    }

    @OnClick(R.id.ib_clear_histories)
    public void onClick() {
        mPresenter.clearHistories();
    }

    @Override
    public void onClick(String name) {
        search(name);
    }

    @OnClick(R.id.select_btn)
    public void onViewClicked() {
        startActivityForResult(new Intent(getContext(), RealmSelectActivity.class), Content
                .REQUEST_SELECT_REALM);
    }

    public void search() {
        if (mSearchView.isIconified()) {
            mSearchView.setIconified(false);
        } else {
            String query = mSearchView.getQuery().toString();
            if (!TextUtils.isEmpty(query)) {
                search(query);
            }
        }
    }

    private int getHotType(@IdRes int resId) {
        switch (resId) {
            case R.id.rb_week:
                return Hot.WEEK;
            case R.id.rb_day:
                return Hot.DAY;
            case R.id.rb_month:
            default:
                return Hot.MONTH;
        }
    }

}
