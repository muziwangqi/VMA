package com.soling.view.activity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.soling.presenter.LoginPresenter;
import com.soling.R;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity  {	
	private Button login;
	private EditText userNumber;
	private EditText password;
	private LoginPresenter loginPresenter;
	private String user_number;
	private String user_password;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		loginPresenter = new LoginPresenter(this);
		login = findViewById(R.id.login);
		login.setOnClickListener(new OnClickListener() {			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userNumber = findViewById(R.id.user_name);
				user_number=userNumber.toString();		
				password = findViewById(R.id.password);
				user_password=password.toString();
				loginPresenter.Login(LoginActivity.this, user_number, user_password);
			}
		});
		
	}
	
	
}
