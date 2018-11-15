package com.soling.view.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.soling.view.fragment.SettingFragment;
import com.soling.R;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Banner2Activity extends Activity implements OnClickListener {
	private Button enterButtn;
	private TextView textView;
	private LinearLayout linearLayout;
	private RadioGroup radioGroup;
	private List<ImageView> pointList;// point
	private List<ImageView> list;
	private int count = 0;
	private int[] pictures = { R.drawable.banner_a, R.drawable.banner_b,
			R.drawable.banner_c, R.drawable.banner_d };// picture
	private ViewPager viewPager;
	private SharedPreferences sharedPreferences;
	private Editor editor;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			count++;
			viewPager.setCurrentItem(count);
			sendEmptyMessageDelayed(0, 1000);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_banner2);
		isFirst();
		initView();
		initData();
		BannerAdapter bannerAdapter = new BannerAdapter();
		viewPager.setAdapter(bannerAdapter);
		// add point
		addPoint();
		enterButtn.setOnClickListener(this);
		handler.sendEmptyMessageDelayed(0, 1000);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				int index = arg0 % list.size();
				for (int i = 0; i < pictures.length; i++) {
					if (index == 1) {
						pointList.get(i).setImageResource(R.drawable.point);
					} else {
						pointList.get(i).setImageResource(R.drawable.point);
					}
				}
				if (index == pictures.length - 1) {
					enterButtn.setVisibility(View.VISIBLE);// ���һ��ͼƬʱ��ť�ɼ�
				} else {
					enterButtn.setVisibility(View.GONE);
				}
			}

			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

		});

		// 请求权限 hyw
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
		}

	}

	private void addPoint() {
		linearLayout = (LinearLayout) findViewById(R.id.id_ll_linearlayout);
		pointList = new ArrayList<ImageView>();
		for (int i = 0; i < pictures.length; i++) {
			ImageView imageView = new ImageView(this);
			if (i == 0) {
				imageView.setImageResource(R.drawable.point);
			} else {
				imageView.setImageResource(R.drawable.point);
			}
			pointList.add(imageView);
			linearLayout.addView(imageView);
		}
	}

	private void isFirst() {
		// ��ȡactivity��״̬ ���Ƿ��ǵ�һ�ν���ҳ��
		sharedPreferences = getSharedPreferences("state", MODE_PRIVATE);
		if (sharedPreferences.getBoolean("isFirst", false)) {
			Intent intent = new Intent(this, SettingFragment.class);
			startActivity(intent);
			finish();// ����activity����������
		}
	}

	private void initView() {
		enterButtn = (Button) findViewById(R.id.id_button_enter);
		viewPager = (ViewPager) findViewById(R.id.id_vp_banner2);
	}

	private void initData() {
		list = new ArrayList<ImageView>();
		for (int i = 0; i < pictures.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setImageResource(pictures[i]);
			list.add(imageView);
		}
	}

	public void onClick(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onStop() {
		// ��תҳ��ʱ message��������activity�رն�ֹͣ���ֶ�ֹͣ
		if (handler != null) {
			handler.removeMessages(0);
		}
		super.onStop();
	}

	class BannerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			int index = position % list.size();
			container.addView(list.get(index));
			return list.get(index);
		}
	}

}
