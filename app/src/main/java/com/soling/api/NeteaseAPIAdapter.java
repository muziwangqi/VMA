package com.soling.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    public List<Music> search(String...args) {
        List<Music> musics = new ArrayList<>();
        try {
            // 获取专辑ID和音乐ID
            JSONObject root = NeteaseAPI.search(args);
            if (root == null) return null;
            JSONObject result = root.getJSONObject("result");
            JSONArray songs = result.getJSONArray("songs");
            for (int i = 0; i < songs.length(); i++) {
                Music music = new Music();
                JSONObject song = songs.getJSONObject(i);
                int musicId = song.getInt("id");
                String musicName = song.getString("name");

                JSONArray artists = song.getJSONArray("artists");
                JSONObject artist = artists.getJSONObject(0);
                String artistName = artist.getString("name");

                JSONObject album = song.getJSONObject("album");
                int albumId = album.getInt("id");

                music.setAId(musicId);
                music.setAlbumId(albumId);
                music.setName(musicName);
                music.setArtist(artistName);
                musics.add(music);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return musics;
    }

    @Override
    public String getCoverPath(int albumId) {
        try {
            JSONObject cRoot = NeteaseAPI.getAlbumDetail(albumId);
            JSONArray cSongs = cRoot.getJSONArray("songs");
            JSONObject cSong = cSongs.getJSONObject(0);
            JSONObject al = cSong.getJSONObject("al");
            return al.getString("picUrl");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getLyric(Integer aId) {
        String lyric = null;
        try {
            JSONObject json = NeteaseAPI.getLyric(aId);
            if (json != null) {
                json = json.getJSONObject("lrc");
                lyric = json.getString("lyric");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lyric;
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

    @Override
    public List<String> searchHot() {
        List<String> hots = new ArrayList<>();
        try {
            JSONObject json = NeteaseAPI.searchHot();
            if (json == null) return hots;
            json = json.getJSONObject("result");
            JSONArray jsonArray = json.getJSONArray("hots");
            for (int i = 0; i < jsonArray.length(); i++) {
                json = jsonArray.getJSONObject(i);
                hots.add(json.get("first").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hots;
    }

}
