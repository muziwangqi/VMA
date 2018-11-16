package com.soling.presenter;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.List;

import com.soling.App;
import com.soling.model.LyricLine;
import com.soling.model.Music;
import com.soling.model.PlayList;
import com.soling.service.player.IPlayer;
import com.soling.service.player.PlayerService;
import com.soling.utils.DBHelper;

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
		playerService.play(music);
	}

    public void delete(List<Music> musicList, int position) {
        Music music = musicList.get(position);
        if (TextUtils.equals(getPlayingMusic().getPath(), music.getPath())) {
            playNext();
        }
        File file = new File(music.getPath());
        if (file.exists() && file.isFile()) {
            file.delete();
            musicList.remove(position);
        }
        if (music.isLike()) {
            deleteLike(music);
        }
    }

    public boolean likeToggle(Music music) {
        if (music.isLike()) {
            deleteLike(music);
        }
        else {
            insertLike(music);
        }
        music.setLike(!music.isLike());
        return music.isLike();
    }

    private void insertLike(Music music) {
        App.getInstance().getLikeMusics().add(music);
        SQLiteDatabase db = App.getInstance().getDbHelper().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.Table.MusicLike.COLUMN_MUSIC_ID, music.getId());
        db.insert(DBHelper.Table.MusicLike.TABLE_NAME, null, values);
        db.close();
    }

    private void deleteLike(Music music) {
        App.getInstance().getLikeMusics().remove(music);
        SQLiteDatabase db = App.getInstance().getDbHelper().getWritableDatabase();
        db.delete(DBHelper.Table.MusicLike.TABLE_NAME, DBHelper.Table.MusicLike.COLUMN_MUSIC_ID + "==?", new String[]{Integer.toString(music.getId())});
        db.close();
    }

}
