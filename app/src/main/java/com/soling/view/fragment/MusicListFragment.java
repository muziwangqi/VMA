package com.soling.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.soling.R;
import com.soling.model.Music;
import com.soling.view.adapter.MusicAdapter;

public class MusicListFragment extends Fragment {

    private static final String TAG = "MusicListFragment";

    private RecyclerView rvMusicList;
    private TextView tvTitle;
    private ImageView ivIcon;
    private ImageButton ibClose;

    private List<Music> musicList;
    private String title;
    private int icon;
    private MusicAdapter musicAdapter;

    public MusicListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);
        initView(view);
        initData();
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (container != null) {
                    container.setVisibility(View.INVISIBLE);
                }
            }
        });
        return view;
    }

    private void initView(View view) {
        ivIcon = view.findViewById(R.id.iv_icon);
        tvTitle = view.findViewById(R.id.tv_title);
        rvMusicList = view.findViewById(R.id.rv_music_list);
        ibClose = view.findViewById(R.id.ib_close);
    }

    private void initData() {
        ivIcon.setImageResource(icon);
        tvTitle.setText(title);
        Log.d(TAG, "initData: " + musicList);
        rvMusicList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMusicList.setAdapter(musicAdapter);
        rvMusicList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    public void setOnItemClickListener(MusicAdapter.OnItemClickListener listener) {
        musicAdapter.setOnItemClickListener(listener);
    }

    public void setOnItemDeleteClickListener(MusicAdapter.OnItemDeleteClickListener onItemDeleteClickListener) {
        musicAdapter.setOnItemDeleteClickListener(onItemDeleteClickListener);
    }

    public void setMusicList(List<Music> musicList) {
        this.musicList = musicList;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public static MusicListFragment build(String title, int icon, MusicAdapter adapter) {
        MusicListFragment fragment = new MusicListFragment();
        fragment.setIcon(icon);
        fragment.setTitle(title);
        fragment.musicAdapter = adapter;
        return fragment;
    }

    public void showDelete() {
        musicAdapter.setShowDeleteBtn(true);
    }

}
