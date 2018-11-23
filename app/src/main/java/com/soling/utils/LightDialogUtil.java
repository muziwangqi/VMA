package com.soling.utils;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import com.soling.App;

public class LightDialogUtil {
    private static final String TAG = LightDialogUtil.class.getSimpleName();
    private static int MAX_BRIGHTNESS = 255;
    private static int MIN_BRIGHTNESS = 30;

    //获取当前系统亮度方式 1自动 0手动
    public static int getBrightnessMode() {
        int brightnessMode = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;
        try {
            brightnessMode = Settings.System.getInt(App.getInstance().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException e) {
            Log.d(TAG, "getBrightnessMode: 获取当前系统屏幕亮度失败");
        }
        return brightnessMode;
    }

    //设置当前系统屏幕亮度模式
    public static void setBrightnessMode(int brightnessMode) {
        try {
            Settings.System.putInt(App.getInstance().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, brightnessMode);
        } catch (Exception e) {
            Log.d(TAG, "setBrightnessMode: 设置当前系统屏幕亮度失败");
        }
    }

    //获取当前系统亮度值
    public static int getSystemBrightness() {
        int screenBrightness = MAX_BRIGHTNESS;
        try {
            screenBrightness = Settings.System.getInt(App.getInstance().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            Log.d(TAG, "getSystemBrightness: 获取当前系统亮度值失败");
        }
        return screenBrightness;
    }

    //设置当前系统亮度值
    public static void setSystemBrightness(int brightness) {
        try {
            ContentResolver contentResolver = App.getInstance().getContentResolver();
            Uri uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
            Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
            contentResolver.notifyChange(uri, null);
        } catch (Exception e) {
            Log.d(TAG, "setSystemBrightness: 设置当前系统亮度值失败");
        }
    }
}
