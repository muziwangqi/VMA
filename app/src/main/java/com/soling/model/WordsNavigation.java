package com.soling.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class WordsNavigation extends View {
	/*
	 * 绘制的列表导航字幕
	 */
	private String[] words = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
	private Paint wordPaint;
	private Paint bgPaint;
	private int itemWidth;
	private int itemHeight;
	private int touchIndex=0;
	private int w;
	private int h;
	private float eventY;//滑动的高度
	private Canvas canvas;
	private float scaleWidth;//缩放离原始的宽度
	private int itemH;
	private onWordsChangeListener listener;
	/*
	 * 初始化画笔
	 */
	private void init() {
		// TODO Auto-generated method stub
		wordPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		wordPaint.setAntiAlias(true);
		//wordPaint.setColor(Color.parseColor("#FFE1FF"));
		wordPaint.setTextSize(30);
		wordPaint.setTypeface(Typeface.DEFAULT_BOLD);
		bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		//bgPaint.setColor(Color.parseColor("#FFE1FF"));
		bgPaint.setAntiAlias(true);
	}

	public WordsNavigation(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public WordsNavigation(Context context) {
		super(context);
		init();
	}
	public WordsNavigation(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		itemWidth=getMeasuredWidth();
		int height = getMeasuredHeight()-10;
		itemHeight = height/27;
		
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		for(int i=0;i<words.length;i++){
			if(touchIndex == i){
				//canvas.drawCircle(itemWidth/2, itemHeight*i+itemHeight/2, 23, bgPaint);
				wordPaint.setColor(Color.WHITE);
			}else{
				wordPaint.setColor(Color.BLUE);
			}
			Rect rect = new Rect();
			wordPaint.getTextBounds(words[i], 0, 1, rect);
			int wordwidth = rect.width();
			float wordX = itemWidth/2+wordwidth/2;
			float wordY = itemHeight*i+itemWidth/2;
			canvas.drawText(words[i], wordX, wordY, wordPaint);
	}
	}
	/*
	 * (non-Javadoc)
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 * 当手指触摸按下的时候改变字母背景颜色
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub		
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				//获取当前触摸到的字母索引
				int index = (int)(event.getY()/itemHeight);
				if(index!=touchIndex){
					touchIndex = index;
				}
				if(listener != null && touchIndex>=0 && touchIndex<=words.length-1){
					listener.wordsChange(words[touchIndex]);
				}
				invalidate();
				break;
		case MotionEvent.ACTION_UP:
					break;
		}
		return super.onTouchEvent(event);
	}


	/*
	 * 设置当前按下的是哪个字母
	 */
	public void touchIndex(String word){
		for(int i=0;i<words.length;i++){
			if(word.equals(words[i])){
				touchIndex = i;
				invalidate();
				return ;
			}
		}
	}



	/*
	 * 手指按下了哪个字母的回掉接口
	 */
	public interface onWordsChangeListener {
		void wordsChange(String words);
	}
	/*
	 * 手指按下改变监听
	 */
	public void setOnWordsChangeListener(onWordsChangeListener listener) {
		this.listener = listener;
	}
}

