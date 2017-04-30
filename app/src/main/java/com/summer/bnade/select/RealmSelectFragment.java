package com.summer.bnade.select;

import com.google.android.flexbox.FlexboxLayoutManager;

import android.app.Activity;
import android.content.Intent;
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
import com.summer.bnade.select.entity.RealmSelectVO;
import com.summer.bnade.utils.Content;
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
    @BindView(R.id.list_used)
    RecyclerView mListUsed;
    private HistoryAdapter mHistoriesAdapter;

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
        mAdapter = new RealmAdapter(this);
        mList.setAdapter(mAdapter);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager();
        mListUsed.setLayoutManager(layoutManager);
        mHistoriesAdapter = new HistoryAdapter(mPresenter);
        mListUsed.setAdapter(mHistoriesAdapter);
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
    public void show(RealmSelectVO vo) {
        show(vo.getRealms());
        mHistoriesAdapter.update(vo.getHistories());
    }

    @Override
    public void selected(Realm realm) {
        Intent intent = new Intent();
        intent.putExtra(Content.EXTRA_DATA, realm);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void show(List<Realm> realms) {
        if (realms.isEmpty()) {
            mTextInputLayout.setErrorEnabled(true);
            mTextInputLayout.setError(getString(R.string.realm_select_no_match));
        } else {
            mTextInputLayout.setErrorEnabled(false);
            mTextInputLayout.setError(null);
        }
        mAdapter.update(realms);
    }
}
