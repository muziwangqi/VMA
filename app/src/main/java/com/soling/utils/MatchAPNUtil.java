package com.soling.utils;

public class MatchAPNUtil {
    private static String CMWAP="cmwap";//yd
    private static String CMNET="cmnet";
    private static String GWAP_3="3gwap";//lt
    private static String GNET_3="3gnet";
    private static String UNIWAP="uniwap";
    private static String UNINET="uninet";
    private static String CTWAP="ctwap";//dx
    private static String CTNET="ctnet";
    public static String matchType(String currType){
        if (currType.equals("")||currType==null){
            return "";
        }
        currType=currType.toLowerCase();
        if (currType.startsWith(CMWAP)){
            return CMWAP;
        }else if (currType.startsWith(CMNET)){
            return CMWAP;
        }else if (currType.startsWith(GWAP_3)){
            return GWAP_3;
        }else if (currType.startsWith(GNET_3)){
             return GNET_3;
        }else if (currType.startsWith(UNINET)){
            return UNINET;
        }else if (currType.startsWith(UNIWAP)){
            return UNIWAP;
        }else if (currType.startsWith(CTNET)){
            return CTNET;
        }else if (currType.startsWith(CTWAP)){
            return CMWAP;
        }
        return "";
    }
}
