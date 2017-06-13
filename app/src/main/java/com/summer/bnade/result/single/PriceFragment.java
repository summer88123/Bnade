package com.summer.bnade.result.single;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;

import com.summer.bnade.R;
import com.summer.bnade.base.BaseFragment;
import com.summer.bnade.utils.Content;
import com.summer.lib.model.entity.Item;
import com.summer.lib.model.entity.Realm;
import com.trello.rxlifecycle2.android.FragmentEvent;

import javax.inject.Inject;

import butterknife.BindView;

public class PriceFragment extends BaseFragment<ItemResultUIModel> {
    @BindView(R.id.list_view)
    RecyclerView mListView;
    @Inject
    ItemResultAdapter mAdapter;
    @Inject
    ItemResultTransformer mPresenter;

    Item item;
    Realm realm;

    public static PriceFragment getInstance(Item item, Realm realm) {
        PriceFragment fragment = new PriceFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Content.EXTRA_DATA, item);
        bundle.putParcelable(Content.EXTRA_SUB_DATA, realm);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int layout() {
        return R.layout.fragment_price;
    }

    @Override
    public void setUpView() {
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void setUpObservable() {
        untilEmit(FragmentEvent.START)
                .map(ignore -> new Pair<>(item, realm))
                .compose(mPresenter.price())
                .compose(bindToLifecycle())
                .subscribe(showAs());
    }

    @Override
    protected void onSuccess(ItemResultUIModel model) {
        mAdapter.update(model.getList());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            item = getArguments().getParcelable(Content.EXTRA_DATA);
            realm = getArguments().getParcelable(Content.EXTRA_SUB_DATA);
        }
    }
}
