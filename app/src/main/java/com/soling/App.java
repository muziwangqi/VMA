package com.soling;


import android.app.Application;

import java.util.LinkedList;
import java.util.List;

import com.soling.model.Music;
<<<<<<< HEAD
import com.soling.utils.DBHelper;
import com.soling.utils.MusicFileManager;
import com.soling.utils.SharedPreferenceUtil;
=======
import com.soling.model.PlayList;
import com.soling.utils.db.DBHelper;
>>>>>>> d218d0aabc28059c56374fcee69dfecee2d8b719

public class App extends Application {

    private static final String TAG = "App";
    private static boolean THEMEC=true;//fen

    private PlayList localMusics;
    private PlayList likeMusics;

    private DBHelper dbHelper;

    private static App INSTANCE = null;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
<<<<<<< HEAD
        MusicFileManager musicFileManager = MusicFileManager.getInstance(this);
        //isThemec();
=======
>>>>>>> d218d0aabc28059c56374fcee69dfecee2d8b719
    }
   /* public static boolean isThemec(){
        if (SharedPreferenceUtil.get(App.getInstance(), "theme", "dayTheme").equals(THEMEC)) {
            App.getInstance().setTHEMEC(true);
        }
        return THEMEC;
    }*/

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

    public  DBHelper getDbHelper() {
        return dbHelper;
    }

    public void setDbHelper(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public PlayList getLikeMusics() {
        return likeMusics;
    }

<<<<<<< HEAD
    public void setLikeMusics(List<Music> likeMusics) {
        this.likeMusics = likeMusics;
    }

    public void setTHEMEC(boolean THEMEC) {
        this.THEMEC = THEMEC;
    }

    public boolean isTHEMEC() {
        return THEMEC;
    }

=======
>>>>>>> d218d0aabc28059c56374fcee69dfecee2d8b719
}
