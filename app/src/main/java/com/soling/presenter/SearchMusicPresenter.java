package com.soling.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.soling.App;
import com.soling.api.MusicAPIFactory;
import com.soling.model.Music;
import com.soling.service.player.IPlayer;
import com.soling.service.player.PlayerService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class SearchMusicPresenter implements SearchMusicContract.Presenter {

    private static final String TAG = "SearchMusicPresenter";

    private static final String PREFERENCE_FILE = "data";

    private static final String PREFERENCE_SEARCH_RECORD = "search_history";
    private static final int MAX_RECORD = 10;

    private SearchMusicContract.View view;

    private IPlayer player;
    private List<Music> localResult = new ArrayList<>();
    private List<Music> networkResult = new ArrayList<>();

    private List<String> searchHistory = new LinkedList<>();

    public SearchMusicPresenter(SearchMusicContract.View view) {
        this.view = view;
        searchHistory.addAll(readFromSharedPreferences());
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
            player = binder.getService().getPlayer();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void searchHot() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> hots = MusicAPIFactory.getMusicAPI().searchHot();
                view.showHots(hots);
            }
        }).start();
    }

    @Override
    public void search(final String s) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                localResult = searchLocal(s);
                networkResult = searchNetWork(s);
                view.showSearchResult(localResult, networkResult);
            }
        }).start();
    }

    @Override
    public void bindPlayerService(Context context) {
        context.bindService(new Intent(context,PlayerService.class), connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void unbindPlayerService(Context context) {
        context.unbindService(connection);
    }

    @Override
    public void playLocal(int position) {
        player.play(localResult.get(position));
    }

    @Override
    public void playNetworK(int position) {

    }

    @Override
    public List<Music> searchLocal(String s) {
        List<Music> result = new ArrayList<>();
        List<Music> localMusics = App.getInstance().getLocalMusics().getMusics();
        if (localMusics == null || localMusics.size() == 0) return null;
        for (int i = 0; i < localMusics.size(); i++) {
            Music music = localMusics.get(i);
            if (music.getName().contains(s) || music.getAlbum().contains(s) || music.getArtist().contains(s)) {
                result.add(music);
            }
        }
        return result;
    }

    private List<Music> searchNetWork(String s) {
//        return MusicAPIFactory.getMusicAPI().search(s);
        return null;
    }

    @Override
    public List<String> getSearchHistory() {
        return searchHistory;
    }

    @Override
    public void saveSearchRecord(String s) {
        if (searchHistory.size() >= MAX_RECORD) {
            searchHistory.remove(searchHistory.size() - 1);
        }
        searchHistory.remove(s);
        searchHistory.add(0, s);

        saveToSharedPreferences();

        Log.d(TAG, "saveSearchRecord: " + new LinkedHashSet<>(searchHistory).toString());
    }

    @Override
    public void saveToSharedPreferences() {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < searchHistory.size(); i++) {
            set.add(Integer.toString(i) + "_" + searchHistory.get(i));
        }
        SharedPreferences.Editor edit = App.getInstance().getSharedPreferences(PREFERENCE_FILE, MODE_PRIVATE).edit();
        edit.putStringSet(PREFERENCE_SEARCH_RECORD, set);
        edit.apply();
    }

    private List<String> readFromSharedPreferences() {
        List<String> result = new LinkedList<>();
        SharedPreferences pref = App.getInstance().getSharedPreferences(PREFERENCE_FILE, MODE_PRIVATE);
        result.addAll(Objects.requireNonNull(pref.getStringSet(PREFERENCE_SEARCH_RECORD, new HashSet<String>())));
        Collections.sort(result);
        for (int i = 0; i < result.size(); i++) {
            result.set(i, result.get(i).split("_")[1]);
        }
        return result;
    }

}

