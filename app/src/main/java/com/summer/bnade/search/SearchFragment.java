package com.summer.bnade.search;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.TransitionManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.summer.bnade.R;
import com.summer.bnade.base.BaseFragment;
import com.summer.bnade.result.SearchRealmItemResultActivity;
import com.summer.bnade.result.SearchResultActivity;
import com.summer.bnade.search.entity.SearchResultVO;
import com.summer.bnade.search.entity.SearchVO;
import com.summer.bnade.select.RealmSelectActivity;
import com.summer.bnade.utils.Content;
import com.summer.lib.model.entity.Hot;
import com.summer.lib.model.entity.Realm;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.summer.bnade.R.id.searchView;

public class SearchFragment extends BaseFragment<SearchContract.Presenter> implements SearchContract.View {
    public static final String TAG = SearchFragment.class.getSimpleName();
    @BindView(searchView)
    SearchView mSearchView;
    @BindView(R.id.rg_hot_type)
    RadioGroup mRgHotType;
    @BindView(R.id.rv_hot)
    RecyclerView mRvHot;
    Unbinder unbinder;
    @BindView(R.id.ib_clear_histories)
    ImageButton mIbClearHistories;
    @BindView(R.id.list_histories)
    RecyclerView mListHistories;
    @Inject
    HotSearchAdapter mHotAdapter;
    @Inject
    HistoryAdapter mHistoriesAdapter;
    @Inject
    FuzzyItemAdapter mFuzzyAdapter;
    @BindView(R.id.btn_realm_select)
    Button mBtnRealmSelect;
    @BindView(R.id.btn_realm_clear)
    Button mBtnRealmClear;
    ListPopupWindow mFuzzyList;
    @BindView(R.id.content)
    ConstraintLayout mContent;

    ConstraintSet hasRealmSet = new ConstraintSet();
    ConstraintSet noRealmSet = new ConstraintSet();

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Content.REQUEST_SELECT_REALM && resultCode == Activity.RESULT_OK && data != null) {
            selectRealm(data.<Realm>getParcelableExtra(Content.EXTRA_DATA));
        }
    }

    private void selectRealm(Realm realm) {
        mBtnRealmSelect.setText(realm.getConnected());
        mBtnRealmSelect.setTag(realm);
        TransitionManager.beginDelayedTransition(mContent);
        hasRealmSet.setVisibility(R.id.btn_realm_clear, ConstraintSet.VISIBLE);
        hasRealmSet.connect(R.id.btn_realm_select, ConstraintSet.RIGHT, R.id.btn_realm_clear, ConstraintSet.LEFT);
        hasRealmSet.connect(R.id.btn_realm_clear, ConstraintSet.LEFT, R.id.btn_realm_select, ConstraintSet.RIGHT);
        hasRealmSet.applyTo(mContent);
    }

    private void clearRealm(){
        mBtnRealmSelect.setTag(null);
        mBtnRealmSelect.setText(R.string.btn_realm_select);
        TransitionManager.beginDelayedTransition(mContent);
        noRealmSet.applyTo(mContent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, view);
        hasRealmSet.clone(mContent);
        noRealmSet.clone(mContent);

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
        mFuzzyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                mFuzzyList.dismiss();
                search(((TextView) view).getText().toString());
            }
        });

        mRgHotType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                mPresenter.updateHotSearchType(getHotType(i));
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                search(s);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.load(getHotType(mRgHotType.getCheckedRadioButtonId()));
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.updateHistory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void show(SearchVO searchVO) {
        updateHotSearch(searchVO.getHotList());
        updateHistories(searchVO.getHistories());
    }

    @Override
    public void search(String query) {
        mPresenter.search(query, (Realm) mBtnRealmSelect.getTag());
    }

    @Override
    public void showFuzzySearch(SearchResultVO searchResultVO) {
        mFuzzyAdapter.update(searchResultVO.getNames());
        if (Build.VERSION.SDK_INT == 24) {
            int[] a = new int[2];
            mSearchView.getLocationInWindow(a);
            mFuzzyList.setHeight(getResources().getDisplayMetrics().heightPixels - a[1] - mSearchView.getHeight());
        }
        mFuzzyList.show();
    }

    @Override
    public void showRealmItemResult(SearchResultVO searchResultVO) {
        Intent intent = new Intent(getActivity(), SearchRealmItemResultActivity.class);
        intent.putExtra(Content.EXTRA_DATA, searchResultVO);
        startActivity(intent);
    }

    @Override
    public void showResult(SearchResultVO searchResultVO) {
        Intent intent = new Intent(getActivity(), SearchResultActivity.class);
        intent.putExtra(Content.EXTRA_DATA, searchResultVO);
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

    @OnClick(R.id.btn_realm_clear)
    public void onClearClick() {
        clearRealm();
    }

    @OnClick(R.id.ib_clear_histories)
    public void onClick() {
        mPresenter.clearHistories();
    }

    @OnClick(R.id.btn_realm_select)
    public void onViewClicked() {
        startActivityForResult(new Intent(getContext(), RealmSelectActivity.class), Content.REQUEST_SELECT_REALM);
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
