package com.soling.view.adapter;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

public class ShakeListener implements SensorEventListener {

	private static final int SPEED_SHAKE=3000;
	private static final int UPDATE_TIME=70;//时间间隔
	private SensorManager sensorManager;
	private Sensor sensor;
	private Context context;
	private float lastX;
	private float lastY;
	private float lastZ;
	private long lastTime;
	private OnShakeListener onShakeListener;
		
	public ShakeListener(Context context) {
		this.context=context;
//		this.onShakeListener = new OnShakeListener() {
//			
//			@Override
//			public void onShake() {
//				// TODO Auto-generated method stub
//				Toast.makeText(context, "heheheh", Toast.LENGTH_SHORT).show();
//			}
//		};

		start();
	}

	//start监测
	public void start() {
		sensorManager=(SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		if (sensorManager!=null) {
			sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		}
		if (sensor!=null) {
			sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
		}
	}

	public void stop(){
		sensorManager.unregisterListener(this);
	}
	

	public void setOnShakeListener(OnShakeListener listener){
		this.onShakeListener=listener;
	}
	

	public void onSensorChanged(SensorEvent sensorEvent) {
		long currentTime=System.currentTimeMillis();
		long timeDiff=currentTime-lastTime;
		if (timeDiff<UPDATE_TIME) {
			return;
		}
		lastTime=currentTime;
		float currentX=sensorEvent.values[0];
		float currentY=sensorEvent.values[1];
		float currentZ=sensorEvent.values[2];
		
		float xDiff=currentX-lastX;
		float yDiff=currentY-lastY;
		float zDiff=currentZ-lastZ;
		
		lastX=currentX;
		lastY=currentY;
		lastZ=currentZ;
		
		double speed=Math.sqrt(xDiff*xDiff+yDiff*yDiff+zDiff*zDiff)/timeDiff*10000;
		//System.out.println(speed+"-------------");
		
		if (speed>=SPEED_SHAKE) {
			onShakeListener.onShake();//null
		}
		
	}

	public interface OnShakeListener {
		public void onShake();
	}


	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
