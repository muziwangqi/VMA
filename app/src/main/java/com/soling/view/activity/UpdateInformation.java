package com.soling.view.activity;

import java.util.Calendar;

import com.soling.model.PhoneDto;
import com.soling.model.User;
import com.soling.R;
import com.soling.utils.BitmapUtil;
import com.soling.utils.PhoneUtil;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class UpdateInformation extends BaseActivity implements OnClickListener {
    private ImageView updateHead;
    private EditText updateNickname;
    private EditText updateNumber;
    private TextView updateBirthdy;
    private TextView updateMusic;
    private TextView updateDelete;
    private TextView updateCom;
    private EditText updateEemail;
    private EditText updateCompany;
    private EditText updatePosition;
    private EditText updateIm;
    private PhoneDto phoneDto = new PhoneDto();
    private PhoneUtil phoneUtil = new PhoneUtil(this
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updateinformation);
        updateHead = findViewById(R.id.update_head);
        updateNumber = findViewById(R.id.update_number);
        updateNickname = findViewById(R.id.update_nickname);
        updateBirthdy = findViewById(R.id.update_birthdy);
        updateMusic = findViewById(R.id.update_music);
        updateDelete = findViewById(R.id.update_delete);
        updateCom = findViewById(R.id.update_com);
        updateEemail = findViewById(R.id.update_email);
        updateCompany = findViewById(R.id.update_company);
        updatePosition = findViewById(R.id.update_position);
        updateIm = findViewById(R.id.update_im);
        Bundle myBundle = this.getIntent().getExtras();
        if(myBundle!=null){
            ; String[] myText = myBundle.getStringArray("phoneF");
            long id = myBundle.getLong("phoneF1");
            if (myText != null ) {
                phoneDto.setName(myText[0]);
                phoneDto.setTelPhone(myText[1]);
                phoneDto.setId(id);
                refreshUpdateActivity(myText[0], myText[1]);
        }
        }

        updateHead.setOnClickListener(this);
        updateDelete.setOnClickListener(this);
        updateBirthdy.setOnClickListener(this);
        updateMusic.setOnClickListener(this);
        updateNumber.setOnClickListener(this);
        updateNickname.setOnClickListener(this);
        updateCom.setOnClickListener(this);
    }

    public static void actionStart(Context context, User user) {
        Intent intent = new Intent(context, InformationActivity.class);
        intent.putExtra("userId", user.getUserId());
        context.startActivity(intent);
    }

    public void initView() {

    }

    public void onPointerCaptureChanged(boolean hasCapture) {
        // TODO Auto-generated method stub

    }

    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.update_com:
                String number = updateNumber.getText().toString();
                String nickName = updateNickname.getText().toString();
                String email = updateEemail.getText().toString();
                String company = updateCompany.getText().toString();
                String position = updatePosition.getText().toString();
                String im = updateIm.getText().toString();
                if(phoneDto.getId()+""!=""||phoneDto.getId()+""!=null){
                    phoneUtil.updateContact(phoneDto.getId(),nickName,number,email,company,position,im);
                }else{
                    phoneUtil.addContact(nickName,number,email,company,position,im);
                }

                break;
            case R.id.update_delete:
                phoneUtil.deleteContact(phoneDto.getId());
                break;
            case R.id.update_head:
                break;
            case R.id.update_music:
                break;
            case R.id.update_birthdy:
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(UpdateInformation.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int mouthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        updateBirthdy.setText(year + "-" + mouthOfYear + 1 + "-" + dayOfMonth);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            default:
                break;
        }
    }

    public static void showDatePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {

    }

    public void refreshUpdateActivity(String phoneNumber, String nickName) {
        updateNumber.setText(phoneNumber);
        updateNickname.setText(nickName);
        Drawable drawable = getResources().getDrawable(R.drawable.headphoto);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.headphoto);
        RoundedBitmapDrawable rbd = BitmapUtil.roundedBitmapDrawable(bmp);
        updateHead.setImageDrawable(rbd);
    }
}
