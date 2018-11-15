package com.soling.presenter;

import java.util.List;

import com.soling.model.User;

public interface MainActivityInterface {
	void refreshPhoneList(User user,List<User> users);
}
interface MainPresenterInterface {
	void showPhoneList();
}