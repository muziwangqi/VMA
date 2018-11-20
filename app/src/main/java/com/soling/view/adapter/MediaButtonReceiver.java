package com.soling.view.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.soling.App;
import com.soling.service.player.PlayerService;

public class MediaButtonReceiver extends BroadcastReceiver {

    private static final String TAG = "MediaButtonReceiver";

    private Handler handler = new Handler();
    private static int headSetHookNum = 0;

    private static Runnable taskPlayToggle = new Runnable() {
        @Override
        public void run() {
            Context context = App.getInstance();
            if (headSetHookNum == 1) {
                Intent intent = new Intent(context, PlayerService.class);
                intent.setAction(PlayerService.ACTION_PLAY_TOGGLE);
                context.startService(intent);
            }
            else {
                Intent intent = new Intent(context, PlayerService.class);
                intent.setAction(PlayerService.ACTION_PLAY_NEXT);
                context.startService(intent);
            }
            headSetHookNum = 0;
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        if (Intent.ACTION_HEADSET_PLUG.equals(intentAction)) {
            Log.d(TAG, "onReceive: " + "ACTION_AUDIO_BECOMING_NOISY");
        }
        else if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            KeyEvent keyEvent = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            int keyCode = keyEvent.getKeyCode();
            switch (keyCode) {
                case KeyEvent.KEYCODE_HEADSETHOOK:
                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) return;
                    if (headSetHookNum == 0) {
                        handler.postDelayed(taskPlayToggle, 1000);
                    }
                    headSetHookNum++;
                    break;
            }
        }
    }

}
