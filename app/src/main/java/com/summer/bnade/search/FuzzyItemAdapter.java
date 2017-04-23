package com.summer.bnade.search;

import android.view.View;
import android.widget.TextView;

import com.summer.bnade.base.Adapter;


/**
 * Created by kevin.bai on 2017/4/17.
 */

class FuzzyItemAdapter extends Adapter<String, FuzzyItemAdapter.ViewHolder> {

    @Override
    protected int layoutId() {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    protected ViewHolder createViewHolder(View v, int viewType) {
        return new ViewHolder(v);
    }

    class ViewHolder extends com.summer.bnade.base.ViewHolder<String> {
        TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
        }

        @Override
        protected void onBind(String item) {
            mTextView.setText(item);
        }
    }
}
