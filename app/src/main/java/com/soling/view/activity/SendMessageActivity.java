package com.soling.view.activity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.soling.R;

import static android.app.Activity.RESULT_OK;

public class SendMessageActivity extends BaseActivity {
    private EditText contentEditText;
    private EditText phoneNumberEditText;
    private IntentFilter sendFilter;
    private SendStatusReceiver sendStatusReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.send_message);
        phoneNumberEditText = findViewById(R.id.message_recriver);
        contentEditText = findViewById(R.id.message_content);
        Bundle myBundle = this.getIntent().getExtras();
        String[] myText  = myBundle.getStringArray("phoneF");
        if(!myText[1].equals("")||myText[1]!=null){
            phoneNumberEditText.setText(myText[1]);
        }else{
            phoneNumberEditText.setText(myText[0]);
        }
        final String phoneNumber = myText[0];
        findViewById(R.id.message_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = contentEditText.getText().toString();
                final String name = phoneNumberEditText.getText().toString();
                sendMessage (v ,phoneNumber ,content);
                final InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(phoneNumberEditText.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(contentEditText.getWindowToken(), 0);
            }
        });
    }


    @Override
    public void shortToast(String str) {
        super.shortToast(str);
    }

    /*
    发送信息的方法
    */
    public void sendMessage (View view ,String number , String content) {
        sendFilter = new IntentFilter();
        sendStatusReceiver = new SendStatusReceiver();
        registerReceiver(sendStatusReceiver,sendFilter);
        Intent sendIntent = new Intent("SENT_SMS_ACTION");
        PendingIntent pi = PendingIntent.getBroadcast(SendMessageActivity.this,0,sendIntent,0);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number.trim(),null,content.trim(),pi,null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(sendStatusReceiver);
    }
}

class SendStatusReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if(getResultCode() == RESULT_OK){
            Toast.makeText(context,"短信发送成功",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context,"短信发送失败",Toast.LENGTH_LONG).show();
        }
    }

}