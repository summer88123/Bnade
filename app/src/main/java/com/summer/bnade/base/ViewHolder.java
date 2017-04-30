package com.summer.bnade.base;

import android.content.res.Resources;
import android.view.View;

/**
 * Created by kevin.bai on 2017/4/23.
 */
public abstract class ViewHolder<ITEM> {
    protected View itemView;
    protected Resources mResources;
    protected ITEM item;

    public ViewHolder(View view) {
        this.itemView = view;
        this.mResources = view.getResources();
    }

    public void bind(ITEM item) {
        this.item = item;
        this.onBind(this.item);
    }

    protected abstract void onBind(ITEM item);
}
