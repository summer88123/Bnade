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
import com.summer.lib.model.entity.Realm;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kevin.bai on 2017/5/10.
 */

public class RealmSelectButton extends ConstraintLayout {
    @BindView(R.id.btn_realm_select)
    Button mBtnRealmSelect;
    @BindView(R.id.btn_realm_clear)
    Button mBtnRealmClear;

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
        mBtnRealmSelect.setText(R.string.btn_realm_select);
        mBtnRealmSelect.postInvalidate();
        TransitionManager.beginDelayedTransition(this);
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
        mBtnRealmSelect.postInvalidate();
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
        hasRealmSet.clone(this);
        noRealmSet.clone(this);
    }
}
