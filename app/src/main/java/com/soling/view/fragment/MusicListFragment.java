package com.soling.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import com.soling.R;
import com.soling.view.adapter.MusicAdapter;

public class MusicListFragment extends BaseFragment {

    private static final String TAG = "MusicListFragment";

    private RecyclerView rvMusicList;
    private TextView tvTitle;
    private ImageView ivIcon;
    private ImageButton ibClose;

    private String title;
    private int icon;
    private MusicAdapter musicAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        rvMusicList = (RecyclerView) findViewById(R.id.rv_music_list);
        ibClose = (ImageButton) findViewById(R.id.ib_close);
    }

    private void initData() {
        ivIcon.setImageResource(icon);
        tvTitle.setText(title);
        rvMusicList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMusicList.setAdapter(musicAdapter);
        rvMusicList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    private void initEvent() {
        final ViewGroup parent = (ViewGroup) Objects.requireNonNull(getView()).getParent();
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parent != null) {
                    parent.setVisibility(View.INVISIBLE);
                }
            }
        });
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

}
