package com.soling.utils;

import android.Manifest;
import android.os.Build;
/*
获取设备权限工具类，适配安卓较高版本
 */
public final class PermissionUtil {
    public static String[] CALENDAR ;    //读写日历
    public static String[] CAMERA;       // 相机
    public static String[] CONTACTS;    // 读写联系人。
    public static String[] LOCATION;    // 读位置信息。
    public static String[] MICROPHONE; // 使用麦克风。
    public static String[] PHONE;      // 读电话状态、打电话、读写电话记录。
    public static String[] SENSORS;    // 传感器。
    public static String[] SMS;        // 读写短信、收发短信。
    public static String[] STORAGE;    // 读写存储卡
    static {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            CALENDAR = new String[]{};
            CAMERA = new String[]{};
            CONTACTS = new String[]{};
            LOCATION = new String[]{};
            MICROPHONE = new String[]{};
            PHONE = new String[]{};
            SENSORS = new String[]{};
            SMS = new String[]{};
            STORAGE = new String[]{};
        } else {
            CALENDAR = new String[]{
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR};

            CAMERA = new String[]{
                    Manifest.permission.CAMERA};

            CONTACTS = new String[]{
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.GET_ACCOUNTS};

            LOCATION = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION};

            MICROPHONE = new String[]{
                    Manifest.permission.RECORD_AUDIO};

            PHONE = new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.WRITE_CALL_LOG,
                    Manifest.permission.USE_SIP,
                    Manifest.permission.PROCESS_OUTGOING_CALLS,
                    Manifest.permission.ADD_VOICEMAIL
            };

            SENSORS = new String[]{
                    Manifest.permission.BODY_SENSORS};

            SMS = new String[]{
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_WAP_PUSH,
                    Manifest.permission.RECEIVE_MMS};

            STORAGE = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
    }

}

