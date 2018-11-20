package com.soling.presenter;

import java.util.Date;
import java.util.List;

import com.soling.model.User;
import com.soling.view.fragment.PhoneFragment;

public class MainPresenter implements MainPresenterInterface {	
	public PhoneFragment phoneFragment = new PhoneFragment();
	public void showPhoneList() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {			
			public void run() {

			}
		}).start();	
	}

}
