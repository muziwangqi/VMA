package com.soling.view.activity;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.soling.model.User;
import com.soling.R;


import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class InformationActivity extends Activity implements OnClickListener  {
	private ImageView image;
	private View view;
	private TextView pic;
	private TextView cancel;
	private TextView camera;
	private View inflate;
	private Dialog dialog;
	private Bitmap head;
	private static String path="/sdcard/demohead/";
	private int SELECT_PICTURE=0x00;
	private int SELECT_CAMER=0x01;
	Bitmap bitmap;
	private Button update;
	private Button send;
	private User user;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	initView();
	update = findViewById(R.id.update);
	update.setOnClickListener(this);
	image.setOnClickListener(new OnClickListener() {		
		public void onClick(View view) {
			// TODO Auto-generated method stub
			show(view);
		}		
	});
}
	public void initView(){
		setContentView(R.layout.myinfo);
		//view = findViewById(R.drawable.l);
		image = findViewById(R.id.head_phone);
		Bitmap bitmap = BitmapFactory.decodeFile(path+"head.jpg");
		if(bitmap!=null){
			Drawable drawable = new BitmapDrawable(bitmap);
			image.setImageDrawable(drawable);
		}else{
			image.setImageBitmap(circleImage());
		}		
	}
	public void show(View view) {
	// TODO Auto-generated method stub
	inflate =  LayoutInflater.from(this).inflate(R.layout.bottom, null);
	camera = inflate.findViewById(R.id.camera);
	pic = inflate.findViewById(R.id.pic);
	cancel = inflate.findViewById(R.id.cancel);
	camera.setOnClickListener(this);
	pic.setOnClickListener(this);
	cancel.setOnClickListener(this);
	dialog = new Dialog(this);
	dialog.setContentView(inflate);
	Window dialogWindow = dialog.getWindow();
	dialogWindow.setGravity(Gravity.BOTTOM);
	WindowManager.LayoutParams lp = dialogWindow.getAttributes();
	lp.y = 20;
	dialogWindow.setAttributes(lp);
	dialog.show();
	}
public void actionStart(Context context,User user){
	Intent intent = new Intent(context, InformationActivity.class);
	intent.putExtra("userId",user.getUserId());
	context.startActivity(intent);	
//	if(position==1){
//		update.setText("修改");
//	}else{
//		update.setText("私信");
//	}
	}
/*
 * 将图片换成指定大小
 */
public Bitmap resizeBitmap(float newHeight,float newWidth,Bitmap bitmap){
	Matrix matrix = new Matrix();
	matrix.postScale(newWidth/bitmap.getWidth(),newHeight/bitmap.getHeight() );
	Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(),matrix,true);
	return newBitmap;
}
/*
 * 将图片制成圆形图片
 */
public 	Bitmap circleImage(){
	Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.l);
	bitmap = resizeBitmap(400,400,bitmap);
	int width = bitmap.getWidth();
	int height = bitmap.getHeight();
	//构造新图纸
	Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	Canvas canvas = new Canvas(output);
	Paint paint = new Paint();
	paint.setAntiAlias(true);
	canvas.drawARGB(0, 0, 0, 0);
	paint.setColor(0xff424242);
	canvas.drawCircle(width/2, height/2, width/2, paint);
	paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
	Rect src = new Rect(0, 0, width, width);
	Rect dst = new Rect(0, 0, width, height);
	canvas.drawBitmap(bitmap, src, dst,paint);
	return output;
}

public void onClick(View view) {
	// TODO Auto-generated method stub
	switch (view.getId()) {
	case R.id.camera:
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//开启相机应用程序并返回图片
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"head.jpg")));
			startActivityForResult(intent, 2);
		} catch (Exception e) {
			Toast.makeText(this, "相机无法启动，请先开启相机使用权限", Toast.LENGTH_SHORT);
		}	
		Toast.makeText(this, "拍照", Toast.LENGTH_SHORT).show();		
		dialog.dismiss();
		break;
	case R.id.pic:
		Intent intent = new Intent(Intent.ACTION_PICK,null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(intent ,1);
		Toast.makeText(this, "相册", Toast.LENGTH_SHORT).show();
		dialog.dismiss();
		break;
	case R.id.cancel:
		Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();	
		dialog.dismiss();
		break;
	case R.id.update:
		UpdateInformation uif;
		Intent updateInformation;
		if(update.getText()=="修改"){
			updateInformation = new Intent(InformationActivity.this, UpdateInformation.class);
			UpdateInformation update = new UpdateInformation();
			update.actionStart(InformationActivity.this,user );}
//		}else if(update.getText()=="私信"){
//			updateInformation = new Intent(InformationActivity.this, ChatActivity.class);
//			updateInformation.putExtra("userId", user.getUserId());
//		}

	}	
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub

	switch (resultCode) {
	case 1:
		if(resultCode==RESULT_OK){
			cropPhoto(data.getData());
		}
		break;
	case 2:
		if(resultCode==RESULT_OK){
			File temp = new File(Environment.getExternalStorageDirectory()+"/head.jpg");
			cropPhoto(Uri.fromFile(temp));
		}
	case 3:
		if(data!=null){
			Bundle extras = data.getExtras();
			head = extras.getParcelable("data");
			if(head!=null){
				setPicToView(head);
				image.setImageBitmap(head);
			}
		}
	default:
		break;
	}
	super.onActivityResult(requestCode, resultCode, data);
}
private void setPicToView(Bitmap mBitmap) {
	// TODO Auto-generated method stub
	String sdStatus = Environment.getExternalStorageState();
    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
           return;
       }
    FileOutputStream b = null;
    File file = new File(path);
    file.mkdirs();// 创建文件夹
    String fileName =path + "head.jpg";//图片名字
    try {
        b = new FileOutputStream(fileName);
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } finally {
        try {
            //关闭流
            b.flush();
            b.close();
        } catch (IOException e) {
            e.printStackTrace();

}
    }
}
private void cropPhoto(Uri uri) {
	// TODO Auto-generated method stub
	Intent intent = new Intent("com.android.camera.action.CROP");
	intent.setDataAndType(uri, "image/*");
	intent.putExtra("crop", "true");
	 // aspectX aspectY 是宽高的比例
    intent.putExtra("aspectX", 1);
    intent.putExtra("aspectY", 1);
    // outputX outputY 是裁剪图片宽高
    intent.putExtra("outputX", 150);
    intent.putExtra("outputY", 150);
    intent.putExtra("return-data", true);
    startActivityForResult(intent, 3);
}

}

