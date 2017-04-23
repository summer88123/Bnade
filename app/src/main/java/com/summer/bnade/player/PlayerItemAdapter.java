package com.summer.bnade.player;

import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.summer.bnade.R;
import com.summer.bnade.base.BaseAdapter;
import com.summer.bnade.base.BaseViewHolder;
import com.summer.lib.model.entity.Auction;
import com.summer.lib.model.entity.Gold;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerItemAdapter extends BaseAdapter<Auction, PlayerItemAdapter.ViewHolder> {
    private final PlayerItemFragment mFragment;
    private final Resources mResources;

    public PlayerItemAdapter(PlayerItemFragment fragment) {
        mFragment = fragment;
        mResources = fragment.getResources();
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_auction;
    }

    @Override
    protected ViewHolder createViewHolder(View v, int viewType) {
        return new ViewHolder(v);
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
            Glide.with(mFragment).load(auction.getItem().getUrl()).into(mIvIcon);
            mTvCount.setText(String.valueOf(auction.getCount()));
            mTvName.setText(auction.getName());
            mTvLastTime.setText(auction.getLastTime());
            mTvLabelBidPrice.setText(R.string.bid_price);
            Gold bidPrice = auction.getBidPrece();
            mTvBidPrice.setText(mResources
                    .getString(R.string.full_gold, bidPrice.getGold(), bidPrice.getSilver(), bidPrice.getCopper()));
            mTvLabelBuyout.setText(R.string.buyout);
            Gold buyout = auction.getBuyOut();
            mTvBuyout.setText(mResources
                    .getString(R.string.full_gold, buyout.getGold(), buyout.getSilver(), buyout.getCopper()));
        }

    }
}
