package com.summer.bnade.select;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.summer.bnade.R;
import com.summer.bnade.base.BaseAdapter;
import com.summer.bnade.base.BaseViewHolder;
import com.summer.bnade.utils.Content;
import com.summer.lib.model.entity.Realm;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

class RealmAdapter extends BaseAdapter<Realm, RealmAdapter.ViewHolder> {
    private FragmentActivity mActivity;

    RealmAdapter(FragmentActivity activity) {
        this.mActivity = activity;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_item_realm;
    }

    @Override
    protected ViewHolder createViewHolder(View v, int viewType) {
        return new ViewHolder(v);
    }

    public class ViewHolder extends BaseViewHolder<Realm> {
        @BindView(R.id.content)
        TextView mContent;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onBind(Realm realm) {
            mContent.setText(realm.getConnected());
        }

        @OnClick(R.id.layout)
        public void onClick() {
            Intent intent = new Intent();
            intent.putExtra(Content.EXTRA_DATA, item);
            mActivity.setResult(Activity.RESULT_OK, intent);
            mActivity.finish();
        }
    }
}
