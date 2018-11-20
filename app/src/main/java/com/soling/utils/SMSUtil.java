package com.soling.utils;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.soling.App;

public class SMSUtil {

    private static final String TAG = "SMSUtil";
    
    public static void sendSMS(String phoneNumber, String message) {
        Log.d(TAG, "sendSMS: " + phoneNumber + ": " + message);
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
        intent.putExtra("sms_body", message);
        App.getInstance().startActivity(intent);
    }

}
