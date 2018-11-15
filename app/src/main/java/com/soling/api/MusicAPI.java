package com.soling.api;

import java.util.List;

import com.soling.model.Music;
import com.soling.model.User;



public interface MusicAPI {

    // 获取音乐文件在api中对应的id，albumId,专辑封面路径coverPath
    void getInfo(Music music);
    void getLyric(Music music);
    User getuser(String userNumber,String password);
    User getUserInformation(String userId);
    List<User> getMyAttention(String userId);
}
