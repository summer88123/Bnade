package com.summer.bnade.base;

import android.view.View;

/**
 * Created by kevin.bai on 2017/4/23.
 */
public abstract class ViewHolder<ITEM> {
    protected View itemView;
    protected ITEM item;

    public ViewHolder(View view) {
        this.itemView = view;
    }

    public void bind(ITEM item) {
        this.item = item;
        this.onBind(this.item);
    }

    protected abstract void onBind(ITEM item);
}
