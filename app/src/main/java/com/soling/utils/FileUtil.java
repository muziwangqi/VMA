package com.soling.utils;

import java.io.File;

public class FileUtil {

    public static boolean remove(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            file.delete();
            return true;
        }
        return false;
    }

}
