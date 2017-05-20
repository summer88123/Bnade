package com.summer.bnade.result.single;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.summer.bnade.R;
import com.summer.bnade.base.BaseViewActivity;
import com.summer.bnade.base.di.ComponentHolder;
import com.summer.bnade.home.Provider;
import com.summer.bnade.utils.Content;
import com.summer.lib.model.entity.Item;
import com.summer.lib.model.entity.Realm;

import javax.inject.Inject;

import butterknife.BindView;
import icepick.Icicle;

public class ItemResultActivity extends BaseViewActivity<ItemResultContract.Presenter>
        implements ItemResultContract.View , Provider<ItemResultContract.Presenter> {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @Inject
    ItemResultPageAdapter mItemResultPageAdapter;

    @Icicle
    Item item;
    @Icicle
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            item = getIntent().getParcelableExtra(Content.EXTRA_DATA);
            realm = getIntent().getParcelableExtra(Content.EXTRA_SUB_DATA);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void injectComponent() {
        ItemResultComponent component = DaggerItemResultComponent.builder()
                .appComponent(ComponentHolder.getComponent())
                .itemResultModule(new ItemResultModule(this, item, realm))
                .build();
        component.inject(this);
    }

    @Override
    public int layout() {
        return R.layout.activity_search_realm_item_result;
    }

    @Override
    public void setUpView() {
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(mItemResultPageAdapter);
        mToolbar.setTitle(item.getName());
    }

    @Override
    public ItemResultContract.Presenter provide() {
        return mPresenter;
    }
}
