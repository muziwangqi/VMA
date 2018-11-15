package com.soling.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.soling.utils.HttpUtil;



public class NeteaseAPI {

    public static final String HOST = "http://192.168.11.241:3000";
    public static final String PATH_SEARCH = "/search?keywords=";
    public static final String PATH_ALBUM_DETAIL = "/album?id=";
    public static final String PATH_LYRIC = "/lyric?id=";
    public static final String PATH_SEARCH_HOT = "/search/hot";
    private static final String TAG = "NeteaseAPI";
    private static final String PATH_LOGIN = "/login/cellphone?";
    private static final String PATH_USER_INFORMATION = "/song/detail/?uid=";
    private static final String PATH_USER_ATTENTION = "/user/follows?uid=";   
    public static JSONObject getLyric(int musicId) {
        String url = HOST + PATH_LYRIC + musicId;
        return HttpUtil.requestJSON(url, HttpUtil.METHOD_GET);
    }

    public static JSONObject search(String...args) {
        JSONObject result;
        String url = HOST + PATH_SEARCH;
        for (String s : args) {
            url = url + s + " ";
        }
        result = HttpUtil.requestJSON(url, HttpUtil.METHOD_GET);
        return result;
    }

    public static JSONObject getAlbumDetail(String albumId) {
        String url = HOST + PATH_ALBUM_DETAIL + albumId;
        JSONObject result = HttpUtil.requestJSON(url, HttpUtil.METHOD_GET);
        return result;
    }

    public static JSONObject searchHot() {
        String url = HOST + PATH_SEARCH_HOT;
        JSONObject result = HttpUtil.requestJSON(url, HttpUtil.METHOD_GET);
        return result;
    }
    public static JSONObject login(String userNumber,String password){
    	String url = HOST+PATH_LOGIN+"phone="+userNumber+"&password="+password;
    	JSONObject result = HttpUtil.requestJSON(url, HttpUtil.METHOD_GET);
    	return result;
    }
    public static JSONObject getAttention(String userId){
    	String url = HOST+PATH_USER_ATTENTION+userId;
    	JSONObject result = HttpUtil.requestJSON(url, HttpUtil.METHOD_GET);
    	return result;
    }
    public static JSONObject getInformation(String userId){
    	String url = HOST+PATH_USER_INFORMATION+userId;
    	JSONObject result = HttpUtil.requestJSON(url, HttpUtil.METHOD_GET);
    	try {
			JSONArray jsonArray = result.getJSONArray("follow");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
    }
}
