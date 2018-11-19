package com.soling.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Toast;

import com.soling.App;
import com.soling.R;


import com.soling.utils.DBHelper;

import com.soling.utils.MusicFileManager;


public abstract class BaseActivity extends AppCompatActivity {

    private boolean isShowTitle = true;
    private static Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    }
    public void initViews(){}
    public void initDatas(){}
    public void setListeners(){}
        public void setTitle(boolean isShow){
            isShowTitle=isShow;
     }


    public void longToast(String str){
        if (toast==null){
            toast=new Toast(this);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }else{
            toast.setText(str);
            toast.show();
        }
    }

    public void shortToast(String str){
        if (toast==null){
            toast=new Toast(this);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }else{
            toast.setText(str);
            toast.show();
        }
    }

    //wucan
    public void intentJump(Context context,Class<?> cls){
        Intent intent=new Intent(context,cls);
        startActivity(intent);
    }

    //有参
    public void intentJump(Context context,Class<?> cls,Bundle bundle){
        Intent intent=new Intent(context,cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

