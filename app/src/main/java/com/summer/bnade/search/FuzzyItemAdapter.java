package com.summer.bnade.search;

import android.view.View;
import android.widget.TextView;

import com.summer.bnade.base.BaseAdapter;
import com.summer.bnade.base.BaseViewHolder;

/**
 * Created by kevin.bai on 2017/4/17.
 */

class FuzzyItemAdapter extends BaseAdapter<String, FuzzyItemAdapter.ViewHolder> {
    private SearchContract.Presenter mPresenter;

    public FuzzyItemAdapter(SearchContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    protected int layoutId() {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    protected ViewHolder createViewHolder(View v, int viewType) {
        return new ViewHolder(v);
    }

    class ViewHolder extends BaseViewHolder<String> {
        TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.fuzzySearch(mTextView.getText().toString());
                }
            });
        }

        @Override
        public void onBind(String s) {
            mTextView.setText(s);
        }
    }
}
