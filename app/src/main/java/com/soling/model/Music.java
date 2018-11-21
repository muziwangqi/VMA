package com.soling.model;

import android.text.TextUtils;

import java.util.List;

public class Music {

    private static final String TAG = "Music";

    private int id;
    private String name;        // 歌曲名
    private String album;       // 专辑名
    private Integer albumId;    // API中对应的专辑ID
    private String coverPath;   // 专辑封面对应的位置
    private Integer aId;         // API中对应的歌曲ID
    private List<LyricLine> lyric;       // 歌词
    private String path;        // 路径
    private String artist;      // 艺术家
    private long size;          // 文件大小
    private int duration;       // 时长
    private boolean like;
    private String localCoverPath;
    private String localLyricPath;


    private static final String[] QQ_SUFFIXS = {"[mqms2]", "[mqms]"};
    private static final String QQ_SPLIT = "-";

    public Music(int id, String name, String album, String path, String artist, long size, int duration) {

//        this.name = name;
        this.id = id;
        this.album = album;
        this.path = path;
        this.artist = artist;
        this.size = size;
        this.duration = duration;
        this.name = resolveName(name);
        this.like = false;
    }

    public Music() {
    }

    private String resolveName(String name) {
        String[] strs = name.split("[\\\\.](?=((?![\\\\.]).)*$)");
        name = strs[0];
        for (String suffix : QQ_SUFFIXS) {
            if (name.endsWith(suffix)) {
                String[] nameMap = name.split("-");
                if (TextUtils.isEmpty(artist)) {
                    artist = nameMap[0].trim();
                }
                name = nameMap[1].trim();
                name = name.substring(0, name.length() - 1 - suffix.length());
            }
        }
        String[] ss= name.split("-");
        name = ss[ss.length - 1];
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public Integer getAId() {
        return aId;
    }

    public void setAId(Integer aId) {
        this.aId = aId;
    }

    public List<LyricLine> getLyric() {
        return lyric;
    }

    public void setLyric(List<LyricLine> lyric) {
        this.lyric = lyric;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public int getId() {
        return id;
    }

    public String getLocalCoverPath() {
        return localCoverPath;
    }

    public void setLocalCoverPath(String localCoverPath) {
        this.localCoverPath = localCoverPath;
    }

    public String getLocalLyricPath() {
        return localLyricPath;
    }

    public void setLocalLyricPath(String localLyricPath) {
        this.localLyricPath = localLyricPath;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", album='" + album + '\'' +
                ", albumId=" + albumId +
                ", coverPath='" + coverPath + '\'' +
                ", aId=" + aId +
                ", lyric=" + lyric +
                ", path='" + path + '\'' +
                ", artist='" + artist + '\'' +
                ", size=" + size +
                ", duration=" + duration +
                ", like=" + like +
                ", localCoverPath='" + localCoverPath + '\'' +
                ", localLyricPath='" + localLyricPath + '\'' +
                '}';
    }

    public static Music findMusicById(List<Music> musics, int id) {
        for (Music music : musics) {
            if (music.getId() == id) {
                return music;
            }
        }
        return null;
    }

}
