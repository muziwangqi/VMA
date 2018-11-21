package com.soling.utils;

public class TimeUtil {

    // 将例如01:30(一分三十秒)格式的时间转换成毫秒
    public static long resolveTime(String time) {
        try {
            String[] mapStr = time.split(":");
            int min = Integer.parseInt(mapStr[0]);
            double sec = Double.parseDouble(mapStr[1]);
            return (long) ((min * 60 + sec) * 1000);
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
