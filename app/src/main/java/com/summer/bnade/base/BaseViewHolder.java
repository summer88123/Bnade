package com.summer.bnade.base;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by kevin.bai on 2017/4/14.
 */

public abstract class BaseViewHolder<Item> extends RecyclerView.ViewHolder {
    protected Item item;
    protected Resources mResources;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.mResources = itemView.getResources();
    }

    void bind(Item item) {
        this.item = item;
        onBind(this.item);
    }

    public abstract void onBind(Item item);
}
