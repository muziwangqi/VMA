package com.soling.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Toast;

import com.soling.App;

import com.soling.R;


import com.soling.service.player.PlayerService;
import com.soling.utils.db.DBHelper;

import com.soling.utils.MusicFileManager;
import com.soling.utils.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {

    private boolean isShowTitle = true;
    private static Toast toast;
    private PermissionListener mListener;
    private static final int PERMISSION_REQUESTCODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        if (App.getInstance().isTHEMEC()) {
//            App.getInstance().setTHEMEC(true);
//            setTheme(R.style.dayTheme);
//        } else {
//            App.getInstance().setTHEMEC(false);
//            setTheme(R.style.nightTheme);
//        }

        super.onCreate(savedInstanceState);

        if (!isShowTitle) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        initViews();
        initDatas();
        setListeners();

        // 添加dbHelper hyw
        App app = (App) getApplication();
        if (app.getDbHelper() == null) {
            app.setDbHelper(new DBHelper(this, DBHelper.DATABASE_NAME, null, DBHelper.VERSION));
        }
        if (app.getPlayerService() == null) {
            Intent intent = new Intent(this, PlayerService.class);
            startService(intent);
            app.setPlayerService(intent);
        }
    }

    public void initViews() {
    }

    public void initDatas() {
    }

    public void setListeners() {
    }

    public void setTitle(boolean isShow) {
        isShowTitle = isShow;
    }

    public void longToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void shortToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    //wucan
    public void intentJump(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }

    //有参
    public void intentJump(Context context, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(context, cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //封装动态权限
    public void requestRunPermission(String[] permissions, PermissionListener listener) {
        mListener = listener;
        List<String> permissionLists = new ArrayList<String>();
        for(String peimission : permissions){
            if(ContextCompat.checkSelfPermission(this,peimission)!=PackageManager.PERMISSION_GRANTED){
                permissionLists.add(peimission);
            }
        }
        if(!permissionLists.isEmpty()){
            ActivityCompat.requestPermissions(this,permissionLists.toArray(new String[permissionLists.size()]), PERMISSION_REQUESTCODE);
        }else{
            //表示全都授权了
            mListener.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUESTCODE:
                if(grantResults.length>0){
                    List<String> deniedPermissions = new ArrayList<String>();
                    for(int i=0; i<grantResults.length; i++){
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if(grantResult!=PackageManager.PERMISSION_GRANTED){
                            deniedPermissions.add(permission);
                        }
                    }
                    if(deniedPermissions.isEmpty()){
                        //说明都授权了
                        mListener.onGranted();
                    }else{
                        mListener.onDenied(deniedPermissions);
                    }
                }
                break;
                default:break;

        }
    }

    /*
        已授权、未授权的接口回调
         */
    interface  PermissionListener{
        void onGranted();//已授权
        void onDenied(List<String> deniedPermission);//未授权

    }

}

