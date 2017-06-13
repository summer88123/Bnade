package com.summer.bnade.realmrank;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.summer.bnade.R;
import com.summer.bnade.base.BaseAdapter;
import com.summer.bnade.base.BaseViewHolder;
import com.summer.bnade.utils.DateUtil;
import com.summer.lib.model.entity.AuctionRealm;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

class RealmRankAdapter extends BaseAdapter<AuctionRealm, RealmRankAdapter.ViewHolder> {
    @Inject
    RealmRankAdapter() {
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_item;
    }

    @Override
    protected ViewHolder createViewHolder(View v, int viewType) {
        return new ViewHolder(v);
    }

    class ViewHolder extends BaseViewHolder<AuctionRealm> {

        @BindView(R.id.tv_realm_name)
        TextView mTvRealmName;
        @BindView(R.id.tv_realm_type)
        TextView mTvRealmType;
        @BindView(R.id.tv_total_count)
        TextView mTvTotalCount;
        @BindView(R.id.tv_user_count)
        TextView mTvUserCount;
        @BindView(R.id.tv_item_kind)
        TextView mTvItemKind;
        @BindView(R.id.tv_update_time)
        TextView mTvUpdateTime;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onBind(AuctionRealm auctionRealm) {
            mTvRealmName.setText(auctionRealm.getRealm().getConnected());
            mTvRealmType.setText(auctionRealm.getType());
            mTvRealmType.setTextColor(Objects.equals(auctionRealm.getType(), AuctionRealm.PVP)
                    ? ContextCompat.getColor(itemView.getContext(), R.color.pvp_label)
                    : ContextCompat.getColor(itemView.getContext(), R.color.pve_label));
            mTvTotalCount.setText(String.valueOf(auctionRealm.getAuctionQuantity()));
            mTvUserCount.setText(String.valueOf(auctionRealm.getPlayerQuantity()));
            mTvItemKind.setText(String.valueOf(auctionRealm.getItemQuantity()));
            mTvUpdateTime.setText(DateUtil.format(auctionRealm.getLastModified(), "H:mm"));
        }

    }
}
