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
        player.unregisterObserver(this);
        context.unbindService(conn);
    }

    @Override
    public void play(int index) {
        player.play(index);
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
    }

    @Override
    public IPlayer.Model getModel() {
        return player.getModel();
    }

    @Override
    public void onPlayNext() {
        view.refreshView();
    }

    @Override
    public void onPlayLast() {
        view.refreshView();
    }

    @Override
    public void onPlayToggle() {
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
        player.play(music);
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
