package com.summer.bnade.select;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.summer.bnade.R;
import com.summer.bnade.base.BaseAdapter;
import com.summer.bnade.base.BaseViewHolder;
import com.summer.bnade.select.entity.TypedRealm;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

class RealmAdapter extends BaseAdapter<TypedRealm, RealmAdapter.ViewHolder> implements
        StickyRecyclerHeadersAdapter<RealmAdapter.HeaderViewHolder> {
    private RealmSelectContract.View view;

    RealmAdapter(RealmSelectContract.View view) {
        this.view = view;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_item_realm;
    }

    @Override
    protected ViewHolder createViewHolder(View v, int viewType) {
        return new ViewHolder(v);
    }

    @Override
    public long getHeaderId(int position) {
        return getItem(position).getType().charAt(0);
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new HeaderViewHolder(mInflater.inflate(R.layout.header_realm_select, parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder holder, int position) {
        holder.label.setText(getItem(position).getType());
    }


    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView label;

        HeaderViewHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView;
        }
    }

    public class ViewHolder extends BaseViewHolder<TypedRealm> {
        @BindView(R.id.content)
        TextView mContent;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onBind(TypedRealm realm) {
            mContent.setText(realm.getConnected());
        }

        @OnClick(R.id.layout)
        public void onClick() {
            view.selected(item.getRealm());
        }
    }
}
