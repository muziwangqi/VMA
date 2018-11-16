package com.soling.service.player;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.soling.api.MusicAPI;
import com.soling.api.MusicAPIFactory;
import com.soling.model.LyricLine;
import com.soling.model.Music;
import com.soling.model.PlayList;
import com.soling.utils.HttpUtil;

public class PlayerService extends Service implements IPlayer {

    public static final String ACTION_PLAY_NEXT = "cn.hyw.www.imusic.action.PLAY_NEXT";
    public static final String ACTION_PLAY_LAST = "cn.hyw.www.imusic.action.PLAY_LAST";
    public static final String ACTION_PLAY_TOGGLE = "cn.hyw.www.imusic.action.PLAY_TOGGLE";

    private static final String TAG = "PlayerService";
    private final LocalBinder binder = new LocalBinder();

    private Player player;
    private List<Observer> observers = new ArrayList<>();

    public class LocalBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = Player.getInstance();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNext();
            }
        });
        new PlayerNotification(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (TextUtils.equals(action, ACTION_PLAY_LAST)) {
                playNext();
            }
            else if (TextUtils.equals(action, ACTION_PLAY_TOGGLE)) {
                if (isPlaying()) pause();
                else play();
            }
            else if (TextUtils.equals(action, ACTION_PLAY_NEXT)) {
                playNext();
            }
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
    }

    @Override
    public void play() {
        player.play();
        notifyPlayStateChanged();
        loadNetworkResource();
    }

    public void play(Music music) {
        player.play(music);
        notifyPlayStateChanged();
        loadNetworkResource();
    }

    @Override
    public void play(int index) {
        player.play(index);
        notifyPlayStateChanged();
        loadNetworkResource();
    }

    @Override
    public void play(PlayList playList) {
        player.play(playList);
        notifyPlayStateChanged();
        loadNetworkResource();
    }

    @Override
    public void play(PlayList playList, int startIndex) {
        player.play(playList, startIndex);
        notifyPlayStateChanged();
        loadNetworkResource();
    }

    @Override
    public void pause() {
        player.pause();
        notifyPlayStateChanged();
    }

    @Override
    public void resume() {
        player.resume();
        notifyPlayStateChanged();
    }

    @Override
    public void playNext() {
        player.playNext();
        notifySkipNext();
        loadNetworkResource();
    }

    @Override
    public void playLast() {
       player.playLast();
       notifySkipLast();
       loadNetworkResource();
    }

    @Override
    public void seekTo(int progress) {
        player.seekTo(progress);
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public int getProgress() {
        return player.getProgress();
    }

    @Override
    public void release() {
        player.release();
    }

    @Override
    public Music getPlayingMusic() {
        return player.getPlayingMusic();
    }

    @Override
    public int getPlayingIndex() {
        return player.getPlayingIndex();
    }

    @Override
    public void changeModel() {
        player.changeModel();
    }

    @Override
    public Model getModel() {
        return player.getModel();
    }

    @Override
    public List<Music> getMusicList() {
        return player.getMusicList();
    }


    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    public void unregisterObserver(Observer observer) {
        observers.remove(observer);
    }


    // 加载专辑封封面和歌词
    private void loadNetworkResource() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Music music = player.getPlayingMusic();
                if (music == null) return;
                MusicAPI api = MusicAPIFactory.getMusicAPI();
                api.getInfo(music);
                if (music.getCoverPath() != null) {
                    Bitmap cover = HttpUtil.requestBitmap(music.getCoverPath(), HttpUtil.METHOD_GET);
                    loadCover(cover);
                }
                api.getLyric(music);
                loadLyric(music.getLyric());
            }
        }).start();
    }

    private void loadCover(Bitmap cover) {
        if (cover == null) return;
        for (Observer c : observers) {
            c.onCoverLoad(cover);
        }
    }

    private void loadLyric(List<LyricLine> lyric) {
        for (Observer c : observers) {
            c.onLyricLoad(lyric);
        }
    }

    public void notifySkipNext() {
        for (Observer c : observers) {
            c.onSkipNext();
        }
    }

    public void notifySkipLast() {
        for (Observer c : observers) {
            c.onSkipLast();
        }
    }

    public void notifyPlayStateChanged() {
        for (Observer c : observers) {
            c.onPlayStateChanged();
        }
    }

}
