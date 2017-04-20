package com.summer.bnade.search;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.summer.bnade.R;
import com.summer.bnade.base.BaseFragment;
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
    @BindView(R.id.fuzzy_list)
    RecyclerView mFuzzyList;
    @Inject
    HotSearchAdapter mHotAdapter;
    @Inject
    HistoryAdapter mHistoriesAdapter;
    @Inject
    FuzzyItemAdapter mFuzzyAdapter;
    @BindView(R.id.btn_realm_select)
    Button mBtnRealmSelect;

    @OnClick(R.id.ib_clear_histories)
    public void onClick() {
        mPresenter.clearHistories();
    }


    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, view);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager();
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);

        mRvHot.setLayoutManager(layoutManager);
        mRvHot.setAdapter(mHotAdapter);

        layoutManager = new FlexboxLayoutManager();
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        mListHistories.setLayoutManager(layoutManager);
        mListHistories.setAdapter(mHistoriesAdapter);

        mFuzzyList.setAdapter(mFuzzyAdapter);

        mRgHotType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rb_week:
                        mPresenter.updateHotSearchType(Hot.WEEK);
                        break;
                    case R.id.rb_day:
                        mPresenter.updateHotSearchType(Hot.DAY);
                        break;
                    case R.id.rb_month:
                    default:
                        mPresenter.updateHotSearchType(Hot.MONTH);
                        break;
                }
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mPresenter.search(s, true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                hideFuzzyList(null);
                return false;
            }
        });

        return view;
    }

    @OnClick(R.id.btn_realm_select)
    public void onViewClicked() {
        startActivityForResult(new Intent(getContext(), RealmSelectActivity.class), Content.REQUEST_SELECT_REALM);
    }

    private boolean isFuzzyListVisible() {
        return mFuzzyList.getVisibility() == View.VISIBLE;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.load();
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
    public void hideFuzzyList(String text) {
        if (isFuzzyListVisible()) {
            mFuzzyList.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(text)) {
            mSearchView.setQuery(text, false);
        }
    }

    @Override
    public void show(SearchVO searchVO) {
        updateHotSearch(searchVO.getHotList());
        updateHistories(searchVO.getHistories());
    }

    @Override
    public void showFuzzySearch(SearchResultVO searchResultVO) {
        mFuzzyAdapter.update(searchResultVO.getNames());
        mFuzzyList.setVisibility(View.VISIBLE);
    }

    @Override
    public void showResult(SearchResultVO searchResultVO) {
        Intent intent = new Intent(getActivity(), SearchResultActivity.class);
        intent.putExtra("data", searchResultVO);
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
