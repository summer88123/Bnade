package com.summer.bnade.result.single;

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

public class ItemResultActivity extends BaseActivity implements ItemResultContract.View {


    @BindView(R.id.list_view)
    RecyclerView mListView;
    @Inject
    ItemResultPresenter mPresenter;
    @Inject
    ItemResultAdapter mAdapter;

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
        DaggerItemResultComponent.builder()
                .applicationComponent(ComponentHolder.getComponent())
                .itemResultModule(new ItemResultModule(this))
                .build().inject(this);
    }

    @Override
    public void setPresenter(ItemResultContract.Presenter presenter) {
    }

    @Override
    public void show(SearchResultVO result) {
        mAdapter.update(result.getAuctionRealmItems());
    }
}
