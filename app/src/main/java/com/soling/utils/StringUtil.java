package com.soling.utils;

import java.util.List;

public class StringUtil {

    public static String msToString(int millisecond) {
        int second = millisecond / 1000;
        int min = second / 60;
        second = second % 60;
        return String.valueOf(min) +
                (second < 10 ? ":0" : ":") +
                second;
    }

    public static List<String> sortByHeadChar() {
        return null;
    }

}
