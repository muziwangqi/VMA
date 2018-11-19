package com.soling.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Field;

public class FloatButtonUtil {
    private static int statusBarHeight;
    //屏幕像素点
    private static final Point screenSize = new Point();
    // 获取屏幕像素点
    public static Point getScreenSize(Activity context){
        if(context==null){
            return screenSize;
        }
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if(wm!=null){
            DisplayMetrics mDisplayMetrics = new DisplayMetrics();
            Display display = wm.getDefaultDisplay();
            if(display!=null){
                display.getMetrics(mDisplayMetrics);
                int W = mDisplayMetrics.widthPixels;
                int H = mDisplayMetrics.heightPixels;
                if(W*H>0&&(W>screenSize.x||H>screenSize.y)){
                    screenSize.set(W,H);
                }
            }
        }
        return screenSize;
    }
    // 获取状态栏高度
    public static int getStatusBarHeight(Context context){
        if(statusBarHeight<=0){
            Rect frame = new Rect();
            ((Activity)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            statusBarHeight = frame.top;
            if(statusBarHeight<=0){
                try{
                    Class<?> c = Class.forName("com.android.internal.R$dimen");
                    Object obj = c.newInstance();
                    Field field = c.getField("status_bar_height");
                    int x = Integer.parseInt(field.get(obj).toString());
                    statusBarHeight = context.getResources().getDimensionPixelSize(x);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        return statusBarHeight;
    }

}
