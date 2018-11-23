package com.soling.service.player;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.soling.api.MusicAPI;
import com.soling.api.MusicAPIFactory;
import com.soling.model.LyricLine;
import com.soling.model.Music;
import com.soling.model.PlayList;
import com.soling.utils.BitmapUtil;
import com.soling.utils.FileUtil;
import com.soling.utils.HttpUtil;
import com.soling.utils.LyricUtil;
import com.soling.utils.db.MusicHelper;

public class Player implements IPlayer {

    private static final String TAG = "Player";

    private MediaPlayer mediaPlayer;
    private PlayList playList;
    private Music playingMusic;
    private boolean playing;
    private Model model;
    private Bitmap cover;
    private List<LyricLine> lyric;

    private List<Observer> observers = new ArrayList<>();

    public Player() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNext();
            }
        });
        playing = false;
        playList = new PlayList();
        model = Model.LOOP_ALL;
    }

    @Override
    public void play() {
        if (playList.getPlayingMusic() == null) return;
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
        notifyPlayChange();
        loadResource();
    }

    @Override
    public void play(int index) {
        playList.setPlayingIndex(index);
        play();
    }

    @Override
    public void play(PlayList playList, int startIndex) {
        this.playList = playList;
        play(startIndex);
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
        playing = false;
        notifyPlayToggle();
    }

    @Override
    public void resume() {
        playing = true;
        mediaPlayer.start();
        notifyPlayToggle();
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
        lyric = null;
        cover = null;
        play();
        notifyPlayChange();
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
        lyric = null;
        cover = null;
        play();
        notifyPlayChange();
    }

    @Override
    public void seekTo(int progress) {
        mediaPlayer.seekTo(progress);
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
    }

    @Override
    public Music getPlayingMusic() {
        return playingMusic;
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
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {
        observers.remove(observer);
    }

    private Bitmap loadCoverFromApi(Music music) {
        String albumPath = music.getCoverPath();
        Bitmap cover = null;
        MusicAPI api = MusicAPIFactory.getMusicAPI();
        if (albumPath == null) {
            if (music.getAlbumId() == null) {
                List<Music> musics = api.search(music.getName(), music.getArtist(), music.getAlbum());
                if (musics != null && musics.size() > 0) {
                    music.setAId(musics.get(0).getAId());
                    music.setAlbumId(musics.get(0).getAlbumId());
                }
            }
            if (music.getAlbumId() != null) {
                albumPath = api.getCoverPath(music.getAlbumId());
                music.setCoverPath(albumPath);
            }
        }
        if (music.getCoverPath() != null) {
            cover = HttpUtil.requestBitmap(music.getCoverPath(), HttpUtil.METHOD_GET);
        }
        return cover;
    }

    private Bitmap loadCoverFromLocal(Music music) {
        Bitmap cover = null;
        String localAlbumPath = music.getLocalCoverPath();
        if (localAlbumPath != null) {
            cover = BitmapUtil.read(localAlbumPath);
        }
        return cover;
    }

    private String loadLyricFromApi(Music music) {
        MusicAPI api = MusicAPIFactory.getMusicAPI();
        if (music.getAId() == null) {
            List<Music> musics = api.search(music.getName(), music.getArtist(), music.getAlbum());
            if (musics != null && musics.size() > 0) {
                music.setAId(musics.get(0).getAId());
                music.setAlbumId(musics.get(0).getAlbumId());
            }
        }
        if (music.getAId() == null) return null;
        return api.getLyric(music.getAId());

    }

    private String loadLyricFromLocal(Music music) {
        return music.getLocalLyricPath() == null ? null : LyricUtil.read(music.getLocalLyricPath());
    }

    private void loadCover(Music music) {
        if (music == null) return;
        Bitmap cover = loadCoverFromLocal(music);
        if (cover == null) {
            cover = loadCoverFromApi(music);
        }
        if (cover != null) {
            notifyCoverLoaded(cover);
            if (music.getLocalCoverPath() == null || !FileUtil.isExist(music.getLocalCoverPath())) {
                music.setLocalCoverPath(BitmapUtil.save(cover));
                MusicHelper.update(music);
            }
            this.cover = cover;
        }
    }


    private void loadLyric(Music music) {
        String lyricStr = loadLyricFromLocal(music);
        if (lyricStr == null) {
            lyricStr = loadLyricFromApi(music);
        }
        if (lyricStr != null) {
            List<LyricLine> lyric = LyricUtil.resolve(lyricStr);
            music.setLyric(lyric);
            notifyLyricLoaded(music.getLyric());
            if (music.getLocalLyricPath() == null || !FileUtil.isExist(music.getLocalLyricPath())) {
                String lyricLocalPath = LyricUtil.save(lyricStr);
                music.setLocalLyricPath(lyricLocalPath);
                MusicHelper.update(music);
            }
            this.lyric = lyric;
        }
    }

    @Override
    public void loadLyric(final Observer observer) {
        observer.onLyricLoad(lyric);
    }

    @Override
    public void loadCover(final Observer observer) {
        observer.onCoverLoad(cover);
    }

    private void loadResource() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Music music = getPlayingMusic();
                loadCover(music);
                loadLyric(music);
            }
        }).start();
    }

    private void notifyCoverLoaded(Bitmap cover) {
        if (cover == null) return;
        for (Observer c : observers) {
            c.onCoverLoad(cover);
        }
    }

    private void notifyLyricLoaded(List<LyricLine> lyric) {
        for (Observer c : observers) {
            c.onLyricLoad(lyric);
        }
    }

    public void notifyPlayChange() {
        for (Observer c : observers) {
            c.onPlayChange();
        }
    }


    public void notifyPlayToggle() {
        for (Observer c : observers) {
            c.onPlayToggle();
        }
    }

}
