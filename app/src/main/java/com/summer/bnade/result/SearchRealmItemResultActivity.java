package com.summer.bnade.result;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.summer.bnade.R;
import com.summer.bnade.search.entity.SearchResultVO;
import com.summer.bnade.utils.Content;
import com.summer.lib.base.BaseActivity;
import com.summer.lib.model.di.ComponentHolder;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchRealmItemResultActivity extends BaseActivity implements SearchResultContract.View {


    @BindView(R.id.list_view)
    RecyclerView mListView;
    @Inject
    SearchRealmItemResultPresenter mPresenter;
    @Inject
    SearchRealmItemResultAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_realm_item_result);
        ButterKnife.bind(this);

        mListView.setAdapter(mAdapter);
        mPresenter.setData((SearchResultVO) getIntent().getParcelableExtra(Content.EXTRA_DATA));
        mPresenter.load();
    }

    @Override
    protected void injectComponent() {
        DaggerSearchResultComponent.builder()
                .applicationComponent(ComponentHolder.getComponent())
                .searchResultModule(new SearchResultModule(this))
                .build().inject(this);
    }

    @Override
    public void setPresenter(SearchResultContract.Presenter presenter) {
    }

    @Override
    public void show(SearchResultVO result) {
        mAdapter.update(result.getAuctionRealmItems());
    }
}
