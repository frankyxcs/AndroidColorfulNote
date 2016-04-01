package com.product.colorfulnote.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.product.colorfulnote.R;
import com.product.colorfulnote.common.interfaces.OnRecyclerViewItemClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/4/1 0001.
 */
public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private ArrayList<String> mDataset;
    private OnRecyclerViewItemClickListener mItemClickListener;

    public NavigationAdapter(ArrayList<String> dataset) {
        this.mDataset = dataset;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.listitem_navigation, null);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(mDataset.get(position));
        holder.txtTitle.setText(mDataset.get(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_title)
        TextView txtTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setItemClickListener(OnRecyclerViewItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        if (null != mItemClickListener) {
            mItemClickListener.onItemClick(view, view.getTag());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (null != mItemClickListener) {
            mItemClickListener.onItemLongClick(view, view.getTag());
        }
        return true;
    }
}
