package com.soling.service.player;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import java.util.List;

import com.soling.R;
import com.soling.model.LyricLine;
import com.soling.model.Music;

public class PlayerNotification implements IPlayer.Observer {

    public static final String CHANEL_ID = "CHANEL_PLAY";
    public static final int NOTIFICATION_ID = 2;

    private NotificationCompat.Builder builder;
    private RemoteViews remoteViews;
    private PlayerService player;

    public PlayerNotification(PlayerService player) {
        this.player = player;
        this.player.registerObserver(this);
        initMediaNotification();
    }

    public void refreshText() {
        Music music = player.getPlayingMusic();
        if (music != null) {
            remoteViews.setTextViewText(R.id.tv_name, music.getName());
            remoteViews.setTextViewText(R.id.tv_artist, music.getArtist());
            show();
        }
    }

    public void refreshButton() {
        if (player.isPlaying()) {
            remoteViews.setImageViewResource(R.id.ib_play_toggle, R.drawable.ic_notification_pause);
        }
        else {
            remoteViews.setImageViewResource(R.id.ib_play_toggle, R.drawable.ic_notification_play_arrow);
        }
        show();
    }

    public void refreshCover(Bitmap cover) {
        remoteViews.setImageViewBitmap(R.id.iv_album_cover, cover);
        show();
    }

    private void show() {
        NotificationManager manager = (NotificationManager) player.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder.setChannelId(CHANEL_ID);
            NotificationChannel channel = new NotificationChannel(CHANEL_ID, CHANEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setSound(null, null);
            if (manager != null)
                manager.createNotificationChannel(channel);
        }
        manager.notify(NOTIFICATION_ID, builder.build());
    }

    private void initMediaNotification() {
        PendingIntent playLastIntent = PendingIntent.getService(player, 0,
                new Intent(player, PlayerService.class).setAction(PlayerService.ACTION_PLAY_LAST), 0);
        PendingIntent playToggleIntent = PendingIntent.getService(player, 0,
                new Intent(player, PlayerService.class).setAction(PlayerService.ACTION_PLAY_TOGGLE), 0);
        PendingIntent playNextIntent = PendingIntent.getService(player, 0,
                new Intent(player, PlayerService.class).setAction(PlayerService.ACTION_PLAY_NEXT), 0);
        remoteViews = new RemoteViews(player.getPackageName(), R.layout.remote_play_notifaction);

        remoteViews.setOnClickPendingIntent(R.id.ib_play_last, playLastIntent);
        remoteViews.setOnClickPendingIntent(R.id.ib_play_toggle, playToggleIntent);
        remoteViews.setOnClickPendingIntent(R.id.ib_play_next, playNextIntent);
        remoteViews.setImageViewResource(R.id.iv_album_cover, R.drawable.record);

        builder = new NotificationCompat.Builder(player, CHANEL_ID);
        builder.setSmallIcon(R.drawable.ic_music_note)
                .setSound(null)
                .setContent(remoteViews)
                .setOngoing(true);
    }


    @Override
    public void onSkipNext() {
        refreshText();
        refreshButton();
    }

    @Override
    public void onSkipLast() {
        refreshText();
        refreshButton();
    }

    @Override
    public void onPlayStateChanged() {
        refreshText();
        refreshButton();
    }

    @Override
    public void onCoverLoad(Bitmap cover) {
        refreshCover(cover);
    }

    @Override
    public void onLyricLoad(List<LyricLine> lyric) {

    }

}
