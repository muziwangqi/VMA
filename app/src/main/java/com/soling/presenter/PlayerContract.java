package com.soling.presenter;

import android.graphics.Bitmap;

import java.util.List;

import com.soling.model.LyricLine;
import com.soling.service.player.IPlayer;

public interface PlayerContract {

    interface View {

        void onPlayServiceBound();

        void refreshView();

        void refreshCover(Bitmap cover);

        void refreshLyric(List<LyricLine> lyric);

    }

    interface Presenter extends IPlayer {

        void bindPlayService();

        void unbindPlayService();

    }

}
