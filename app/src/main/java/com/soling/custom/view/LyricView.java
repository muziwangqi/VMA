package com.soling.custom.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.soling.model.LyricLine;

public class LyricView extends View {

    private static final String TAG = "LyricView";
    private static final int MARGIN_BOTTOM = 32; // dp
    private static final int TEXT_SIZE = 16;    // sp

    private List<LyricLine> lyric;
    private List<StaticLayout> staticLayouts;
    private TextPaint textPaint = new TextPaint();
    private int curLine = 0;
    private boolean isTouching = false;
    private boolean stopAutoScroll = false;
    private Timer touchEndTimer = new Timer();
    private float lastY = 0;
    private float lastX = 0;

    public LyricView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "LyricView: " + "create completed !");
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE, getResources().getDisplayMetrics()));
        textPaint.setTextAlign(Paint.Align.LEFT);
        staticLayouts = new ArrayList<StaticLayout>();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerY = getHeight() / 2;

        float offset = 0;
        float marginBottom = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, MARGIN_BOTTOM, getResources().getDisplayMetrics());
        for (int i = curLine; i >= 0; i--) {
            offset += staticLayouts.get(i).getHeight() + marginBottom;
        }
        float y = 0 - offset + centerY;
        for (int i = 0; i < staticLayouts.size(); i++) {
            if (i != 0) {
                y += staticLayouts.get(i).getHeight() + marginBottom;
            }
            canvas.save();
            if (i == curLine) {
                textPaint.setColor(Color.BLACK);
            }
            else {
                textPaint.setColor(Color.GRAY);
            }
            drawText(canvas, staticLayouts.get(i), y);
            canvas.restore();
        }
    }

    private void drawText(Canvas canvas, StaticLayout staticLayout, float y) {
        canvas.translate(0, y);
        staticLayout.draw(canvas);
    }

    public void setLyric(List<LyricLine> lyric) {
        this.lyric = lyric;
        if (this.lyric == null) {
            reset();
        }
        else {
            staticLayouts.clear();
            for (int i = 0; i < this.lyric.size(); i++) {
                staticLayouts.add(new StaticLayout(lyric.get(i).getText(), textPaint, getWidth(), Layout.Alignment.ALIGN_CENTER, 1f, 0f, false));
            }
        }
        if (getVisibility() == View.VISIBLE) {
            this.invalidate();
        }
    }

    public void refresh(long progress) {
        if (lyric == null) {
            if (stopAutoScroll) return;
            reset();
            return;
        }
        int newLine = findLine(progress);
        if (curLine != newLine) {
            curLine = newLine;
            if (stopAutoScroll) return;
            this.invalidate();
        }
    }

    public void reset() {
        lyric = null;
        staticLayouts.clear();
        staticLayouts.add(new StaticLayout("暂无歌词", textPaint, getWidth(), Layout.Alignment.ALIGN_CENTER, 1f, 0f, false));
        curLine = 0;
        this.invalidate();
    }

    private int findLine(long progress) {
        int left = 0, right = staticLayouts.size(), mid;
        long endTime = lyric.get(lyric.size() - 1).getTimestamp();
        if (progress > endTime) return lyric.size() - 1;
        while (left < right - 1) {
            mid = (left + right) / 2;
            long timestamp = lyric.get(mid).getTimestamp();
            if (timestamp < progress) {
                left = mid;
            }
            else if (timestamp > progress) {
                right = mid;
            }
            else {
                return mid;
            }
        }
        return left;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                Log.d(TAG, "onTouchEvent: " + y);
                float offsetY = y - lastY;
                if (Math.abs(offsetY) > 0) {
                    isTouching = true;
                    stopAutoScroll = true;
                    lastY = y;
                    scrollBy(0, -(int) offsetY);

                    if (touchEndTimer != null) {
                        touchEndTimer.cancel();
                    }
                }
                break;
            case  MotionEvent.ACTION_UP:
                if (isTouching) {
                    isTouching = false;
                    touchEndTimer = new Timer();
                    touchEndTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            stopAutoScroll = false;
                            scrollTo(0, 0);
                        }
                    }, 2000);
                }
                else {
                    Log.d(TAG, "onTouchEvent: " + "click");
                    callOnClick();
                }
                break;

        }
        return true;

    }

}
