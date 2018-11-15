package com.soling.view.fragment;

import java.util.Date;
import java.util.List;

import com.soling.api.NeteaseAPIAdapter;
import com.soling.database.UserList;
import com.soling.model.User;
import com.soling.presenter.MainActivityInterface;
import com.soling.presenter.MainPresenter;
import com.soling.view.activity.InformationActivity;
import com.soling.view.adapter.UserAdapter;
import com.soling.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class PhoneFragment extends Fragment implements MainActivityInterface{
	
	private List<User> users;
	private ListView personListView;
	private UserAdapter userAdapter;
	private View view ;
	private String userId;
	private User user;
//	private ImageView myHead;
//	private TextView myName;
	//private MainPresenter mainPresenter = new MainPresenter();
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub		
		super.onAttach(activity);
		user = UserList.buildUser("h", new Date(16660812), 1, 1, 40000);
		users  =UserList.getUsers(); 
		userAdapter = new UserAdapter(getActivity(),R.layout.person_list,users);
	}	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
			
	}	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view =  inflater.inflate(R.layout.person_lists,container,false);
		personListView = view.findViewById(R.id.person_list);
		personListView.setAdapter(userAdapter);
		//mainPresenter.showPhoneList();
		personListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
//				position =1;
//				User user = users.get(position);
				InformationActivity in = new InformationActivity();
				in.actionStart(getActivity(), user);
//				if(position==1){			
//					in.actionStart(getActivity(), user);
//				}else{
//					in.actionStart(getActivity(), user);	
//				}
			}
		});
		return view;
	}	

	public void refreshPhoneList(User user, List<User> users) {
		// TODO Auto-generated method stub
		user = UserList.buildUser("h", new Date(16660812), 1, 1, 40000);
		users  =UserList.getUsers(); 
		userAdapter = new UserAdapter(getActivity(),R.layout.person_list,users);	
	}

	public void runOnUiThread(Runnable runnable) {
		// TODO Auto-generated method stub
		
	}
		
			
//	}
	
}
