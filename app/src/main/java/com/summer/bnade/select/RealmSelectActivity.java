package com.summer.bnade.select;

import android.os.Bundle;
import android.view.Window;
import android.widget.FrameLayout;

import com.summer.bnade.R;
import com.summer.bnade.data.RepoModule;
import com.summer.lib.base.BaseActivity;
import com.summer.lib.model.di.ComponentHolder;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RealmSelectActivity extends BaseActivity {
    @Inject
    RealmSelectPresenter mPresenter;
    @BindView(R.id.contentFrame)
    FrameLayout mContentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setFinishOnTouchOutside(true);
        setContentView(R.layout.activity_realm_select);
        ButterKnife.bind(this);
        RealmSelectFragment fragment = (RealmSelectFragment) getSupportFragmentManager()
                .findFragmentByTag(RealmSelectFragment.TAG);
        if (fragment == null) {
            fragment = RealmSelectFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentFrame, fragment, RealmSelectFragment.TAG).commit();
        }
        DaggerRealmSelectComponent.builder().applicationComponent(ComponentHolder.getComponent())
                .realmSelectModule(new RealmSelectModule(fragment))
                .repoModule(new RepoModule())
                .build().inject(this);
    }

    @Override
    protected void injectComponent() {

    }
}
