package com.soling.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import com.soling.App;
import com.soling.model.LyricLine;
import com.soling.model.Music;
import com.soling.model.PlayList;
import com.soling.service.player.IPlayer;
import com.soling.service.player.PlayerService;
import com.soling.utils.FileUtil;
import com.soling.utils.VolumeUtil;
import com.soling.utils.db.MusicHelper;

public class PlayerPresenter implements PlayerContract.Presenter, IPlayer.Observer {

    private static final String TAG = "PlayerPresenter";

    private PlayerContract.View view;
    private IPlayer player;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) iBinder;
            PlayerService playerService = binder.getService();
            player = playerService.getPlayer();
            player.registerObserver(PlayerPresenter.this);
            view.onPlayServiceBound();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    public PlayerPresenter(PlayerContract.View view) {
        this.view = view;
    }

    @Override
    public void bindPlayService() {
        Log.d(TAG, "bindPlayService: ");
        App.getInstance().bindService(new Intent(App.getInstance(), PlayerService.class), conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void unbindPlayService() {
        player.unregisterObserver(this);
        App.getInstance().unbindService(conn);
    }

    @Override
    public void play(PlayList playList, int startIndex) {
        player.play(playList, startIndex);
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public void resume() {
        player.resume();
    }

    @Override
    public void playNext() {
        player.playNext();
    }

    @Override
    public void playLast() {
        player.playLast();
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
    public Music getPlayingMusic() {
        return player.getPlayingMusic();
    }

    @Override
    public void changeModel() {
        player.changeModel();
        view.changeModel();
    }

    @Override
    public IPlayer.Model getModel() {
        return player.getModel();
    }

    @Override
    public void onPlayChange() {
        view.refreshView();
    }

    @Override
    public void onPlayToggle() {
        view.refreshPlayState();
    }

    @Override
    public void onCoverLoad(Bitmap cover) {
        view.loadCover(cover);
    }

    @Override
    public void onLyricLoad(List<LyricLine> lyric) {
        view.loadLyric(lyric);
    }

	@Override
    public void delete(List<Music> musicList, int position) {
        Music music = musicList.get(position);
        if (TextUtils.equals(getPlayingMusic().getPath(), music.getPath())) {
            playNext();
        }
        if (FileUtil.remove(music.getPath())) {
            musicList.remove(position);
            if (music.isLike()) {
                music.setLike(!music.isLike());
                MusicHelper.update(music);
            }
        }
    }

    @Override
    public void likeToggle(Music music) {
        if (music.isLike()) {
            App.getInstance().getLikeMusics().getMusics().remove(music);
        }
        else {
            App.getInstance().getLikeMusics().getMusics().add(music);
        }
        music.setLike(!music.isLike());
        MusicHelper.update(music);
        view.refreshLike(music.isLike());
    }

    @Override
    public int getMaxVolume() {
        return VolumeUtil.getMaxVolume();
    }

    @Override
    public int getVolume() {
        return VolumeUtil.getVolume();
    }

    @Override
    public void setVolume(int progress) {
        VolumeUtil.setVolume(progress);
    }

    @Override
    public void loadLyric() {
        player.loadLyric(this);
    }

    @Override
    public void loadCover() {
        player.loadCover(this);
    }

}
