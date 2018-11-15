package com.soling.view.activity;

import com.soling.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class AboutActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_about);
	}
}
