package com.soling;


import android.app.Application;
import android.content.Intent;

import java.util.LinkedList;
import java.util.List;

import com.soling.model.Music;

import com.soling.utils.MusicFileManager;
import com.soling.utils.SharedPreferenceUtil;
import com.soling.model.PlayList;
import com.soling.utils.db.DBHelper;

public class App extends Application {

    private static final String TAG = "App";
    private static boolean THEMEC = true;//fen

    private PlayList localMusics;
    private PlayList likeMusics;

    private DBHelper dbHelper;

    private static App INSTANCE = null;

    private Intent playerService;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        MusicFileManager musicFileManager = MusicFileManager.getInstance(this);
        isThemec();
    }

    public static boolean isThemec() {
        if (SharedPreferenceUtil.get(App.getInstance(), "theme", "dayTheme").equals("dayTheme")) {
            App.getInstance().setTHEMEC(THEMEC);
        }
        return THEMEC;
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

    public void setTHEMEC(boolean THEMEC) {
        this.THEMEC = THEMEC;
    }

    public boolean isTHEMEC() {
        return THEMEC;
    }

    public Intent getPlayerService() {
        return playerService;
    }

    public void setPlayerService(Intent playerService) {
        this.playerService = playerService;
    }
}
