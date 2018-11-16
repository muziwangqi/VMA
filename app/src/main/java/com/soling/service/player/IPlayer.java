package com.soling.service.player;

import android.graphics.Bitmap;

import java.util.List;

import com.soling.model.LyricLine;
import com.soling.model.Music;
import com.soling.model.PlayList;

public interface IPlayer {

    void play();

    void play(int index);

    void play(Music music);

    void play(PlayList playList);

    void play(PlayList playList, int startIndex);

    void pause();

    void resume();

    void playNext();

    void playLast();

    void seekTo(int progress);

    boolean isPlaying();

    int getProgress();

    void release();

    Music getPlayingMusic();

    void changeModel();

    Player.Model getModel();

    void unregisterObserver(Observer observer);

    void registerObserver(Observer observer);

    interface Observer {

        void onPlayNext();

        void onPlayLast();

        void onPlayToggle();

        void onCoverLoad(Bitmap cover);

        void onLyricLoad(List<LyricLine> lyric);

    }

    enum Model {
        LOOP_ALL,
        SHUFFLE,
        LOOP_SINGLE
    }

}
