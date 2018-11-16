package com.soling.service.player;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.soling.model.Music;
import com.soling.model.PlayList;

public class Player implements IPlayer, MediaPlayer.OnCompletionListener {

    private static final String TAG = "Player";

    private MediaPlayer mediaPlayer;
    private PlayList playList;
    private Music playingMusic;
    private boolean playing;
    private Model model;

    private static volatile Player INSTANCE;

    private Player() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        playing = false;
        playList = new PlayList();
        model = Model.LOOP_ALL;
    }

    public static Player getInstance() {
        if (INSTANCE == null) {
            synchronized (Player.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Player();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void play() {
        Music music = playList.getPlayingMusic();
        if (music != null) {
            File file = new File(music.getPath());
            if (file.exists()) {
                play(playList.getPlayingMusic());
            }
            else {
                playNext();
            }
        }
    }

    public void play(Music music) {
        try {
            playingMusic = music;
            mediaPlayer.reset();
            mediaPlayer.setDataSource(music.getPath());
            mediaPlayer.prepare();
            playing = true;
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void play(int index) {
        playList.setPlayingIndex(index);
        play();
    }

    @Override
    public void play(PlayList playList) {
        this.playList = playList;
        play();
    }

    @Override
    public void play(PlayList playList, int startIndex) {
        this.playList = playList;
        play(startIndex);
    }

    @Override
    public void pause() {
        Log.d(TAG, "pause: ");
        mediaPlayer.pause();
        playing = false;
    }

    @Override
    public void resume() {
        playing = true;
        mediaPlayer.start();
    }

    @Override
    public void playNext() {
        Log.d(TAG, "playNext: ");
        playing = true;
        switch (model) {
            case LOOP_ALL:
                playList.skipNext();
                break;
            case LOOP_SINGLE:
                break;
            case SHUFFLE:
                playList.skipRandom();
        }
        play();
    }

    @Override
    public void playLast() {
        playing = true;
        switch (model) {
            case LOOP_ALL:
                playList.skipLast();
                break;
            case LOOP_SINGLE:
                break;
            case SHUFFLE:
                playList.skipRandom();
        }
        play();
    }

    @Override
    public void seekTo(int progress) {
        mediaPlayer.seekTo(progress);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        playNext();
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying() & playing;
    }

    @Override
    public int getProgress() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void release() {
        Log.d(TAG, "release: ");
        mediaPlayer.release();
        playList = null;
        INSTANCE = null;
    }

    @Override
    public Music getPlayingMusic() {
        return playingMusic;
    }

    @Override
    public int getPlayingIndex() {
        return playList.getPlayingIndex();
    }

    @Override
    public void changeModel() {
        switch (model) {
            case SHUFFLE:
                model = Model.LOOP_SINGLE;
                break;
            case LOOP_SINGLE:
                model = Model.LOOP_ALL;
                break;
            case LOOP_ALL:
                model = Model.SHUFFLE;
                break;
        }
        Log.d(TAG, "changeModel: " + model);
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public List<Music> getMusicList() {
        return playList.getMusics();
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        mediaPlayer.setOnCompletionListener(onCompletionListener);
    }

}
