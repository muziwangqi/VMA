package com.soling.presenter;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Toast;

public class SetLight {

    private Activity activity;
    private ContentResolver contentResolver;
    private int brightness;

    public SetLight() {

    }

    public SetLight(Activity activity) {
        this.activity = activity;
        this.contentResolver = activity.getContentResolver();
        this.brightness = getBright(activity);
    }

    public static boolean isAutoBright(Activity activity) {
        boolean isAutoBrigt = false;
        ContentResolver contentResolver1 = activity.getContentResolver();
        try {
            if (Settings.System.getInt(contentResolver1, Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC)
                isAutoBrigt = true;
        } catch (Exception e) {
            Toast.makeText(activity, "无法获取屏幕亮度", Toast.LENGTH_SHORT).show();
        }

        return isAutoBrigt;
    }

    public static void startAutoBright(Activity activity){
        Settings.System.putInt(activity.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS_MODE,Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        Uri uri=android.provider.Settings.System.getUriFor("screen_brightness");
        activity.getContentResolver().notifyChange(uri,null);
    }

    public void stopAutoBright(Activity activity) {
        Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        Uri uri=android.provider.Settings.System.getUriFor("screen_brightness");
        activity.getContentResolver().notifyChange(uri,null);
    }

    public static int getBright(Activity activity) {
        int nowLight = 0;
        ContentResolver contentResolver = activity.getContentResolver();
        try {
            nowLight = android.provider.Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("getBright:" + nowLight);
        return nowLight;
    }

    public static void setBright(Activity activity, int brightness) {
        WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
        layoutParams.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f - brightness % 5;
        activity.getWindow().setAttributes(layoutParams);
    }

    //save
    public static void saveBright(ContentResolver contentResolver, int brightness) {
        Uri uri = android.provider.Settings.System.getUriFor("screen_brightness");
        android.provider.Settings.System.putInt(contentResolver, "screen_brightness", brightness);
        contentResolver.notifyChange(uri, null);
    }

    public static void setBrightMode(Activity activity,int mode){
        //1 auto  2
        Settings.System.putInt(activity.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS_MODE,mode);
    }
}
