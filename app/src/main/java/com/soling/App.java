package com.soling;

import android.app.Application;

import java.util.LinkedList;
import java.util.List;

import com.soling.model.Music;
import com.soling.utils.DBHelper;

public class App extends Application {

    private static final String TAG = "App";

    private List<Music> localMusics;
    private List<Music> likeMusics;

    private DBHelper dbHelper;

    private static App INSTANCE = null;
    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    public List<Music> getLocalMusics() {
        return localMusics;
    }

    public void setLocalMusics(List<Music> localMusics) {
        this.localMusics = localMusics;
        likeMusics = new LinkedList<>();
        for (Music music : localMusics) {
            if (music.isLike())
                likeMusics.add(music);
        }
    }

    public static App getInstance() {
        return INSTANCE;
    }

    public DBHelper getDbHelper() {
        return dbHelper;
    }

    public void setDbHelper(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public List<Music> getLikeMusics() {
        return likeMusics;
    }

    public void setLikeMusics(List<Music> likeMusics) {
        this.likeMusics = likeMusics;
    }

}
