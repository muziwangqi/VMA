package com.soling.view.adapter;

import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class MyOnPageChangeAdapter implements OnPageChangeListener {

	private int one;
	private int offset=0;
	private int currentPageIndex=0;
	private int two;
	
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	public void onPageSelected(int i) {
		Animation animation=null;
		switch (i) {
		case 0:
			animation=new TranslateAnimation(one, 0, 0, 0);
			break;
		case 1:
			animation=new TranslateAnimation(offset, 0, 0, 0);
			break;
		case 2:
			animation=new TranslateAnimation(two, 0,0,0);
			break;
		}
		currentPageIndex=i;
		animation.setFillAfter(true);
	}

}
