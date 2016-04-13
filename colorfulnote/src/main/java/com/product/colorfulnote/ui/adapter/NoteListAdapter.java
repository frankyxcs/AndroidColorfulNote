package com.product.colorfulnote.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.product.colorfulnote.R;
import com.product.colorfulnote.common.interfaces.OnRecyclerViewItemClickListener;
import com.product.colorfulnote.db.gen.Note;
import com.product.colorfulnote.ui.helper.ThemeHelper;
import com.product.common.utils.TimeUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/4/1 0001.
 */
public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {
    private ArrayList<Note> mDataset;
    private OnRecyclerViewItemClickListener mItemClickListener;

    public NoteListAdapter(ArrayList<Note> dataset) {
        this.mDataset = dataset;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.listitem_note, null);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(mDataset.get(position));
        holder.lyCard.setBackgroundResource(ThemeHelper.getInstance().getGroupBgColor());
        holder.ivTitle.setBackgroundResource(ThemeHelper.getInstance().getGroupIconBg());
        holder.txtTitleWeek.setText(ThemeHelper.getInstance().getWeekly(mDataset.get(position).getDate()));
        holder.txtTitleDate.setText(TimeUtils.getTime(mDataset.get(position).getDate().getTime()));
        holder.txtTitleContent.setText(mDataset.get(position).getContent());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_title)
        ImageView ivTitle;
        @Bind(R.id.txt_title_week)
        TextView txtTitleWeek;
        @Bind(R.id.txt_title_date)
        TextView txtTitleDate;
        @Bind(R.id.txt_title_content)
        TextView txtTitleContent;
        @Bind(R.id.ly_card)
        LinearLayout lyCard;

        ViewHolder(View itemView) {
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
