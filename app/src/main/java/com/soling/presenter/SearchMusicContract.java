package com.soling.presenter;

import android.content.Context;

import java.util.List;

import com.soling.model.Music;

public interface SearchMusicContract {

    interface View {

        void showHots(List<String> hots);

        void showSearchResult(List<Music> localMusic, List<Music> netMusics);

    }

    interface Presenter {

        void searchHot();

        void search(String s);

        void bindPlayerService(Context context);

        void unbindPlayerService(Context context);

        void playLocal(int position);

        void playNetworK(int position);

        List<Music> searchLocal(String s);

        List<String> getSearchHistory();

        void saveSearchRecord(String s);

        void saveToSharedPreferences();
    }

}

