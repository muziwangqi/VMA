package com.soling.api;

import java.util.List;
import java.util.Map;

import com.soling.model.LyricLine;
import com.soling.model.Music;
import com.soling.model.User;



public interface MusicAPI {

    String getLyric(Integer aId);
    List<String> searchHot();
    List<Music> search(String...args);


    User getuser(String userNumber,String password);
    String getCoverPath(int albumId);
    User getUserInformation(String userId);
    List<User> getMyAttention(String userId);

}
