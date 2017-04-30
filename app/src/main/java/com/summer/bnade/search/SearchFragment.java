package com.summer.bnade.search;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
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

    ListPopupWindow mFuzzyList;

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
                mPresenter.fuzzySearch(((TextView) view).getText().toString());
            }
        });

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
                return false;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.updateHistory();
    }

    @OnClick(R.id.btn_realm_select)
    public void onViewClicked() {
        startActivityForResult(new Intent(getContext(), RealmSelectActivity.class), Content.REQUEST_SELECT_REALM);
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
    public void show(SearchVO searchVO) {
        updateHotSearch(searchVO.getHotList());
        updateHistories(searchVO.getHistories());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void search() {
        if (mSearchView.isIconified()) {
            mSearchView.setIconified(false);
        } else {
            String query = mSearchView.getQuery().toString();
            if (!TextUtils.isEmpty(query)) {
                mPresenter.search(query, true);
            }
        }
    }
}
