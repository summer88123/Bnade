package com.summer.bnade.result.single;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.summer.bnade.R;
import com.summer.bnade.base.BaseActivity;
import com.summer.bnade.base.di.ComponentHolder;
import com.summer.bnade.home.Provider;
import com.summer.bnade.utils.Content;
import com.summer.bnade.utils.ScreenUtil;
import com.summer.lib.model.entity.Item;
import com.summer.lib.model.entity.Realm;

import javax.inject.Inject;

import butterknife.BindView;
import icepick.Icicle;

public class ItemResultActivity extends BaseActivity implements Provider<ItemResultTransformer> {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @Inject
    ItemResultTransformer mPresenter;
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
        mToolbar.setSubtitle(realm.getConnected());
        Glide.with(this).load(item.getUrl()).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean
                    isFirstResource) {
                mToolbar.setLogo(R.mipmap.ic_launcher);
                return true;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                mToolbar.setLogo(resource);
                return true;
            }
        }).into(ScreenUtil.dp2px(48),ScreenUtil.dp2px(48));
    }

    @Override
    public ItemResultTransformer provide() {
        return mPresenter;
    }
}
