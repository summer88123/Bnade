package com.summer.bnade.select;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.summer.bnade.R;
import com.summer.bnade.base.BaseFragment;
import com.summer.lib.model.entity.Realm;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class RealmSelectFragment extends BaseFragment<RealmSelectContract.Presenter> implements RealmSelectContract
        .View {
    public static final String TAG = RealmSelectFragment.class.getSimpleName();
    @BindView(R.id.list)
    RecyclerView mList;
    @BindView(R.id.et_search)
    TextInputEditText mEtSearch;
    @BindView(R.id.textInputLayout)
    TextInputLayout mTextInputLayout;
    Unbinder unbinder;

    RealmAdapter mAdapter;

    @SuppressWarnings("unused")
    public static RealmSelectFragment newInstance() {
        return new RealmSelectFragment();
    }

    @OnTextChanged(R.id.et_search)
    public void onTextChange(CharSequence s) {
        mPresenter.filter(s);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_realm_select, container, false);
        unbinder = ButterKnife.bind(this, view);
        mAdapter = new RealmAdapter(getActivity());
        mList.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.load();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void show(List<Realm> list) {
        if (list.isEmpty()) {
            mTextInputLayout.setError(getString(R.string.realm_select_no_match));
        } else {
            mTextInputLayout.setError(null);
        }
        mAdapter.update(list);
    }
}
