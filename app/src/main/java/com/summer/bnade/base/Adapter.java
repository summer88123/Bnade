package com.summer.bnade.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin.bai on 2017/4/23.
 */

public abstract class Adapter<ITEM, VH extends ViewHolder> extends BaseAdapter {

    private final List<ITEM> data;
    private LayoutInflater mInflater;

    protected Adapter() {
        data = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ITEM getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH holder;
        if (convertView == null) {
            if (mInflater == null) {
                mInflater = LayoutInflater.from(parent.getContext());
            }
            convertView = mInflater.inflate(layoutId(), parent, false);
            holder = createViewHolder(convertView, getItemViewType(position));
            convertView.setTag(holder);
        } else {
            holder = (VH) convertView.getTag();
        }
        holder.bind(getItem(position));
        return convertView;
    }

    public void update(List<ITEM> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    protected abstract VH createViewHolder(View view, int viewType);

    protected abstract int layoutId();

}
