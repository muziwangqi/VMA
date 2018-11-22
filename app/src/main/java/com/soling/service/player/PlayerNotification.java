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

import com.soling.App;
import com.soling.R;
import com.soling.model.LyricLine;
import com.soling.model.Music;
import com.soling.view.activity.MainActivity;

public class PlayerNotification implements IPlayer.Observer {

    public static final String CHANEL_ID = "CHANEL_PLAY";
    public static final int NOTIFICATION_ID = 2;

    private NotificationCompat.Builder builder;
    private RemoteViews remoteViews;
    private Player player;

    public PlayerNotification(Player player) {
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
        } else {
            remoteViews.setImageViewResource(R.id.ib_play_toggle, R.drawable.ic_notification_play_arrow);
        }
        show();
    }

    public void refreshCover(Bitmap cover) {
        if (cover == null) {
            remoteViews.setImageViewResource(R.id.iv_album_cover, R.drawable.player_cover_default);
        } else {
            remoteViews.setImageViewBitmap(R.id.iv_album_cover, cover);
        }
        show();
    }

    private void show() {
        NotificationManager manager = (NotificationManager) App.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
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
        Context context = App.getInstance();
        PendingIntent playLastIntent = PendingIntent.getService(context, 0,
                new Intent(context, PlayerService.class).setAction(PlayerService.ACTION_PLAY_LAST), 0);
        PendingIntent playToggleIntent = PendingIntent.getService(context, 0,
                new Intent(context, PlayerService.class).setAction(PlayerService.ACTION_PLAY_TOGGLE), 0);
        PendingIntent playNextIntent = PendingIntent.getService(context, 0,
                new Intent(context, PlayerService.class).setAction(PlayerService.ACTION_PLAY_NEXT), 0);

        PendingIntent intent = PendingIntent.getActivity(context, 0
                , new Intent(context, MainActivity.class), 0);

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.remote_play_notifaction);

        remoteViews.setOnClickPendingIntent(R.id.ib_play_last, playLastIntent);
        remoteViews.setOnClickPendingIntent(R.id.ib_play_toggle, playToggleIntent);
        remoteViews.setOnClickPendingIntent(R.id.ib_play_next, playNextIntent);
        remoteViews.setImageViewResource(R.id.iv_album_cover, R.drawable.player_cover_default);

        builder = new NotificationCompat.Builder(context, CHANEL_ID);
        builder.setSmallIcon(R.drawable.ic_music_note)
                .setSound(null)
                .setContent(remoteViews)
                .setOngoing(true)
                .setContentIntent(intent);
    }

    @Override
    public void onPlayChange() {
        refreshCover(null);
        refreshText();
        refreshButton();
    }

    @Override
    public void onPlayToggle() {
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
