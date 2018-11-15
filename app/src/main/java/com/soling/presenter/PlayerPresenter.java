package com.soling.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.Map;

import com.soling.model.LyricLine;
import com.soling.model.Music;
import com.soling.model.PlayList;
import com.soling.service.player.IPlayer;
import com.soling.service.player.PlayerService;

public class PlayerPresenter implements PlayerContract.Presenter, IPlayer.Observer {

    private static final String TAG = "PlayerPresenter";

    private Context context;
    private PlayerContract.View view;
    private PlayerService playerService;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) iBinder;
            playerService = binder.getService();
            playerService.registerObserver(PlayerPresenter.this);
            view.onPlayServiceBound();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            playerService = null;
        }
    };

    public PlayerPresenter(PlayerContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void bindPlayService() {
        Log.d(TAG, "bindPlayService: ");
        context.bindService(new Intent(context, PlayerService.class), conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void unbindPlayService() {
        playerService.unregisterObserver(this);
        context.unbindService(conn);
    }

    @Override
    public void play() {
        playerService.play();
    }

    @Override
    public void play(int index) {
        playerService.play(index);
    }

    @Override
    public void play(PlayList playList) {
        playerService.play(playList);
    }

    @Override
    public void play(PlayList playList, int startIndex) {
        playerService.play(playList, startIndex);
    }

    @Override
    public void pause() {
        playerService.pause();
    }

    @Override
    public void resume() {
        playerService.resume();
    }

    @Override
    public void playNext() {
        playerService.playNext();
    }

    @Override
    public void playLast() {
        playerService.playLast();
    }

    @Override
    public void seekTo(int progress) {
        playerService.seekTo(progress);
    }

    @Override
    public boolean isPlaying() {
        return playerService.isPlaying();
    }

    @Override
    public int getProgress() {
        return playerService.getProgress();
    }

    @Override
    public void release() {
        playerService.release();
    }

    @Override
    public Music getPlayingMusic() {
        return playerService.getPlayingMusic();
    }

    @Override
    public int getPlayingIndex() {
        return playerService.getPlayingIndex();
    }

    @Override
    public void changeModel() {
        playerService.changeModel();
    }

    @Override
    public Model getModel() {
        return playerService.getModel();
    }

    @Override
    public List<Music> getMusicList() {
        return playerService.getMusicList();
    }


    @Override
    public void onSkipNext() {
        view.refreshView();
    }

    @Override
    public void onSkipLast() {
        view.refreshView();
    }

    @Override
    public void onPlayStateChanged() {
        view.refreshView();
    }

    @Override
    public void onCoverLoad(Bitmap cover) {
        view.refreshCover(cover);
    }

    @Override
    public void onLyricLoad(List<LyricLine> lyric) {
        view.refreshLyric(lyric);
    }

	@Override
	public void play(Music music) {
		// TODO Auto-generated method stub	
	}

}
