package com.soling.model;

import android.provider.CallLog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PhoneCallLog {
    String date;
    String name;
    String type;
    String duration;
    String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(int anInt) {
        switch (anInt){
            case CallLog.Calls.INCOMING_TYPE:
                this.type = "呼入";
                break;
            case CallLog.Calls.OUTGOING_TYPE:
                this.type = "呼出";
                break;
            case CallLog.Calls.MISSED_TYPE:
                this.type = "未接";
                break;
            default:
                break;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = formatDate(date);
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = formatDuration(duration);
    }
    /*
    时间格式转化
     */
    public String formatDate(long time){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.getDefault());
        return format.format(new Date(time));
    }
    /*
    通话时长格式转化
     */
    public String formatDuration(long time){
        long s = time%60;
        long m = time/60;
        long h = time/60/60;
        StringBuilder sb = new StringBuilder();
        if(h>0){
            sb.append(h).append("小时");
        }
        if(m>0){
            sb.append(m).append("分");
        }
        sb.append(s).append("秒");
        return sb.toString();
    }
}

