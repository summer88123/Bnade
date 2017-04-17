package com.summer.bnade.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by kevin.bai on 2017/4/14.
 */

public abstract class BaseViewHolder<Item> extends RecyclerView.ViewHolder {
    protected Item item;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    void bind(Item item) {
        this.item = item;
        onBind(this.item);
    }

    public abstract void onBind(Item item);
}
