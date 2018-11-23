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
	//String uid = "1660095144";
	Bitmap bmp ;
	Bitmap bitmapBackground;
	private ImageView myTwoDimensionCode;	
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.code);
	myTwoDimensionCode = findViewById(R.id.code);
	Bundle myBundle = this.getIntent().getExtras();
	String myText  = myBundle.getString("phoneNumber");
	bmp = BitmapFactory.decodeResource(getResources(),
		    R.drawable.headphoto);
	bitmapBackground = BitmapFactory.decodeResource(getResources(),
			R.drawable.code);
	bmp = CodePresenter.makeQRImage(bmp, myText, 552, 508);
	myTwoDimensionCode.setImageBitmap(bmp);
}
}
