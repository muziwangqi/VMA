package com.soling.view.activity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;

import com.soling.R;
import com.soling.utils.APNUtil;
import com.soling.utils.MatchAPNUtil;
import com.soling.view.adapter.WiperSwitch;

import java.util.ArrayList;
import java.util.List;

public class WirelessActivity extends BaseActivity {

    private WiperSwitch wsMobileNet;
    private Context context;
    private String id, type, apn, curr;

    Uri uri = Uri.parse("content://telephony/carriers");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wireless);
        context = this;
        initViews();
        getMobileNet();
    }

    @Override
    public void initViews() {
        wsMobileNet = (WiperSwitch) findViewById(R.id.ws_mobilenet);
    }

    private void getMobileNet() {
        //判断是否已连接移动网
//        boolean flag;
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) {
//            flag = true;
//        } else {
//            flag = false;
//        }
        wsMobileNet.setChecked(false);
        wsMobileNet.setOnChangedListener(new WiperSwitch.IOnChangedListener() {
            @Override
            public void onChange(WiperSwitch wiperSwitch, boolean checkStat) {
                if (checkStat) {
                    //openAPN(context);
                    shortToast("移动网络已开启");
                } else {
                    //closeAPN(context);
                    shortToast("移动网络已关闭");
                }
            }
        });
    }

    public void openAPN(Context context) {
        List<APNUtil> apnList = new ArrayList<APNUtil>();
        apnList=getAPNList();
        for (APNUtil apnUtil : apnList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("apn", MatchAPNUtil.matchType(apnUtil.getApn()));
            contentValues.put("type", MatchAPNUtil.matchType(apnUtil.getType()));
            context.getContentResolver().update(uri, contentValues, "id=?", new String[]{apnUtil.getId()});
        }
    }

    public void closeAPN(Context context) {
        List<APNUtil> apnList = new ArrayList<APNUtil>();
        apnList=getAPNList();
        for (APNUtil apnUtil : apnList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("apn", MatchAPNUtil.matchType(apnUtil.getApn())+"mdev");
            contentValues.put("type", MatchAPNUtil.matchType(apnUtil.getType())+"mdev");//通过设置错误的APN关闭移动网
            context.getContentResolver().update(uri, contentValues, "id=?", new String[]{apnUtil.getId()});
        }
    }

    public ArrayList<APNUtil> getAPNList() {
        String[] projection = {id, type, apn, curr};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        ArrayList<APNUtil> list = new ArrayList<APNUtil>();
        while (cursor != null && cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex("apn")).equals("")) {

            } else {
                APNUtil apnUtil = new APNUtil();
                apnUtil.setId(cursor.getString(cursor.getColumnIndex("id")));
                apnUtil.setApn(cursor.getString(cursor.getColumnIndex("apn")));
                apnUtil.setType(cursor.getString(cursor.getColumnIndex("type")));
                list.add(apnUtil);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }
}
