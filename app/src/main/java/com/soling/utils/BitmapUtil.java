package com.soling.utils;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import com.soling.App;

public class BitmapUtil {

    public static RoundedBitmapDrawable roundedBitmapDrawable(Bitmap bitmap) {
        RoundedBitmapDrawable result = RoundedBitmapDrawableFactory.create(App.getInstance().getResources(), bitmap);
        result.setAntiAlias(true);
        result.setCornerRadius(Math.min(bitmap.getWidth(), bitmap.getHeight()));
        return result;
    }

}
