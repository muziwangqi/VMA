package com.soling.view.activity;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.soling.model.PhoneDto;
import com.soling.model.User;
import com.soling.R;
import com.soling.utils.PhoneUtil;
import com.soling.utils.PhotoHandleUtil;
import com.soling.view.adapter.PhoneCallLogAdapter;


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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class InformationActivity extends BaseActivity implements OnClickListener {
    private ImageView image;
    private View view;
    private TextView pic;
    private TextView cancel;
    private TextView camera;
    private View inflate;
    private Dialog dialog;
    private Bitmap head;
    private ImageView goPhone;
    private ImageView goInformation;
    private TextView card;
    private TextView phone;
    private ImageView send;
    private static String path = "/sdcard/demohead/";
    private int SELECT_PICTURE = 0x00;
    private int SELECT_CAMER = 0x01;
    private PhoneDto phoneDto = new PhoneDto();
    Bitmap bitmap;
    PhotoHandleUtil photoHandleUtil = new PhotoHandleUtil();
    private ListView personListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        initView();
        String name = (String) getIntent().getExtras().get("name");
        long iid = (long) getIntent().getExtras().get("id");
        String iphone = (String) getIntent().getExtras().get("phoneNumber");
       // refreshInformation( phoneDto);
       phoneDto.setTelPhone(iphone);
       phoneDto.setName(name);
       phoneDto.setId(iid);
        refreshInformation(name,iid,iphone);
        personListView = findViewById(R.id.music_list_recently);
        image = findViewById(R.id.head_phone);
        goPhone = findViewById(R.id.go_phone);
        goInformation = findViewById(R.id.go_information);
        card = findViewById(R.id.friend_card);
        phone = findViewById(R.id.friend_phone);
        send = findViewById(R.id.information_update);
        refreshCallLog(iphone);
        send.setOnClickListener(this);
        goPhone.setOnClickListener(this);
        goInformation.setOnClickListener(this);
        card.setOnClickListener(this);
        image.setOnClickListener(this);
        phone.setOnClickListener(this);
    }

    public void initView() {
        setContentView(R.layout.myinfo);
        //view = findViewById(R.drawable.l);
        image = findViewById(R.id.head_phone);
        Bitmap bitmap = BitmapFactory.decodeFile(path + "head.jpg");
        if (bitmap != null) {
            Drawable drawable = new BitmapDrawable(bitmap);
            image.setImageDrawable(drawable);
        } else {
            image.setImageBitmap(photoHandleUtil.circleImage(getResources()));
        }
    }

    public void show(View view) {
        // TODO Auto-generated method stub
        inflate = LayoutInflater.from(this).inflate(R.layout.bottom, null);
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


    public void refreshInformation( String name , long iid , String number){
        TextView phoneNumber = findViewById(R.id.friend_phone);
        TextView id = findViewById(R.id.friend_id);
        TextView nickname = findViewById(R.id.nickname);
        phoneNumber.setText(number);
        id.setText(iid+"");
        nickname.setText(name);
    }


    public void onClick(View view) {
        // TODO Auto-generated method stub
        Bundle bundle = new Bundle();
        String[] phoneF = {phoneDto.getTelPhone(),phoneDto.getName()};
        long phoneF1 =  phoneDto.getId();
        switch (view.getId()) {
            case R.id.camera:
                try {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//开启相机应用程序并返回图片
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
                    startActivityForResult(intent, 2);
                } catch (Exception e) {
                    Toast.makeText(this, "相机无法启动，请先开启相机使用权限", Toast.LENGTH_SHORT);
                }
                Toast.makeText(this, "拍照", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                break;
            case R.id.pic:
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 1);
                Toast.makeText(this, "相册", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                break;
            case R.id.cancel:
                Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                break;
            case R.id.information_update:
                bundle.putLong("phoneF1",phoneF1);
                bundle.putStringArray("phoneF",phoneF);
                intentJump(InformationActivity.this,UpdateInformation.class,bundle);
                break;
            case R.id.friend_phone:
                String phoneNumber = phone.getText().toString();
                call(phoneNumber);
                break;
            case R.id.go_phone:
                call(phoneDto.getTelPhone());
                break;
            case R.id.go_information:
                bundle.putStringArray("phoneF",phoneF);
                intentJump(InformationActivity.this,SendMessageActivity.class,bundle);
                break;
            case R.id.friend_card:
                bundle.putString("phoneNumber",phoneDto.getTelPhone());
                intentJump(InformationActivity.this,TwoDimensionCode.class,bundle);
                break;
                }

        }

    @Override
    public void intentJump(Context context, Class<?> cls) {
        super.intentJump(context, cls);
    }

    @Override
    public void intentJump(Context context, Class<?> cls, Bundle bundle) {
        super.intentJump(context, cls, bundle);
    }

    /**
     * 调用拨号界面
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (resultCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    photoHandleUtil.cropPhoto(data.getData());
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/head.jpg");
                    photoHandleUtil.cropPhoto(Uri.fromFile(temp));
                }
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        photoHandleUtil.setPicToView(head);
                        image.setImageBitmap(head);
                    }
                }
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void refreshCallLog(String phone){
        PhoneUtil phoneUtil = new PhoneUtil(getBaseContext());
        PhoneCallLogAdapter phoneCallLogAdapter = new PhoneCallLogAdapter(getBaseContext(), phoneUtil.selectPhoneLog(phone));
        personListView.setAdapter(phoneCallLogAdapter);
    }

}

