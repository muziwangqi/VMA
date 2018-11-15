package com.soling.view.adapter;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.soling.R;

import java.util.List;

public class SearchHistoryAdapter extends RecyclerView.Adapter {

    private List<String> searchHistory;
    private OnItemClickListener onItemClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public SearchHistoryAdapter(List<String> searchHistory) {
        this.searchHistory = searchHistory;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_history, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        holder.tvRecord.setText(searchHistory.get(i));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, i);
            }
        });
        holder.ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClickListener.onDeleteClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchHistory.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvRecord;
        ImageButton ibDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecord = itemView.findViewById(R.id.tv_record);
            ibDelete = itemView.findViewById(R.id.ib_delete);
        }

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View item, int position);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

}
