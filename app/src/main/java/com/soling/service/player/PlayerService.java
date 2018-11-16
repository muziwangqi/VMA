package com.soling.service.player;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public class PlayerService extends Service {

    public static final String ACTION_PLAY_NEXT = "cn.hyw.www.imusic.action.PLAY_NEXT";
    public static final String ACTION_PLAY_LAST = "cn.hyw.www.imusic.action.PLAY_LAST";
    public static final String ACTION_PLAY_TOGGLE = "cn.hyw.www.imusic.action.PLAY_TOGGLE";

    private static final String TAG = "PlayerService";
    private final LocalBinder binder = new LocalBinder();

    private Player player;

    public class LocalBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new Player();

        new PlayerNotification(player);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (TextUtils.equals(action, ACTION_PLAY_LAST)) {
                player.playNext();
            }
            else if (TextUtils.equals(action, ACTION_PLAY_TOGGLE)) {
                if (player.isPlaying()) player.pause();
                else player.play();
            }
            else if (TextUtils.equals(action, ACTION_PLAY_NEXT)) {
                player.playNext();
            }
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
    }

    public IPlayer getPlayer() {
        return player;
    }

}
