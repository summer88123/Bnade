package com.summer.bnade.result.all;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.summer.bnade.R;
import com.summer.bnade.base.BaseAdapter;
import com.summer.bnade.base.BaseViewHolder;
import com.summer.bnade.result.single.ItemResultActivity;
import com.summer.bnade.utils.DateUtil;
import com.summer.bnade.utils.RxUtil;
import com.summer.lib.model.entity.AuctionItem;
import com.summer.lib.model.entity.Gold;
import com.summer.lib.model.entity.Item;
import com.summer.lib.model.entity.Realm;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 */
class SearchResultAdapter extends BaseAdapter<AuctionItem, SearchResultAdapter.ViewHolder> {
    private final Item item;

    SearchResultAdapter(Item item) {
        this.item = item;
    }

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
        @BindView(R.id.card_view)
        CardView mCardView;

        RxUtil.DataConsumer<Context, Realm> consumer;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            consumer = ItemResultActivity.starter(SearchResultAdapter.this.item);
            RxView.clicks(mCardView)
                    .map(o -> itemView.getContext())
                    .subscribe(consumer);
        }

        @Override
        public void onBind(AuctionItem auctionItem) {
            consumer.setData(auctionItem.getRealm());
            mTvRealm.setText(auctionItem.getRealm().getName());
            mTvSeller.setText(auctionItem.getName());
            mTvLastTime.setText(auctionItem.getLastTime().getResult());
            mTvUpdateTime.setText(DateUtil.format(auctionItem.getLastUpdateTime(), "H:mm"));
            mTvTotal.setText(auctionItem.getTotal() + "");
            Gold minBuyout = auctionItem.getMinBuyOut();
            mTvMinBuyout.setText(mTvMinBuyout.getContext()
                    .getString(R.string.full_gold, minBuyout.getGold(), minBuyout.getSilver(), minBuyout.getCopper()));
        }

    }
}
