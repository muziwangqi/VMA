package com.soling.api;

public class MusicAPIFactory {

    public static MusicAPI getMusicAPI() {
        return NeteaseAPIAdapter.getInstance();
    }

}
