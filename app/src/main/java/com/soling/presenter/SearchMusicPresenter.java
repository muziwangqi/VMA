package com.soling.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.soling.App;
import com.soling.api.NeteaseAPI;
import com.soling.model.Music;
import com.soling.service.player.IPlayer;
import com.soling.service.player.PlayerService;

import java.util.ArrayList;
import java.util.List;

public class SearchMusicPresenter implements SearchMusicContract.Presenter {

    private static final String TAG = "SearchMusicPresenter";

    private SearchMusicContract.View view;

    private IPlayer player;
    private List<Music> localResult = new ArrayList<>();
    private List<Music> networkResult = new ArrayList<>();

    public SearchMusicPresenter(SearchMusicContract.View view) {
        this.view = view;
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
                try {
                    List<String> hots = new ArrayList<>();
                    JSONObject json = NeteaseAPI.searchHot();
                    Log.d(TAG, "run: " + json);
                    if (json == null) return;
                    json = json.getJSONObject("result");
                    JSONArray jsonArray = json.getJSONArray("hots");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        json = jsonArray.getJSONObject(i);
                        hots.add(json.get("first").toString());
                    }
                    view.showHots(hots);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    private List<Music> searchLocal(String s) {
        List<Music> result = new ArrayList<>();
        List<Music> localMusics = App.getInstance().getLocalMusics();
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
        List<Music> result = new ArrayList<>();
//        try {
//            JSONObject json = NeteaseAPI.searchHot();
//            Log.d(TAG, "run: " + json);
//            if (json == null) return null;
//            json = json.getJSONObject("result");
//            JSONArray jsonArray = json.getJSONArray("hots");
//            for (int i = 0; i < jsonArray.length(); i++) {
//                json = jsonArray.getJSONObject(i);
//                result.add(json.get("first").toString());
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return result;
    }

}
