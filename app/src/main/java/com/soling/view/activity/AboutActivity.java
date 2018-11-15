package com.soling.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.soling.R;

public class AboutActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_about);
	}
}
