package com.soling.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import com.soling.App;
import com.soling.model.Music;
import com.soling.utils.db.DBHelper;
import com.soling.utils.db.MusicHelper;

public class MusicFileManager {

    private static final String TAG = "MusicFileManager";
    private static final int MIN_DURATION = 20000;  // 设置歌曲的最短时长

    private static MusicFileManager mInstance;
    private static ContentResolver mContentResolver;
    private List<Music> localMusics;

    public static MusicFileManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MusicFileManager();
            mContentResolver = context.getContentResolver();
        }
        return mInstance;
    }

    public void getLocalMusics(final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                localMusics = new ArrayList<>();
                Cursor c = null;
                try {
                    c = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
                    while (c.moveToNext()) {
                        String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));    // 路径名
                        if (!FileUtil.isExist(path)) {
                            continue;
                        }
                        int id = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                        String name = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                        String album = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                        String artist = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                        long size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                        int duration = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                        if ("<unknown>".equals(artist)) {
                            artist = "";
                        }
                        if ("song".equals(album)) {
                            album = "";
                        }
                        Music music = new Music(id, name, album, path, artist, size, duration);
                        Log.d(TAG, "getLocalMusics: " + music.toString());
                        if (music.getDuration() > MIN_DURATION)
                            localMusics.add(music);
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } finally {
                    if (c != null) c.close();
                }
                List<Music> musicsData = MusicHelper.query();
                Log.d(TAG, "getLocalMusics: " + musicsData.toString());
                for (Music mData : musicsData) {
                    Music music = Music.findMusicById(localMusics, mData.getId());
                    if (music == null) {
                        MusicHelper.delete(mData);
                        continue;
                    }
                    music.setLocalCoverPath(mData.getLocalCoverPath());
                    music.setLocalLyricPath(mData.getLocalLyricPath());
                    music.setLike(mData.isLike());
                }
                for (Music music : localMusics) {
                    Music mData = Music.findMusicById(musicsData, music.getId());
                    if (mData == null) {
                        MusicHelper.insert(music);
                    }
                }
                callback.onFinish(localMusics);
            }
        }).start();
    }

    public interface Callback {
        void onFinish(List<Music> musics);
    }


}
