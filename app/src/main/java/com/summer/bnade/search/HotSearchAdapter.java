package com.summer.bnade.search;

import android.view.View;
import android.widget.TextView;

import com.summer.bnade.R;
import com.summer.bnade.base.BaseAdapter;
import com.summer.bnade.base.BaseViewHolder;
import com.summer.lib.model.entity.Hot;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kevin.bai on 2017/4/13.
 */

class HotSearchAdapter extends BaseAdapter<Hot, HotSearchAdapter.ViewHolder> {

    private final OnTabClickListener mOnTabClickListener;

    @Inject
    HotSearchAdapter(OnTabClickListener listener) {
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

    class ViewHolder extends BaseViewHolder<Hot> {
        @BindView(R.id.tv_search_item)
        TextView tvSearchItem;

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
            mOnTabClickListener.onClick(item.getName());
        }
    }
}
