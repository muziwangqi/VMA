package com.soling;

import android.app.Application;

import java.util.LinkedList;
import java.util.List;

import com.soling.model.Music;
import com.soling.utils.DBHelper;
import com.soling.utils.MusicFileManager;
import com.soling.utils.SharedPreferenceUtil;

public class App extends Application {

    private static final String TAG = "App";
    private static boolean THEMEC=true;//fen

    private List<Music> localMusics;
    private List<Music> likeMusics;

    private DBHelper dbHelper;

    private static App INSTANCE = null;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        MusicFileManager musicFileManager = MusicFileManager.getInstance(this);
        //isThemec();
    }
   /* public static boolean isThemec(){
        if (SharedPreferenceUtil.get(App.getInstance(), "theme", "dayTheme").equals(THEMEC)) {
            App.getInstance().setTHEMEC(true);
        }
        return THEMEC;
    }*/

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

    public void setTHEMEC(boolean THEMEC) {
        this.THEMEC = THEMEC;
    }

    public boolean isTHEMEC() {
        return THEMEC;
    }

}
