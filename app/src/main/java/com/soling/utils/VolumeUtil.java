package com.soling.utils;

import android.content.Context;
import android.media.AudioManager;

import com.soling.App;

public class VolumeUtil {

    public static int getMaxVolume() {
        AudioManager audioManager = (AudioManager) App.getInstance().getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    public static int getVolume() {
        AudioManager audioManager = (AudioManager) App.getInstance().getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public static void setVolume(int volume) {
        AudioManager audioManager = (AudioManager) App.getInstance().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }

}
