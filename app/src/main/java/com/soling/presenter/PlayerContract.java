package com.soling.presenter;

import android.graphics.Bitmap;

import java.util.List;

import com.soling.model.LyricLine;
import com.soling.model.Music;
import com.soling.model.PlayList;
import com.soling.service.player.Player;

public interface PlayerContract {

    interface View {

        void onPlayServiceBound();

        void refreshView();

        void refreshPlayState();

        void refreshLike(boolean like);

        void loadCover(Bitmap cover);

        void loadLyric(List<LyricLine> lyric);

        void changeModel();

    }

    interface Presenter {

        void bindPlayService();

        void unbindPlayService();

        void play(PlayList playList, int startIndex);

        void pause();

        void resume();

        void playNext();

        void playLast();

        void seekTo(int progress);

        boolean isPlaying();

        int getProgress();

        Music getPlayingMusic();

        void changeModel();

        Player.Model getModel();

        void delete(List<Music> musicList, int position);

        void likeToggle(Music music);

        int getMaxVolume();

        int getVolume();

        void setVolume(int progress);

        void loadLyric();

        void loadCover();
    }

}
