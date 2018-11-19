package com.soling.view.adapter;

import java.util.ArrayList;
import java.util.List;

import com.soling.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class TimePickerDialog extends Dialog implements OnTimeChangedListener {

	private Context context;
	private TimePicker timePicker;
	private OnTimeChangedListener onTimeChangedListener;
	private static boolean is24Hour = true;
	private AlertDialog.Builder alertDialogBuilder;
	private String hour = "00";
	private String minute = "00";
	private int hourI = 0;
	private int minuteI = 0;
	private static String[] selector = new String[] { "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
			"18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
			"29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
			"40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
			"51", "52", "53", "54", "55", "56", "57", "58", "59", "60" };

	public TimePickerDialog(Context context) {
		this(context, is24Hour, 0, 0);
	}

	public TimePickerDialog(Context context, boolean is24Hour, int hourI,
			int minuteI) {
		super(context);
		this.context = context;
		this.hourI = hourI;
		this.is24Hour = is24Hour;
		this.minuteI = minuteI;
		initTime();
	}

	private void initTime() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.layout_timeselector, null);
		timePicker = (TimePicker) view
				.findViewById(R.id.id_tp_hourminuteselector);
		timePicker.setIs24HourView(is24Hour);

		timePicker.setCurrentHour(hourI);
		timePicker.setCurrentMinute(minuteI);
		// 获取ȡviewGroup numberPicker
		NumberPicker minuteSpinner = findNumberPicker(timePicker).get(
				findNumberPicker(timePicker).size() - 1);
		minuteSpinner.setMaxValue(59);
		minuteSpinner.setMinValue(0);
		minuteSpinner.setDisplayedValues(selector);
		minuteSpinner.setOnLongPressUpdateInterval(1000);//
		timePicker.setOnTimeChangedListener(this);
		alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setView(view);
		alertDialogBuilder.setTitle("自定义停止播放");
		alertDialogBuilder.setPositiveButton("确定", new OnClickListener() {

			public void onClick(DialogInterface dialogInterface, int arg1) {
				if (onTimeChangedListener != null) {
					onTimeChangedListener.onTimeChanged(hour, minute);
				}
				int h=Integer.parseInt(hour);
				int m=Integer.parseInt(minute);
				final int time=h*3600000+m*60000;
				class Test{
					public void main(String[] args){
						Test test=new Test();
						MyThread myThread=test.new MyThread();
						myThread.start();
					}

					class MyThread extends Thread{
						@Override
						public void run() {
							super.run();
							try {
								Thread.currentThread().sleep(time);
							}catch (InterruptedException e){
								e.printStackTrace();
							}
						}
					}
				}
				Toast.makeText(context, hour + "小时" + minute + "分钟后",
						Toast.LENGTH_SHORT).show();
			}
		});
		alertDialogBuilder.setNegativeButton("取消", null);
		alertDialogBuilder.create().show();
	}

	private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
		List<NumberPicker> numberPickerList = new ArrayList<NumberPicker>();
		View child = null;
		if (null != viewGroup) {
			for (int i = 0; i < viewGroup.getChildCount(); i++) {
				child = viewGroup.getChildAt(i);
				if (child instanceof NumberPicker) {
					numberPickerList.add((NumberPicker) child);
				} else if (child instanceof LinearLayout) {
					List<NumberPicker> pickers = findNumberPicker((ViewGroup) child);
					if (pickers.size() > 0) {
						return pickers;
					}
				}
			}
		}

		return numberPickerList;

	}

	public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
		this.hour = hour < 10 ? "0" + hour : "" + hour;
		this.minute = selector[minute];
	}

	public void setOnTimeChangedListener(
			OnTimeChangedListener onTimeChangedListener) {
		this.onTimeChangedListener = onTimeChangedListener;
	}

	public interface OnTimeChangedListener{
		public void onTimeChanged( String hour, String minute);
	}

}
