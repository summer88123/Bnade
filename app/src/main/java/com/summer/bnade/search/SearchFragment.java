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
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
    ListPopupWindow mFuzzyList;
    @BindView(R.id.content)
    ConstraintLayout mContent;
    @BindView(R.id.select_btn)
    RealmSelectButton mSelectBtn;

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
            mSelectBtn.setRealm(data.<Realm>getParcelableExtra(Content.EXTRA_DATA));
        }
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
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            Realm realm = savedInstanceState.getParcelable("REALM");
            if (realm != null) {
                mSelectBtn.setRealm(realm);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.updateHistory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("REALM", mSelectBtn.getRealm());
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
        intent.putExtra(Content.EXTRA_SUB_DATA, realm);
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

    @OnClick(R.id.ib_clear_histories)
    public void onClick() {
        mPresenter.clearHistories();
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
