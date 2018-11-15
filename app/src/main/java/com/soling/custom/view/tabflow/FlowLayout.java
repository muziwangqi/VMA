package com.soling.custom.view.tabflow;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {

    private static final String TAG = "FlowLayout";
    private List<List<View>> views = new ArrayList<List<View>>();
    List<Integer> lineHeights = new ArrayList<Integer>();

    private int width;
    private int height;
    private boolean isFirstMeasure = true;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure: ");
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightModel = MeasureSpec.getMode(heightMeasureSpec);
        
        if (isFirstMeasure) {
            width = widthMode == MeasureSpec.EXACTLY ? widthSize : 0;
            height = 0;
            int lineWidth = 0;
            int lineHeight = 0;

            List<View> lineViews = new ArrayList<View>();
            int cCount = getChildCount();


            for (int i = 0; i < cCount; i++) {
                View cView = getChildAt(i);
                measureChild(cView, widthMeasureSpec, heightMeasureSpec);
                MarginLayoutParams mp = (MarginLayoutParams) cView.getLayoutParams();
                int cWidth = cView.getMeasuredWidth() + mp.leftMargin + mp.rightMargin;
                int cHeight = cView.getMeasuredHeight() + mp.topMargin + mp.bottomMargin;
                Log.d(TAG, "onMeasure: cWidth" + cWidth);

                if (lineWidth + cWidth > width - getPaddingLeft() - getPaddingRight()) {
                    width = Math.max(width, lineWidth);
                    height += lineHeight;

                    views.add(lineViews);
                    lineHeights.add(lineHeight);

                    lineWidth = cWidth;
                    lineHeight = cHeight;
                    lineViews = new ArrayList<>();
                }
                else {
                    lineWidth += cWidth;
                    lineHeight = Math.max(lineHeight, cHeight);
                }
                lineViews.add(cView);
                if (i == cCount - 1) {
                    height += lineHeight;
                    lineHeights.add(lineHeight);
                    views.add(lineViews);
                }
            }
            Log.d(TAG, "onMeasure: " + lineHeights.toString());

            Log.d(TAG, "onMeasure: height" + height);
            isFirstMeasure = false;
        }

        setMeasuredDimension(
                widthMode == MeasureSpec.EXACTLY ? widthSize : width + getPaddingLeft() + getPaddingRight(),
                heightModel == MeasureSpec.EXACTLY ? heightSize : height + getPaddingTop() + getPaddingBottom());;

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int top = getPaddingTop();

        for (int i = 0; i < views.size(); i++) {
            int left = getPaddingLeft();
            List<View> lineViews = views.get(i);
            for (int j = 0; j < lineViews.size(); j++) {
                View cView = lineViews.get(j);
                MarginLayoutParams mp = (MarginLayoutParams) cView.getLayoutParams();
                int lc = left + mp.leftMargin;
                int tc = top + mp.topMargin;
                int rc = lc + cView.getMeasuredWidth();
                int bc = tc + cView.getMeasuredHeight();
                cView.layout(lc, tc, rc, bc);
                left = rc + mp.rightMargin;
            }
            top += lineHeights.get(i);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
