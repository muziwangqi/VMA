package com.soling.model;

import java.util.ArrayList;
import java.util.List;

public class PlayList {

    private static final String TAG = "PlayList";

    private List<Music> musics;
    private int playingIndex;

    public PlayList() {
        this(new ArrayList<Music>());
    }

    public PlayList(List<Music> musics) {
        this.musics = musics;
        playingIndex = musics.size() > 0 ? 0 : -1;
    }

    public boolean setPlayingIndex(int playingIndex) {
        if (musics == null || playingIndex < 0 || playingIndex > musics.size()) {
            return false;
        }
        this.playingIndex = playingIndex;
        return true;
    }

    public void skipNext() {
        if (musics == null || musics.size() == 0) return;
        playingIndex = (playingIndex + 1) % musics.size();
    }

    public void skipLast() {
        if (musics == null || musics.size() == 0) return;
        playingIndex = (playingIndex + musics.size() - 1) % musics.size();
    }

    public void skipRandom() {
        playingIndex =  (int) (Math.random() * musics.size());
    }

    public Music getPlayingMusic() {
        if (musics == null || musics.size() == 0 || playingIndex < 0 || playingIndex > musics.size()) {
            return null;
        }
        return musics.get(playingIndex);
    }

    public List<Music> getMusics() {
        return musics;
    }
}
