package com.summer.bnade.result.single;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;

import com.summer.bnade.R;
import com.summer.bnade.base.BaseFragment;
import com.summer.bnade.home.Provider;
import com.summer.bnade.utils.Content;
import com.summer.bnade.utils.RxUtil;
import com.summer.lib.model.entity.AuctionRealmItem;
import com.summer.lib.model.entity.Item;
import com.summer.lib.model.entity.Realm;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PriceFragment extends BaseFragment {
    @BindView(R.id.list_view)
    RecyclerView mListView;
    ItemResultAdapter mAdapter;

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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Provider) {
            mPresenter = (ItemResultTransformer) ((Provider) context).provide();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            item = getArguments().getParcelable(Content.EXTRA_DATA);
            realm = getArguments().getParcelable(Content.EXTRA_SUB_DATA);
        }
        mAdapter = new ItemResultAdapter();
    }

    @Override
    public void onStart() {
        super.onStart();
        Single.just(new Pair<>(item, realm))
                .compose(mPresenter.price())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<AuctionRealmItem>>() {
                    @Override
                    public void accept(List<AuctionRealmItem> list) throws Exception {
                        mAdapter.update(list);
                    }
                }, new RxUtil.ContextErrorHandler(getContext()));
    }

    @Override
    public int layout() {
        return R.layout.fragment_price;
    }

    @Override
    public void setUpView() {
        mListView.setAdapter(mAdapter);
    }
}
