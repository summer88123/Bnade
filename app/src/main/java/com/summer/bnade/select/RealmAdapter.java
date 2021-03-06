package com.summer.bnade.select;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.summer.bnade.R;
import com.summer.bnade.base.BaseAdapter;
import com.summer.bnade.base.BaseViewHolder;
import com.summer.bnade.select.entity.TypedRealm;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

class RealmAdapter extends BaseAdapter<TypedRealm, RealmAdapter.ViewHolder> implements
        StickyRecyclerHeadersAdapter<RealmAdapter.HeaderViewHolder> {
    private RealmSelectFragment view;
    private RealmSelectTransformer mPresenter;

    @Inject
    RealmAdapter(RealmSelectFragment view, RealmSelectTransformer presenter) {
        this.view = view;
        this.mPresenter = presenter;
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

    @Override
    public void remove(int position) {
        TypedRealm item = getItem(position);
        mPresenter.remove(item.getRealm());
        super.remove(position);
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
        @BindView(R.id.imageButton)
        ImageButton mImageButton;
        @BindView(R.id.tv_connected)
        TextView mTvConnected;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onBind(TypedRealm realm) {
            mContent.setText(realm.getName());
            mTvConnected.setText(realm.getConnected());
            if (Objects.equals(realm.getType(), TypedRealm.LABEL_USED)) {
                mImageButton.setVisibility(View.VISIBLE);
            } else {
                mImageButton.setVisibility(View.INVISIBLE);
            }
        }

        @OnClick(R.id.imageButton)
        public void onImageButtonClick() {
            remove(getAdapterPosition());
        }

        @OnClick(R.id.layout)
        public void onClick() {
            view.selected(item.getRealm());
        }
    }
}
