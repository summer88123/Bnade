package com.summer.bnade.select;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.summer.bnade.R;
import com.summer.bnade.base.BaseFragment;
import com.summer.bnade.select.entity.TypedRealm;
import com.summer.bnade.utils.Content;
import com.summer.lib.model.entity.Realm;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class RealmSelectFragment extends BaseFragment<RealmSelectUIModel> {
    public static final String TAG = RealmSelectFragment.class.getSimpleName();
    @BindView(R.id.list)
    RecyclerView mList;
    @BindView(R.id.et_search)
    TextInputEditText mEtSearch;
    @BindView(R.id.textInputLayout)
    TextInputLayout mTextInputLayout;
    @Inject
    RealmAdapter mAdapter;
    @Inject
    RealmSelectTransformer mPresenter;

    @SuppressWarnings("unused")
    public static RealmSelectFragment newInstance() {
        return new RealmSelectFragment();
    }

    @Override
    public int layout() {
        return R.layout.fragment_realm_select;
    }

    @Override
    public void setUpView() {
        mAdapter = new RealmAdapter(this, mPresenter);
        mList.setAdapter(mAdapter);
        mList.addItemDecoration(new StickyRecyclerHeadersDecoration(mAdapter));
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, mAdapter.getItem(viewHolder.getAdapterPosition()).getType()
                        .equals(TypedRealm.LABEL_USED) ? ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT : 0);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView
                    .ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                mAdapter.remove(position);
            }

        };
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mList);
    }

    @Override
    public void setUpObservable() {
        untilEmit(FragmentEvent.START)
                .compose(mPresenter.load())
                .compose(bindToLifecycle())
                .subscribe(showAs());

        RxTextView.textChanges(mEtSearch)
                .compose(mPresenter.filter())
                .compose(bindToLifecycle())
                .subscribe(showAs());
    }

    @Override
    protected void onSuccess(RealmSelectUIModel model) {
        show(model.getList());
    }

    public void selected(Realm realm) {
        Intent intent = new Intent();
        intent.putExtra(Content.EXTRA_DATA, realm);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    public void show(List<TypedRealm> realms) {
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
