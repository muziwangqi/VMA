package com.soling.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;



import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class User {
	
	private String userId;//用户唯一识别号
	private String city;//城市
	private Bitmap avatarUrl;//头像属性
	private Bitmap backgroundImgSrc;//个人主页背景
	private String signature;//个人签名
	private String status;//状态
	private int vipType;//vip类型
	private Date birthday;//生日
	private String provice;//省份
	private String nickName;//昵称
	private int follows;//关注数
    private int followeds;//粉丝数量 
    private String headWord; 
    private int age;
    private String sex;
    
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	
	public User() {
		super();
	}
	public User(String city, String signature, String status, int vipType,
			String provice, String nickName, int follows, int followeds, int age,
			String sex) {
		super();
		this.city = city;
		this.signature = signature;
		this.status = status;
		this.vipType = vipType;
		this.provice = provice;
		this.nickName = nickName;
		this.follows = follows;
		this.followeds = followeds;
		this.age = age;
		this.sex = sex;
	}
	public User(String userId, String nickName, int follows, int followeds) {
		super();
		this.userId = userId;
		this.nickName = nickName;
		this.follows = follows;
		this.followeds = followeds;
	}
	public String getHeadWord() {
		return headWord;
	}
	public void setHeadWord(String headWord) {
		String namePinYin = PinYinUtil.getPinYin(nickName);
		headWord = namePinYin.substring(0, 1);
		this.headWord = headWord;
	}
	public int getFollows() {
		return follows;
	}
	public void setFollows(int follows) {
		this.follows = follows;
	}
	public int getFolloweds() {
		return followeds;
	}
	public void setFolloweds(int followeds) {
		this.followeds = followeds;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getProvice() {
		return provice;
	}
	public void setProvice(String provice) {
		this.provice = provice;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Bitmap getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(Bitmap avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public Bitmap getBackgroundImgSrc() {
		return backgroundImgSrc;
	}
	public void setBackgroundImgSrc(Bitmap backgroundImgSrc) {
		this.backgroundImgSrc = backgroundImgSrc;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getVipType() {
		return vipType;
	}
	public void setVipType(int vipType) {
		this.vipType = vipType;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	/*
	 * 汉字转换成拼音工具类
	 */
	public static class PinYinUtil{
		public static String getPinYin(String name){
			StringBuffer bf = new StringBuffer();
			HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
			format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
			format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			char[] c = name.toCharArray();
			for(int i=0;i<c.length;i++){
				if(Character.isWhitespace(c[i])){
					continue;
				}
				try {
					String[] arr = PinyinHelper.toHanyuPinyinStringArray(c[i], format);
					if(arr!=null)bf.append(arr[0]);
					else bf.append(c[i]);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					bf.append(c[i]);
				}				
			}
			return bf.toString();
		}
	}
	public int age(Date date){
		Calendar cal = Calendar.getInstance();
		if(cal.before(date)){
			int yearNow = cal.get(Calendar.YEAR);
			int monthNow = cal.get(Calendar.MONTH);
			int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
			cal.setTime(date);
			int yearBirth = cal.get(Calendar.YEAR);
			int monthBirth = cal.get(Calendar.MONTH);
			int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH); 
			age = yearNow-yearBirth;
			if(monthNow<=monthBirth){
				if(monthNow==monthBirth){
					if(dayOfMonthNow<dayOfMonthBirth){
						age--;
					}
				}else{
					age--;
				}				
			}
			return age;
		}
		return age;		
	}
	public String sex(int i){
		if(i==0){
			sex = "保密";			
		}else if(i==1){
			sex = "男";
		}else if(i==2){
			sex="女";
		}else{
			sex="无";
		}
		return sex;
	}
	 public String city(int i){	 
		 return "深圳";
	 }
	 public String provice(int i){	 
		 return "广东";
	 }
	 public String status(int i){
		 String s;
		 if(i==0){
			 s="在线";
		 }else{
			 s="离线";
		 }
		 return "广东";
	 }
}
