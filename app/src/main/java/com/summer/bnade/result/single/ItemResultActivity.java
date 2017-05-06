package com.summer.bnade.result.single;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.summer.bnade.R;
import com.summer.bnade.base.BaseViewActivity;
import com.summer.bnade.search.entity.SearchResultVO;
import com.summer.bnade.utils.Content;
import com.summer.bnade.di.ComponentHolder;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemResultActivity extends BaseViewActivity<ItemResultContract.Presenter>
        implements ItemResultContract.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @Inject
    PriceFragment mPriceFragment;
    @Inject
    HistoryFragment mHistoryFragment;
    @Inject
    PageAdapter mPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_realm_item_result);
        ButterKnife.bind(this);

        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(mPageAdapter);

        mPresenter.setData(getIntent().<SearchResultVO>getParcelableExtra(Content.EXTRA_DATA));
        mPresenter.load();
    }

    @Override
    protected void injectComponent() {
        ItemResultComponent component = DaggerItemResultComponent.builder()
                .appComponent(ComponentHolder.getComponent())
                .itemResultModule(new ItemResultModule(this))
                .build();
        component.inject(this);
        component.inject(mPriceFragment);
    }

    @Override
    public void show(SearchResultVO result) {
        mToolbar.setTitle(result.getItem().getName());
        mPriceFragment.updateList(result);
        mHistoryFragment.update(result);
    }
}
