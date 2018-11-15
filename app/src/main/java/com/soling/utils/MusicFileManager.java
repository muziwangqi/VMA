package com.soling.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.soling.model.Music;

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

    public List<Music> getLocalMusics() {
        if (localMusics != null) return localMusics;
        localMusics = new ArrayList<>();
        Cursor c = null;
        try {
            c = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            while (c.moveToNext()) {
                String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));    // 路径名
                if (!isExist(path)) {
                    continue;
                }
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
                Music music = new Music(name, album, path, artist, size, duration);
                if (music.getDuration() > MIN_DURATION)
                    localMusics.add(music);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            if (c != null) c.close();
        }
        Log.d(TAG, "getLocalMusics() returned: " + localMusics);
        return localMusics;
    }

    public static boolean isExist(String path) {
        File file = new File(path);
        return file.exists();
    }



}
