package com.soling;

import android.app.Application;

import java.util.LinkedList;
import java.util.List;

import com.soling.model.Music;
import com.soling.model.PlayList;
import com.soling.utils.db.DBHelper;

public class App extends Application {

    private static final String TAG = "App";

    private PlayList localMusics;
    private PlayList likeMusics;

    private DBHelper dbHelper;

    private static App INSTANCE = null;
    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    public PlayList getLocalMusics() {
        return localMusics;
    }

    public void setLocalMusics(PlayList localMusics) {
        this.localMusics = localMusics;
        List<Music> likes = new LinkedList<>();
        for (Music music : localMusics.getMusics()) {
            if (music.isLike())
                likes.add(music);
        }
        likeMusics = new PlayList(likes);
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

    public PlayList getLikeMusics() {
        return likeMusics;
    }

}
