package com.soling.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PhoneInformation {
    private String date;
    private String strAddress;
    private String person ;
    private String strBody;
    private String type;

    public String getDate() {
        return date;
    }

    public void setDate(long time) {
        this.date = formatDate(time);
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getStrAddress() {
        return strAddress;
    }

    public void setStrAddress(String strAddress) {
        this.strAddress = strAddress;
    }

    public String getStrBody() {
        return strBody;
    }

    public void setStrBody(String strBody) {
        this.strBody = strBody;
    }

    public String getType() {
        return type;
    }

    public void setType(int anInt) {
        this.type = getMessageType(anInt);
    }
    /*
    短信阅读状态方法
     */
    private String getMessageRead(int anInt){
        if(1==anInt){
            return "已读";
        }if(2==anInt){
            return "未读";
        }
        return null;
    }
    /*
    短信类型区分代码
     */
    private String getMessageType(int anInt){
        if(1==anInt){
            return "收到的";
        }if(2==anInt){
            return "已发出";
        }
        return null;
    }
    /*
    短信接收状态代码
     */
    public String getMessageStatus(int anInt){
        switch (anInt){
            case -1:
                return "接收";
            case 0:
                return "complete";
            case 64:
                return "pending";
            case 128:
                return "failed";
        }
        return null;
    }
    /*
   时间格式转化
    */
    public String formatDate(long time){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.getDefault());
        return format.format(new Date(time));
    }


}
