package com.soling.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import com.soling.R;
import com.soling.model.Music;


public class MusicAdapter extends RecyclerView.Adapter {

    private List<Music> musics;
    private OnItemClickListener onItemClickListener;
    private boolean showDeleteBtn = false;

    public MusicAdapter(List<Music> musics) {
        this.musics = musics;
    }

    public void setShowDeleteBtn(boolean showDeleteBtn) {
        this.showDeleteBtn = showDeleteBtn;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_music, viewGroup, false);

        ViewHolder holder = new ViewHolder(view);
        if (showDeleteBtn) {
            holder.ibDelete.setVisibility(View.VISIBLE);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.tvName.setText(musics.get(position).getName());
        holder.tvAlbum.setText(musics.get(position).getAlbum());
        holder.tvArtist.setText(musics.get(position).getArtist());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicAdapter.this.onItemClickListener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return musics.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvArtist;
        TextView tvAlbum;
        ImageButton ibDelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvArtist = itemView.findViewById(R.id.tv_artist);
            tvAlbum = itemView.findViewById(R.id.tv_album);
            ibDelete = itemView.findViewById(R.id.ib_delete);
        }

    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setMusics(List<Music> musics) {
        this.musics = musics;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
