package com.soling.presenter;



import java.util.Hashtable;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.provider.Telephony.Sms.Conversations;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.soling.R;
import com.soling.model.User;
import com.soling.view.activity.TwoDimensionCode;


public class CodePresenter {
	private TwoDimensionCode twoDimensionCode=new TwoDimensionCode();
	private ImageView myTwoDimensionCode;
	private Bitmap myDimensionCode;
	public static Bitmap myCode(String phone,Bitmap head,Bitmap bitmapBackground){
//		String userId = user.getUserId();
//		Bitmap bitmap = user.getAvatarUrl();
//		Drawable drawable = r.getDrawable(R.drawable.headphoto);
//		Bitmap bmp =  BitmapFactory.decodeResource(r,R.drawable.headphoto);
		Bitmap myCode = CodePresenter.makeQRImage(head, phone, 552, 508);
		//myCode = CodePresenter.addBackGround(myCode, background)
		return myCode;
	}
	public static Bitmap makeQRImage(Bitmap headBitmap,String content,int QR_WIDTH,int QR_HEIGHT){
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		try {
			BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT,hints);
			int[] pixels = new int[QR_WIDTH*QR_HEIGHT];
			for(int y=0;y<QR_HEIGHT;y++){
				for(int x=0;x<QR_WIDTH;x++){
					if(bitMatrix.get(x, y)){
						if (bitMatrix.get(x, y)) {
							pixels[y * QR_WIDTH + x] = 0xffffffff;//黑点
						} else {
							pixels[y * QR_WIDTH + x] = 0x00ffffff;//透明点,白点为0xffffffff
						}
					}
				}
			}
			Bitmap bm = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
			bm.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			int headWidth = headBitmap.getWidth();
			int headHeight = headBitmap.getHeight();
			if(QR_WIDTH==0||QR_HEIGHT==0){
				return null;
			}
			if(headWidth==0||headHeight==0){
				return bm;
			}
			//图片绘制在二维码中央,头像图片大小为整个二维码整体大小的1/5
			float scaleFactor = QR_WIDTH*1.0f/5/headWidth;
			Canvas canvas = new Canvas(bm);
			canvas.drawBitmap(bm, 0, 0,null);
			canvas.scale(scaleFactor, scaleFactor,QR_WIDTH/2,QR_HEIGHT/2);
			canvas.drawBitmap(headBitmap, (QR_WIDTH-headWidth)/2,(QR_HEIGHT-headHeight)/2,null);
//			//给二维码图片添加背景，放回一个新的二维码
//			Bitmap newBitmap = addBackGround(bm,bitmapBackground);
            canvas.save();
            canvas.restore();
			return bm;

		} catch (WriterException e) {
			// TODO Auto-generated catch block			
			e.printStackTrace();
		}		
		return null;
	}	
	//给二维码图片加背景
	public static Bitmap addBackGround(Bitmap foreground,Bitmap background){
		int bgWidth = background.getWidth();
		int bgHeight = background.getHeight();
		int fgWidth = foreground.getWidth();
		int fgHeight = foreground.getHeight();
		Bitmap newBitmap = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(newBitmap);
		canvas.drawBitmap(background,0, 0, null);
		canvas.drawBitmap(foreground, (bgWidth-fgWidth)/2, (bgHeight-fgHeight)*3/5+70,null);
//		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.save();
		canvas.restore();
		return newBitmap;
	}
}
