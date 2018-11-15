package com.soling.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Bitmap;
import com.soling.model.LyricLine;
import com.soling.model.Music;
import com.soling.model.User;
import com.soling.utils.HttpUtil;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class NeteaseAPIAdapter implements MusicAPI {

    private static NeteaseAPIAdapter INSTANCE;

    private NeteaseAPIAdapter() {

    }

    public static NeteaseAPIAdapter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NeteaseAPIAdapter();
        }
        return INSTANCE;
    }

    public void getInfo(Music music) {
        try {
            // 获取专辑ID和音乐ID
            JSONObject json = NeteaseAPI.search(music.getName(), music.getArtist(), music.getAlbum());
            if (json == null) return;
            json = json.getJSONObject("result");
            JSONArray jsonArray = json.getJSONArray("songs");
            json = jsonArray.getJSONObject(0);
            int musicId = json.getInt("id");
            json = json.getJSONObject("album");
            int albumId = json.getInt("id");

            // 获取专辑封面路径
            json = NeteaseAPI.getAlbumDetail(Integer.toString(albumId));
            jsonArray = json.getJSONArray("songs");
            json = jsonArray.getJSONObject(0);
            json = json.getJSONObject("al");
            String coverPath = json.getString("picUrl");

            music.setId(musicId);
            music.setAlbumId(albumId);
            music.setCoverPath(coverPath);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getLyric(Music music) {
        try {
            if (music.getId() == null) {
                getInfo(music);
                if (music.getId() == null) {
                    return;
                }
            }
            List<LyricLine> lyric = null;
            JSONObject json = NeteaseAPI.getLyric(music.getId());
            if (json != null) {
                json = json.getJSONObject("lrc");
                lyric = new ArrayList<>();
                String lyricStr = json.getString("lyric");
                String[] lyricLines = lyricStr.split("\\n");
                for (String line : lyricLines) {
                    String[] mapStr = line.split("]");
                    if (!mapStr[0].startsWith("["))  continue;
                    mapStr[0] = mapStr[0].substring(1);
                    long time = resolveTime(mapStr[0]);
                    lyric.add(new LyricLine(time, mapStr.length > 1 ? mapStr[1] : ""));
                }
            }
            music.setLyric(lyric);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static long resolveTime(String time) {
        String[] mapStr = time.split(":");
        int min = Integer.parseInt(mapStr[0]);
        double sec = Double.parseDouble(mapStr[1]);
        return (long) ((min * 60 + sec) * 1000);
    }

	public User getuser(String userNumber, String password) {
		User user = new User();			
			try {
				JSONObject jsonObject = NeteaseAPI.login(userNumber, password);
				jsonObject= jsonObject.getJSONObject("profile");
				user.setUserId(jsonObject.getString("userId"));					
				user.setAvatarUrl(HttpUtil.requestBitmap(jsonObject.getString("avatarUrl"), HttpUtil.METHOD_GET));				
				user.setStatus(user.status(jsonObject.getInt("authStatus")));				
				user.setNickName(jsonObject.getString("nickname"));	
				return user;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return user;

	}

	public User getUserInformation(String userId) {
		// TODO Auto-generated method stub
		User user = new User();
		Date date;
		try {
			JSONObject jsonObject = NeteaseAPI.getInformation(userId);
			jsonObject = jsonObject.getJSONObject("profile");
			user.setUserId(jsonObject.getString("userId"));					
			user.setAvatarUrl(HttpUtil.requestBitmap(jsonObject.getString("avatarUrl"), HttpUtil.METHOD_GET));					
			user.setBackgroundImgSrc(HttpUtil.requestBitmap(jsonObject.getString("backgroundUrl"), HttpUtil.METHOD_GET));			
			date = new Date(jsonObject.getLong("birthday"));				
			user.setBirthday(date);
			user.setSignature(jsonObject.getString("signature"));
			user.setStatus(user.status(jsonObject.getInt("authStatus")));					
			user.setNickName(jsonObject.getString("nickname"));
			user.setFolloweds(jsonObject.getInt("followeds"));
			user.setFollows(jsonObject.getInt("follows"));
			user.setAge(user.age(date));					
			user.setSex(user.sex(jsonObject.getInt("gender")));
			user.setCity(user.city(jsonObject.getInt("city")));
			return user;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	public List<User> getMyAttention(String userId) {
		// TODO Auto-generated method stub
		List<User> users = new ArrayList<User>();
		User user = new User();		
		try {
			JSONObject result = NeteaseAPI.getAttention(userId);
			JSONArray jsonArray = result.getJSONArray("follow");
			for(int i=0;i<jsonArray.length();i++){
				result = jsonArray.getJSONObject(i);
				user.setUserId(result.getString("userId"));					
				user.setAvatarUrl(HttpUtil.requestBitmap(result.getString("avatarUrl"), HttpUtil.METHOD_GET));				
				user.setStatus(user.status(result.getInt("authStatus")));				
				user.setNickName(result.getString("nickname"));
				users.add(user);
			}
			return users;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return users;
	}

}
