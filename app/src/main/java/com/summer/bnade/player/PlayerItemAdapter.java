package com.summer.bnade.player;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.summer.bnade.R;
import com.summer.bnade.base.BaseAdapter;
import com.summer.bnade.base.BaseViewHolder;
import com.summer.lib.model.entity.Auction;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

class PlayerItemAdapter extends BaseAdapter<Auction, PlayerItemAdapter.ViewHolder> {
    @Inject
    PlayerItemAdapter() {
    }

    @Override
    protected ViewHolder createViewHolder(View v, int viewType) {
        return new ViewHolder(v);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_auction;
    }

    public class ViewHolder extends BaseViewHolder<Auction> {
        @BindView(R.id.iv_icon)
        ImageView mIvIcon;
        @BindView(R.id.tv_count)
        TextView mTvCount;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_last_time)
        TextView mTvLastTime;
        @BindView(R.id.tv_label_bid_price)
        TextView mTvLabelBidPrice;
        @BindView(R.id.tv_bid_price)
        TextView mTvBidPrice;
        @BindView(R.id.tv_label_buyout)
        TextView mTvLabelBuyout;
        @BindView(R.id.tv_buyout)
        TextView mTvBuyout;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onBind(Auction auction) {
            Context context = mTvCount.getContext();
            mTvCount.setText(String.valueOf(auction.getCount()));
            mTvName.setText(auction.getName());
            mTvLastTime.setText(auction.getLastTime().getResult());
            mTvLabelBidPrice.setText(R.string.bid_price);
            mTvBidPrice.setText(auction.getBidPrece().showSpan(context));
            mTvLabelBuyout.setText(R.string.buyout);
            mTvBuyout.setText(auction.getBuyOut().showSpan(context));
            Glide.with(itemView.getContext()).load(auction.getItem().getUrl()).into(mIvIcon);
        }
    }
}
