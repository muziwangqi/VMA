package com.soling.view.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;

public class MediaButtonReceiver extends BroadcastReceiver {

	private long firstClickTime=0;
	private long secondCliclTime=0;
	private static Context context;
	private static final int mes_click_time=2;
	private static int clickNumber=0;
	private static long saveFirstTime=0;
	
	private static Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case mes_click_time:
				if (clickNumber==0) {
					//������ͣ����
					Toast.makeText(context, "������ͣ", Toast.LENGTH_SHORT).show();
				}else if(clickNumber==1){
					//˫����һ��
					Toast.makeText(context, "˫����һ��", Toast.LENGTH_SHORT).show();
				}else if(clickNumber==2){
					//������һ��
					Toast.makeText(context, "������һ��", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		};
	};
	
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context=context;
		//���ն�
		String intentAction=intent.getAction();
		//��ζ���
		if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intentAction)) {
			//zantingbifang
			Toast.makeText(context, "��ζ�����ͣ����", Toast.LENGTH_SHORT).show();
		}else if(Intent.ACTION_CALL_BUTTON.equals(intentAction)){
			KeyEvent keyEvent=intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
			int action=keyEvent.getAction();
			if (action==KeyEvent.ACTION_DOWN) {
				click();
			}
		}
	}

	private void click() {
		secondCliclTime=saveFirstTime;
		firstClickTime=System.currentTimeMillis();
		saveFirstTime=firstClickTime;
		long time=firstClickTime-secondCliclTime;
		int number=clickNumber;
		if (time>0&&time<700) {
			handler.removeMessages(mes_click_time);
			if (clickNumber==0) {
				clickNumber=1;
			}else if (number==1) {
				clickNumber=2;
			}else if (number==2) {
				clickNumber=0;
			}
		}else {
			clickNumber=0;
		}
		handler.sendEmptyMessageDelayed(mes_click_time, 900L);
	}

}
