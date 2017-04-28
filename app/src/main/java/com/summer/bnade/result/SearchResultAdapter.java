package com.summer.bnade.result;

import android.view.View;
import android.widget.TextView;

import com.summer.bnade.R;
import com.summer.bnade.base.BaseAdapter;
import com.summer.bnade.base.BaseViewHolder;
import com.summer.bnade.utils.DateUtil;
import com.summer.lib.model.entity.AuctionItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 */
class SearchResultAdapter extends BaseAdapter<AuctionItem, SearchResultAdapter.ViewHolder> {

    @Override
    protected int layoutId() {
        return R.layout.fragment_auctionitem;
    }

    @Override
    protected ViewHolder createViewHolder(View v, int viewType) {
        return new ViewHolder(v);
    }

    class ViewHolder extends BaseViewHolder<AuctionItem> {
        @BindView(R.id.tv_realm)
        TextView mTvRealm;
        @BindView(R.id.tv_min_buyout)
        TextView mTvMinBuyout;
        @BindView(R.id.tv_seller)
        TextView mTvSeller;
        @BindView(R.id.tv_total)
        TextView mTvTotal;
        @BindView(R.id.tv_update_time)
        TextView mTvUpdateTime;
        @BindView(R.id.tv_last_time)
        TextView mTvLastTime;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onBind(AuctionItem auctionItem) {
            mTvRealm.setText(auctionItem.getRealm().getConnected());
            mTvSeller.setText(auctionItem.getName());
            mTvLastTime.setText(auctionItem.getLastTime().getResult());
            mTvUpdateTime.setText(DateUtil.format(auctionItem.getLastUpdateTime(), "H:mm"));
            mTvTotal.setText(auctionItem.getTotal() + "");
            mTvMinBuyout.setText(mTvMinBuyout.getContext()
                    .getString(R.string.full_gold,
                            auctionItem.getMinBuyOut() / 10000,
                            auctionItem.getMinBuyOut() % 10000 / 100,
                            auctionItem.getMinBuyOut() % 100));
        }

    }
}
