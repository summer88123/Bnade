package com.summer.bnade.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;

import com.summer.bnade.R;
import com.summer.bnade.data.HistoryRealmRepo;
import com.summer.bnade.base.di.ComponentHolder;
import com.summer.lib.model.entity.Realm;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by kevin.bai on 2017/5/10.
 */

public class RealmSelectButton extends ConstraintLayout {
    @BindView(R.id.btn_realm_select)
    Button mBtnRealmSelect;
    @BindView(R.id.btn_realm_clear)
    Button mBtnRealmClear;

    @Inject
    HistoryRealmRepo mRealmRepo;

    ConstraintSet hasRealmSet = new ConstraintSet();
    ConstraintSet noRealmSet = new ConstraintSet();

    private Realm realm;

    @OnClick(R.id.btn_realm_clear)
    public void onClearClick() {
        clearRealm();
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mBtnRealmSelect.setOnClickListener(l);
    }

    private void clearRealm() {
        this.realm = null;
        TransitionManager.beginDelayedTransition(this);
        mBtnRealmSelect.setText(R.string.btn_realm_select);
        mBtnRealmSelect.postInvalidate();
        noRealmSet.applyTo(this);
    }

    public RealmSelectButton(Context context) {
        super(context);
        init();
    }

    public RealmSelectButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RealmSelectButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
        mBtnRealmSelect.setText(realm.getConnected());
        TransitionManager.beginDelayedTransition(this);
        hasRealmSet.setVisibility(R.id.btn_realm_clear, ConstraintSet.VISIBLE);
        hasRealmSet.connect(R.id.btn_realm_select, ConstraintSet.RIGHT, R.id.btn_realm_clear, ConstraintSet.LEFT);
        hasRealmSet.connect(R.id.btn_realm_clear, ConstraintSet.LEFT, R.id.btn_realm_select, ConstraintSet.RIGHT);
        hasRealmSet.applyTo(this);
    }

    public Realm getRealm() {
        return realm;
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.realm_select_button, this);
        ButterKnife.bind(this);
        ComponentHolder.getComponent().inject(this);
        hasRealmSet.clone(this);
        noRealmSet.clone(this);
        mRealmRepo.last().subscribe(new Consumer<Realm>() {
            @Override
            public void accept(@NonNull Realm realm) throws Exception {
                setRealm(realm);
            }
        });
    }
}
