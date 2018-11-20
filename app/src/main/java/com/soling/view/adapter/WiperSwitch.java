package com.soling.view.adapter;

import com.soling.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class WiperSwitch extends View implements View.OnTouchListener {

	private Bitmap bitmapOn,bitmapOff,slipbtn;
	private float currentX,pressedX;
	private boolean onslip=false;//滑动
	private boolean currentStatus;
	private IOnChangedListener onChangedListener;
	
	public WiperSwitch(Context context) {
		super(context);
		importPicture();
	}

	public WiperSwitch(Context context,AttributeSet attrs) {
		super(context,attrs);
		importPicture();
	}

	public void importPicture(){
		bitmapOn=BitmapFactory.decodeResource(getResources(), R.drawable.kai);
		bitmapOff=BitmapFactory.decodeResource(getResources(), R.drawable.guan);
		slipbtn=BitmapFactory.decodeResource(getResources(), R.drawable.slip);
		
		/*int widthOn=bitmapOn.getWidth();
		int heightOn=bitmapOn.getHeight();
		int widthOff=bitmapOff.getWidth();
		int heightOff=bitmapOff.getHeight();
		int newWidthOn=75,newWidthOff=75;
		int newHeightOn=40,newHeightOff=40;
		System.out.println(widthOn+"  2  "+heightOn+"  3  "+widthOff+" 4 "+heightOff);
		float scaleWidth=(float)newWidthOn/widthOn;
		float scaleHeight=(float)newHeightOn/heightOn;
		Matrix m=new Matrix();
		m.postScale(scaleWidth, scaleHeight);*/
		
		setOnTouchListener(this);
	}
	
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		Matrix matrix=new Matrix();
		Paint paint=new Paint();
		float x=0;
		if (currentX<(bitmapOn.getWidth()/2)) {
			canvas.drawBitmap(bitmapOff, matrix, paint);
		}else {
			canvas.drawBitmap(bitmapOn, matrix, paint);
		}

		if (onslip==true) {
			if (currentX>=(bitmapOn.getWidth())) {
				x=bitmapOn.getWidth()-slipbtn.getWidth()/2;
			}else{
				x=currentX-slipbtn.getWidth()/2;
			}
		}else{
			if (currentStatus==true) {
				x=bitmapOn.getWidth()-slipbtn.getWidth();
			}else{
				x=0;
			}
		}
		if (x<0) {
			x=0;
		}else if (x>bitmapOn.getWidth()-slipbtn.getWidth()) {
			x=bitmapOn.getWidth()-slipbtn.getWidth();
		}

		canvas.drawBitmap(slipbtn, x, 0,paint);
	}
	
	public boolean onTouch(View view, MotionEvent motionEvent) {
		switch (motionEvent.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (motionEvent.getX()>bitmapOff.getWidth()) {
				return false;
			}else {
				onslip=true;
				currentX=pressedX=motionEvent.getX();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			currentX=motionEvent.getX();
			break;
		case MotionEvent.ACTION_UP:
			onslip=false;
			if (motionEvent.getX()>=(bitmapOn.getWidth()/2)) {
				currentStatus=true;
				currentX=motionEvent.getX();
			}else{
				currentStatus=false;
				currentX=0;
			}
			if (onChangedListener!=null) {
				onChangedListener.onChange(this, currentStatus);
			}
			break;
		case MotionEvent.ACTION_CANCEL://避免卡在中间
			onslip=false;
			if (motionEvent.getX()>=(bitmapOn.getWidth()/2)) {
				currentStatus=true;
				currentX=motionEvent.getX();
			}else{
				currentStatus=false;
				currentX=0;
			}
			if (onChangedListener!=null) {
				onChangedListener.onChange(this, currentStatus);
			}
			break;
		}
		invalidate();//
		return true;
	}

	public void setOnChangedListener(IOnChangedListener onChangedListener) {
		this.onChangedListener = onChangedListener;
	}

	public void setChecked(boolean checked) {
		if (checked) {
			currentX=bitmapOff.getWidth();
		}else{
			currentX=0;
		}
	}

	public interface IOnChangedListener {
		public void onChange(WiperSwitch wiperSwitch,boolean checkStat);
	}
}
