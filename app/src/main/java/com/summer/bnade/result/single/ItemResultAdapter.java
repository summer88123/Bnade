package com.summer.bnade.result.single;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.summer.bnade.R;
import com.summer.bnade.base.BaseAdapter;
import com.summer.bnade.base.BaseViewHolder;
import com.summer.lib.model.entity.AuctionRealmItem;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kevin.bai on 2017/4/28.
 */
class ItemResultAdapter extends BaseAdapter<AuctionRealmItem, ItemResultAdapter.ViewHolder> {

    @Inject
    ItemResultAdapter(){

    }

    @Override
    protected int layoutId() {
        return R.layout.item_auction_realm_item;
    }

    @Override
    protected ViewHolder createViewHolder(View v, int viewType) {
        return new ViewHolder(v);
    }

    class ViewHolder extends BaseViewHolder<AuctionRealmItem> {
        @BindView(R.id.tv_player_name)
        TextView mTvPlayerName;
        @BindView(R.id.tv_last_time)
        TextView mTvLastTime;
        @BindView(R.id.tv_count)
        TextView mTvCount;
        @BindView(R.id.tv_bid_price)
        TextView mTvBidPrice;
        @BindView(R.id.tv_buyout)
        TextView mTvBuyout;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onBind(AuctionRealmItem item) {
            Context context = mTvPlayerName.getContext();
            mTvPlayerName.setText(item.getPlayerName());
            mTvLastTime.setText(item.getLastTime().getResult());
            mTvCount.setText(String.valueOf(item.getCount()));
            mTvBidPrice.setText(item.getBidPrice().showSpan(context));
            mTvBuyout.setText(item.getBuyout().showSpan(context));
        }
    }
}
