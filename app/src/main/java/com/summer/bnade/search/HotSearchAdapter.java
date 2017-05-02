package com.summer.bnade.search;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.summer.bnade.R;
import com.summer.bnade.base.BaseAdapter;
import com.summer.bnade.base.BaseViewHolder;
import com.summer.lib.model.entity.Hot;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kevin.bai on 2017/4/13.
 */

class HotSearchAdapter extends BaseAdapter<Hot, HotSearchAdapter.ViewHolder> {

    private final SearchContract.View mView;

    HotSearchAdapter(SearchContract.View view) {
        this.mView = view;
    }

    @Override
    protected int layoutId() {
        return R.layout.item_hot_search;
    }

    @Override
    protected ViewHolder createViewHolder(View v, int viewType) {
        return new ViewHolder(v);
    }

    class ViewHolder extends BaseViewHolder<Hot> {
        @BindView(R.id.tv_search_item)
        TextView tvSearchItem;
        @BindView(R.id.card_search_item)
        CardView cardSearchItem;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(Hot hot) {
            tvSearchItem.setText(item.getName());
        }


        @OnClick(R.id.card_search_item)
        void onClick() {
            mView.search(item.getName());
        }
    }
}
