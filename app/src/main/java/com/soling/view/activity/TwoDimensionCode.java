package com.soling.view.activity;

import java.util.List;

import com.soling.presenter.CodePresenter;
import com.soling.R;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class TwoDimensionCode extends Activity {
	String uid = "1660095144";
	Bitmap bmp ;
	private ImageView myTwoDimensionCode;	
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.code);
	myTwoDimensionCode = findViewById(R.id.code);
	bmp = BitmapFactory.decodeResource(getResources(),
		    R.drawable.ic_launcher);
	bmp = CodePresenter.makeQRImage(bmp, uid, 400, 400);
	myTwoDimensionCode.setImageBitmap(bmp);
}
}
