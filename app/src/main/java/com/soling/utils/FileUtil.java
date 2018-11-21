package com.soling.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

    public static final String DIR_APP_ROOT = "vmusic";
    public static final String DIR_LYRIC = "lyric";



    public static boolean remove(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            file.delete();
            return true;
        }
        return false;
    }

    public static boolean isExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static File getDirAppRoot() {
        File appRoot = new File(Environment.getExternalStorageDirectory(), DIR_APP_ROOT);
        if (!appRoot.exists()) {
            appRoot.mkdir();
        }
        return appRoot;
    }

    public static void save(String path, String content) {
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
