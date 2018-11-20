package com.soling.presenter;

import java.util.List;

import com.soling.api.NeteaseAPIAdapter;
import com.soling.model.Music;
import com.soling.model.User;
import com.soling.view.activity.LoginActivity;
import com.soling.view.fragment.PhoneFragment;

import android.content.Context;
import android.os.Handler;
import android.widget.TextView;


public class LoginPresenter {
	private LoginActivity loginActivity;
	private static User user;
	protected static final int SHOW_RESPONSE = 0;	
	private String user_id;
	private String music_id;
	private static List<User> users;	
	private Music music;
	private List<Music> musicList;
	private boolean isLogin=false;
	private Handler handler;
	private TextView tv;
	private String url;
	private PhoneFragment pf = new PhoneFragment();
	public LoginPresenter(LoginActivity loginActivity) {
		super();
		this.loginActivity = loginActivity;
	}
	public void Login(final Context context,final String userNumber,final String userPassword){
		new Thread(new Runnable() {			
			public void run() {
				// TODO Auto-generated method stub
				user = NeteaseAPIAdapter.getInstance().getuser(userNumber, userPassword);		
				users = NeteaseAPIAdapter.getInstance().getMyAttention(user.getUserId());
				pf.getActivity().runOnUiThread(new Runnable() {
					public void run() {
						pf.refreshPhoneList(user,users);
					}
				});
			}
		}).start();		
	}	
}
