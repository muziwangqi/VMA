package com.soling.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import com.soling.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class BitmapUtil {

    private static final String DIR_COVER = "cover";

    public static RoundedBitmapDrawable roundedBitmapDrawable(Bitmap bitmap) {
        return roundedBitmapDrawable(bitmap, Math.min(bitmap.getWidth(), bitmap.getHeight()));
    }


    public static RoundedBitmapDrawable roundedBitmapDrawable(Bitmap bitmap, int radius) {
        RoundedBitmapDrawable result = RoundedBitmapDrawableFactory.create(App.getInstance().getResources(), bitmap);
        result.setAntiAlias(true);
        result.setCornerRadius(radius);
        return result;
    }

    public static String save(Bitmap bitmap) {
        return save(bitmap, UUID.randomUUID().toString() + ".jpg");
    }

    public static String save(Bitmap bitmap, String fileName) {
        File coverDir = new File(FileUtil.getDirAppRoot(), DIR_COVER);
        if (!coverDir.exists()) {
            coverDir.mkdir();
        }
        File file = new File(coverDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public static Bitmap read(String path) {
        Bitmap bitmap = null;
        try {
            File file = new File(path);
            if (!file.exists()) return null;
            FileInputStream fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
