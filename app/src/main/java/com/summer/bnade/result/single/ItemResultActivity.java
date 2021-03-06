package com.summer.bnade.result.single;

import android.content.Context;
import android.content.Intent;
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
import com.summer.bnade.utils.Content;
import com.summer.bnade.utils.RxUtil;
import com.summer.bnade.utils.ScreenUtil;
import com.summer.lib.model.entity.Item;
import com.summer.lib.model.entity.Realm;

import javax.inject.Inject;

import butterknife.BindView;

public class ItemResultActivity extends BaseActivity{

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

    Item item;
    Realm realm;

    public static RxUtil.DataConsumer<Context, Realm> starter(Item item) {
        return new RxUtil.DataConsumer<Context, Realm>() {
            @Override
            public void accept(Context context) throws Exception {
                Intent intent = new Intent(context, ItemResultActivity.class);
                intent.putExtra(Content.EXTRA_DATA, item);
                intent.putExtra(Content.EXTRA_SUB_DATA, data);
                context.startActivity(intent);
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        item = getIntent().getParcelableExtra(Content.EXTRA_DATA);
        realm = getIntent().getParcelableExtra(Content.EXTRA_SUB_DATA);
        super.onCreate(savedInstanceState);
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
        mToolbar.setSubtitle(realm.getName());
        Glide.with(this).load(item.getUrl()).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean
                    isFirstResource) {
                mToolbar.setLogo(R.mipmap.ic_launcher);
                return true;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                           boolean isFromMemoryCache, boolean isFirstResource) {
                mToolbar.setNavigationIcon(resource);
                return true;
            }
        }).into(ScreenUtil.dp2px(148), ScreenUtil.dp2px(148));
    }

}
