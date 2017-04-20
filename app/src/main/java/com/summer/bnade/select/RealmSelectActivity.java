package com.summer.bnade.select;

import android.os.Bundle;

import com.summer.bnade.R;
import com.summer.lib.base.BaseActivity;
import com.summer.lib.model.di.ComponentHolder;

import javax.inject.Inject;

public class RealmSelectActivity extends BaseActivity {
    @Inject
    RealmSelectPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm_select);

        RealmSelectFragment fragment = (RealmSelectFragment) getSupportFragmentManager()
                .findFragmentByTag(RealmSelectFragment.TAG);
        if (fragment == null) {
            fragment = RealmSelectFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentFrame, fragment, RealmSelectFragment.TAG).commit();
        }
        DaggerRealmSelectComponent.builder().applicationComponent(ComponentHolder.getComponent())
                .realmSelectModule(new RealmSelectModule(fragment))
                .build().inject(this);
    }

    @Override
    protected void injectComponent() {

    }
}
