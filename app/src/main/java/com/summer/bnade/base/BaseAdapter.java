package com.summer.bnade.base;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin.bai on 2017/4/14.
 */

public abstract class BaseAdapter<I, H extends BaseViewHolder<I>> extends RecyclerView.Adapter<H> {
    private List<I> data;
    protected LayoutInflater mInflater;
    private BaseDiffCallback mCallback;

    public BaseAdapter() {
        this.data = new ArrayList<>();
    }

    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
        View view = mInflater.inflate(layoutId(), parent, false);
        return createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(H holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public I getItem(int position) {
        return data.get(position);
    }

    public void remove(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    protected abstract int layoutId();

    protected abstract H createViewHolder(View v, int viewType);

    private BaseDiffCallback createDiffCallBack() {
        return new BaseDiffCallback();
    }

    private BaseDiffCallback getCallback() {
        if (mCallback == null) {
            mCallback = createDiffCallBack();
        }
        return mCallback;
    }

    public void update(List<I> newData) {
        BaseDiffCallback callback = getCallback();
        if (callback == null) {
            data.clear();
            data.addAll(newData);
            notifyDataSetChanged();
        } else {
            callback.update(newData, data);
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
            data.clear();
            data.addAll(newData);
            result.dispatchUpdatesTo(this);
        }
    }

    private class BaseDiffCallback extends DiffUtil.Callback {
        List<I> oldData;
        List<I> newData;

        @Override
        public int getOldListSize() {
            return this.oldData == null ? 0 : this.oldData.size();
        }

        @Override
        public int getNewListSize() {
            return this.newData == null ? 0 : this.newData.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return newData.get(newItemPosition).equals(oldData.get(oldItemPosition));
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return true;
        }

        void update(List<I> newData, List<I> oldData) {
            this.newData = newData;
            this.oldData = oldData;
        }
    }
}
