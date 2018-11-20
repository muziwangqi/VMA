package com.soling.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.soling.model.User;

import android.graphics.drawable.Drawable;



public class UserList {
 private static List<User> users;
 private static User user;
 private static Drawable dra;
 static Date da =new Date();
 public static List<User> getUsers(){
	 users = new ArrayList<User>();
	 for(int i=0;i<=5;i++){	
		 Date date = new Date(1999,11,12);		 
		 buildUser("a",date,i,110000,22222);
		 users.add(user);
	 }
	 for(int i=0;5<i&&i<=8;i++){
		 Date date = new Date(1995,11,6);
		 buildUser("d",date,i,110000,22222);
		 users.add(user);
	 }
	 for(int i=0;8<i&&i<=13;i++){
		 Date date = new Date(1993,11,8);
		 buildUser("f",date ,i,110000,22222);
		 users.add(user);
	 }
	 for(int i=0;13<i&&i<=15;i++){
		 Date date = new Date(1996,11,6);	 
		 buildUser("g",date,i,110000,22222);
		 users.add(user);
	 }
	 for(int i=0;15<i&&i<=17;i++){
		 Date date = new Date(1989,11,6);
		 buildUser("h",date,i,110000,22222);
		 users.add(user);
	 }
	 for(int i=0;17<i&&i<=19;i++){	
		 Date date = new Date(1985,11,6);
		 buildUser("j",date,i,110000,22222);
		 users.add(user);
	 }
	 for(int i=0;19<i&&i<=25;i++){
		 Date date = new Date(1985,11,6);
		 buildUser("m",date,i,110000,22222);
		 users.add(user);
	 }
	 return users;
 }
 public static User buildUser(String c,Date date,int i,int j, int k){
	 user = new User();
	 user.setNickName("lqq");
	 user.setHeadWord(c);
	 user.setFolloweds(23);
	 user.setFollows(225);
	 user.setProvice(user.provice(j));
	 user.setProvice(user.provice(k));
	 user.setSignature("sfdszgfdrhynjsghsrtjhEWtgarhuysjhwe他个人和娲女");
	 user.setStatus("在线");
	 user.setUserId("15649859526526");
	 user.setVipType(5);
	 user.setAge(user.age(date));
	 user.setSex(user.sex(i));
	 return user;
	 
 }

}
