package com.soling.model;

import com.soling.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class LyricLine {

    private long timestamp;
    private String text;

    public LyricLine(long timestamp, String text) {
        this.timestamp = timestamp;
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "LyricLine{" +
                "timestamp=" + timestamp +
                ", text='" + text + '\'' +
                '}';
    }



}
