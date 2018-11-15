package com.soling.view.activity;

import java.util.Calendar;

import com.soling.model.User;
import com.soling.R;



import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class UpdateInformation extends Activity implements OnClickListener {
	private ImageView myHead;
	private ImageView myCode;
	private EditText myNickName;
	private TextView myNumber;
	private EditText mySex;
	private TextView myBirthdy;
	private TextView myAddress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updateinformation);
		myHead = findViewById(R.id.my_head);
		myCode = findViewById(R.id.my_code);
		myNickName = findViewById(R.id.my_nickname);
		myNumber = findViewById(R.id.my_number);
		mySex = findViewById(R.id.my_sex);
		myBirthdy = findViewById(R.id.my_birthdy);
		myAddress = findViewById(R.id.my_address);
		myHead.setOnClickListener(this);
		myCode.setOnClickListener(this);
		myBirthdy.setOnClickListener(this);
		myAddress.setOnClickListener(this);
	}
	public static void actionStart(Context context,User user){
		Intent intent = new Intent(context, InformationActivity.class);
		intent.putExtra("userId",user.getUserId());
		context.startActivity(intent);
		}
	public void initView(){
		
	}
	public void onPointerCaptureChanged(boolean hasCapture) {
		// TODO Auto-generated method stub
		
	}
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.my_head:
			
			break;
		case R.id.my_code:
			
			break;
		case R.id.my_birthdy:
			final Calendar calendar = Calendar.getInstance(); 
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH); 
			int day = calendar.get(Calendar.DAY_OF_MONTH); 
			new DatePickerDialog(UpdateInformation.this,new DatePickerDialog.OnDateSetListener() {			
				public void onDateSet(DatePicker view, int year, int mouthOfYear, int dayOfMonth) {
					// TODO Auto-generated method stub
					myBirthdy.setText(year+"-"+mouthOfYear+1+"-"+dayOfMonth);
				}
			},calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
			break;
		case R.id.my_address:
	
		break;
		default:
			break;
		}
	}
	public static void showDatePickerDialog(Activity activity,int themeResId,final TextView tv,Calendar calendar){
		
	}
}
