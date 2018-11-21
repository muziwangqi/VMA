package com.soling.utils;

import android.util.Log;

import com.soling.model.LyricLine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LyricUtil {

    private static final String DIR_LYRIC = "lyric";
    private static final String TAG = "LyricUtil";

    public static List<LyricLine> resolve(String lyric) {
        Log.d(TAG, "resolve: " + lyric);
        List<LyricLine> result = new ArrayList<>();
        String[] lyricLines = lyric.split("\n");
        for (String line : lyricLines) {
            String[] mapStr = line.split("]");
            if (!mapStr[0].startsWith("[")) continue;
            mapStr[0] = mapStr[0].substring(1);
            long time = TimeUtil.resolveTime(mapStr[0]);
            result.add(new LyricLine(time, mapStr.length > 1 ? mapStr[1].trim() : ""));
        }
        Log.d(TAG, "resolve: " + result.toString());
        return result.size() > 0 ? result : null;
    }

    public static String save(String lyric) {
        Log.d(TAG, "save: " + lyric);
        File lyricDir = new File(FileUtil.getDirAppRoot(), DIR_LYRIC);
        if (!lyricDir.exists()) {
            lyricDir.mkdir();
        }
        File lyricFile = new File(lyricDir, UUID.randomUUID().toString() + ".lrc");
        try {
            if (!lyricFile.exists()) {
                lyricFile.createNewFile();
            }
            FileWriter writer = new FileWriter(lyricFile);
            writer.write(lyric);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lyricFile.getAbsolutePath();
    }

    public static String read(String path) {
        File file = new File(path);
        String result = null;
        if (!file.exists()) return null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String str = null;
            StringBuilder sb = new StringBuilder();
            while ((str = reader.readLine()) != null) {
                sb.append(str);
                sb.append("\n");
            }
            result = sb.toString();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
