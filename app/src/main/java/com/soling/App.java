package com.soling;

import android.app.Application;

import java.util.List;

import com.soling.model.Music;


public class App extends Application {

    private static final String TAG = "App";

    private List<Music> localMusics;

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
    }

    public static App getInstance() {
        return INSTANCE;
    }

}
