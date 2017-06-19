package com.summer.bnade.search;

import android.view.View;
import android.widget.TextView;

import com.summer.bnade.R;
import com.summer.bnade.base.BaseAdapter;
import com.summer.bnade.base.BaseViewHolder;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kevin.bai on 2017/4/16.
 */

class HistoryAdapter extends BaseAdapter<String, HistoryAdapter.ViewHolder> {

    private final OnTabClickListener mOnTabClickListener;

    @Inject
    HistoryAdapter(OnTabClickListener listener) {
        this.mOnTabClickListener = listener;
    }

    @Override
    protected int layoutId() {
        return R.layout.item_hot_search;
    }

    @Override
    protected ViewHolder createViewHolder(View v, int viewType) {
        return new ViewHolder(v);
    }

    class ViewHolder extends BaseViewHolder<String> {
        @BindView(R.id.tv_search_item)
        TextView tvSearchItem;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(String s) {
            tvSearchItem.setText(s);
        }

        @OnClick(R.id.card_search_item)
        void onClick() {
            mOnTabClickListener.onClick(item);
        }
    }
}
