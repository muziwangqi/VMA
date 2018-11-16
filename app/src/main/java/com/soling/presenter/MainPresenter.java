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
				// TODO Auto-generated method stub
//				User user = NeteaseAPIAdapter.getInstance().getuser("16620839486", "95l11q06q");		
//				List<User> users = NeteaseAPIAdapter.getInstance().getMyAttention(user.getUserId());
//				final User user = UserList.buildUser("h", new Date(16660812), 1, 1, 40000);
//				final List<User> users  =UserList.getUsers();
//				phoneFragment.getActivity().runOnUiThread(new Runnable() {
//					public void run() {
//						phoneFragment.refreshPhoneList(user, users);
//					}
//				});
			}
		}).start();	
	}

}
